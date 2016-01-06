package com.dreamspace.superman.Common;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.R;
import com.dreamspace.superman.event.DbChangeEvent;
import com.dreamspace.superman.event.ImTypeMessageEvent;
import com.dreamspace.superman.model.api.SimpleInfo;
import com.ds.greendao.Conversation;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zhangxiaobo on 15/4/20.
 */
public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

    private Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(final AVIMTypedMessage message, final AVIMConversation conversation, AVIMClient client) {

        String clientID = "";
        try {
            clientID = AVImClientManager.getInstance().getClientId();
            if (client.getClientId().equals(clientID)) {

                // 过滤掉自己发的消息
                if (!message.getFrom().equals(clientID)) {
                    sendEvent(message, conversation, Integer.parseInt(message.getFrom()));
                    if (NotificationUtils.isShowNotification(conversation.getConversationId())) {
                        getInfoFromServer(Integer.parseInt(message.getFrom()), new InfoLoadingListener() {
                            @Override
                            public void onSuccess(SimpleInfo info) {
                                sendNotification(message, conversation, info.getNickname());
                            }

                            @Override
                            public void onFail() {
                                sendNotification(message, conversation, "学员");
                            }
                        });
                    }
                }
            } else {
                client.close(null);
            }
        } catch (IllegalStateException e) {
            client.close(null);
        }
    }

    /**
     * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
     * 稍后应该加入 db
     *
     * @param message
     * @param conversation
     */
    private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation, int memberId) {
        Conversation dbCon = new Conversation();
        dbCon.setChatTime(conversation.getLastMessageAt());
        dbCon.setIsRead(true);
        String content = context.getResources().getString(R.string.unspport_message_type);
        if (message instanceof AVIMTextMessage) {
            content = ((AVIMTextMessage) message).getText();
        }
        dbCon.setLastContent(content);
        dbCon.setMemberId((long) memberId);
        storeIntoDb(dbCon, message, conversation);

    }

    private void sendMsgByBus(AVIMTypedMessage message, AVIMConversation conversation) {
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
        DbChangeEvent changeEvent = new DbChangeEvent();
        EventBus.getDefault().post(event);
        EventBus.getDefault().post(changeEvent);
    }

    private void storeIntoDb(final Conversation dbCon, final AVIMTypedMessage message, final AVIMConversation conversation) {
        final Conversation previous = DbRelated.findConById(context, dbCon.getMemberId());
        if (previous != null) {
            getInfoFromServer(dbCon.getMemberId().intValue(), new InfoLoadingListener() {
                @Override
                public void onSuccess(SimpleInfo info) {
                    dbCon.setMemberName(info.getNickname());
                    dbCon.setMemberAvater(info.getImage());
                    DbRelated.updateCon(context, dbCon);
                    sendMsgByBus(message, conversation);
                }

                @Override
                public void onFail() {
                    DbRelated.updateCon(context, dbCon);
                    sendMsgByBus(message, conversation);
                }
            });
        } else {
            getInfoFromServer(dbCon.getMemberId().intValue(), new InfoLoadingListener() {
                @Override
                public void onSuccess(SimpleInfo info) {
                    dbCon.setMemberName(info.getNickname());
                    dbCon.setMemberAvater(info.getImage());
                    DbRelated.insertCon(context, dbCon);
                    sendMsgByBus(message, conversation);
                }

                @Override
                public void onFail() {
                    dbCon.setMemberAvater(Constant.FAIL_AVATER);
                    dbCon.setMemberName(Constant.FAIL_MEMBER_NAME);
                    DbRelated.insertCon(context, dbCon);
                    sendMsgByBus(message, conversation);
                }
            });
        }
    }

    private void getInfoFromServer(int id, final InfoLoadingListener listener) {
        if (NetUtils.isNetworkConnected(context)) {
            ApiManager.getService(context.getApplicationContext()).getUserInfoById(id, new Callback<SimpleInfo>() {
                @Override
                public void success(SimpleInfo userInfo, Response response) {
                    if (userInfo != null) {
                        listener.onSuccess(userInfo);
                    } else {
                        listener.onFail();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    listener.onFail();
                }
            });
        } else {
            listener.onFail();
        }
    }

    private void sendNotification(AVIMTypedMessage message, AVIMConversation conversation, String nickname) {
        String notificationContent = message instanceof AVIMTextMessage ?
                ((AVIMTextMessage) message).getText() : context.getString(R.string.unspport_message_type);
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra(Constant.CONVERSATION_ID, conversation.getConversationId());
        intent.putExtra(Constant.MEMBER_ID, message.getFrom());
        intent.putExtra(Constant.MEMBER_NAME, nickname);
        NotificationUtils.showNotification(context, "您收到了" + nickname + "的私信", notificationContent, null, intent);
    }

    private interface InfoLoadingListener {
        void onSuccess(SimpleInfo info);

        void onFail();
    }

}

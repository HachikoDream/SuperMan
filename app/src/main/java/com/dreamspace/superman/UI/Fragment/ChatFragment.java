package com.dreamspace.superman.UI.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.DbRelated;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.NotificationUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.MultipleItemAdapter;
import com.dreamspace.superman.UI.View.AVInputBottomBar;
import com.dreamspace.superman.event.DbChangeEvent;
import com.dreamspace.superman.event.ImTypeMessageEvent;
import com.dreamspace.superman.event.ImTypeMessageResendEvent;
import com.dreamspace.superman.event.InputBottomBarTextEvent;
import com.dreamspace.superman.model.api.SimpleInfo;
import com.ds.greendao.Conversation;

import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by wli on 15/8/27.
 * 将聊天相关的封装到此 Fragment 里边，只需要通过 setConversation 传入 Conversation 即可
 */
//// TODO: 2015/11/20   发布之前清空消息记录
public class ChatFragment extends Fragment implements Handler.Callback {
    protected AVIMConversation imConversation;

    protected MultipleItemAdapter itemAdapter;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager layoutManager;
    protected SwipeRefreshLayout refreshLayout;
    protected AVInputBottomBar inputBottomBar;
    private int memberId;//由setconversation方法传入
    private String lastContent = null;//两个人的最后聊天内容,用于写入数据库
    private ProgressDialog pd;
    private Handler mHandler;
    private static final int STORE_INTO_DB = 233;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_rv_chat);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_chat_srl_pullrefresh);
        refreshLayout.setEnabled(false);
        inputBottomBar = (AVInputBottomBar) view.findViewById(R.id.fragment_chat_inputbottombar);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new MultipleItemAdapter();
        recyclerView.setAdapter(itemAdapter);
        mHandler = new Handler(this);
        EventBus.getDefault().register(this);
        return view;
    }

    private void showPd(String msg) {
        if (CommonUtils.isEmpty(msg)) {
            msg = "正在加载数据,请稍后";
        }
        if (pd == null) {
            pd = ProgressDialog.show(getActivity(), "", msg, true, false);
        } else {
            pd.show();
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AVIMMessage message = itemAdapter.getFirstMessage();
                if (message == null) {
                    refreshLayout.setRefreshing(false);
                    return;
                }
                imConversation.queryMessages(message.getMessageId(), message.getTimestamp(), 20, new AVIMMessagesQueryCallback() {
                    @Override
                    public void done(List<AVIMMessage> list, AVIMException e) {
                        refreshLayout.setRefreshing(false);
                        if (filterException(e)) {
                            if (null != list && list.size() > 0) {
                                itemAdapter.addMessageList(list);
                                itemAdapter.notifyDataSetChanged();

                                layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                            }
                        }
                    }
                });


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != imConversation) {
            NotificationUtils.addTag(imConversation.getConversationId());
        }
    }

    @Override
    public void onPause() {
        super.onResume();
        NotificationUtils.removeTag(imConversation.getConversationId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }

    public void setConversation(AVIMConversation conversation, int memberId, String memberName) {
        itemAdapter.setMemberName(memberName);
        this.memberId = memberId;
        imConversation = conversation;
        refreshLayout.setEnabled(true);
        inputBottomBar.setTag(imConversation.getConversationId());
        fetchMessages();
        NotificationUtils.addTag(conversation.getConversationId());
    }


    /**
     * 拉取消息，必须加入 conversation 后才能拉取消息
     */
    private void fetchMessages() {
        showPd("正在加载聊天记录");
        imConversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (filterException(e)) {
                    dismissPd();
//          if (!list.isEmpty()) {
//            AVIMMessage lastmessage = list.get(list.size() - 1);
//            if (lastmessage instanceof AVIMTextMessage) {
//              lastContent = ((AVIMTextMessage) lastmessage).getText();
//            } else {
//              lastContent = getString(R.string.unspport_message_type);
//            }
//          }
                    itemAdapter.setMessageList(list);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();
                    scrollToBottom();
                } else {
                    dismissPd();
                }
            }
        });
    }

    /**
     * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送
     * 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
     */
    public void onEvent(final InputBottomBarTextEvent textEvent) {
        Log.i("ONEVENT", "INPUT TEXTEVENT IN");
        if (null != imConversation && null != textEvent) {
            if (!TextUtils.isEmpty(textEvent.sendContent) && imConversation.getConversationId().equals(textEvent.tag)) {
                AVIMTextMessage message = new AVIMTextMessage();
                message.setText(textEvent.sendContent);
                itemAdapter.addMessage(message);
                itemAdapter.notifyDataSetChanged();
                scrollToBottom();
                imConversation.sendMessage(message, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        Log.i("ONEVENT", "done in");
                        if (filterException(e)) {
                            itemAdapter.notifyDataSetChanged();
                            lastContent = textEvent.sendContent;
                            Message message = Message.obtain();
                            message.what = STORE_INTO_DB;
                            message.arg1 = memberId;
                            mHandler.sendMessage(message);
                        }

                    }
                });
            }
        }
    }

    private void storeConIntoDb(int memberId) {
        final Conversation previous = DbRelated.findConById(getActivity(), memberId);
        if (previous == null) {
            //达人通过通知进入或者用户第一次进入对话界面
            final Conversation conversation = new Conversation();
            conversation.setMemberId((long) memberId);
            conversation.setChatTime(new Date());
            conversation.setIsRead(true);
            if (!CommonUtils.isEmpty(lastContent)) {
                conversation.setLastContent(lastContent);
            }
            getUserInfo(memberId, new InfoLoadingListener() {
                @Override
                public void onSuccess(SimpleInfo info) {
                    conversation.setMemberName(info.getNickname());
                    conversation.setMemberAvater(info.getImage());
                    DbRelated.insertCon(getActivity(), conversation);
                    EventBus.getDefault().post(new DbChangeEvent());
                }

                @Override
                public void onFail() {
                    conversation.setMemberAvater(Constant.FAIL_AVATER);
                    conversation.setMemberName(Constant.FAIL_MEMBER_NAME);
                    DbRelated.insertCon(getActivity(), conversation);
                    EventBus.getDefault().post(new DbChangeEvent());
                }
            });
        } else {
            if (previous.getMemberAvater().equals(Constant.FAIL_AVATER)) {
                getUserInfo(memberId, new InfoLoadingListener() {
                    @Override
                    public void onSuccess(SimpleInfo info) {
                        previous.setMemberName(info.getNickname());
                        previous.setMemberAvater(info.getImage());
                        previous.setChatTime(new Date());
                        if (CommonUtils.isEmpty(lastContent)) {
                            previous.setLastContent(lastContent);
                        }
                        DbRelated.updateCon(getActivity(), previous);
                        EventBus.getDefault().post(new DbChangeEvent());
                    }

                    @Override
                    public void onFail() {
                        previous.setChatTime(new Date());
                        if (CommonUtils.isEmpty(lastContent)) {
                            previous.setLastContent(lastContent);
                        }
                        DbRelated.updateCon(getActivity(), previous);
                        EventBus.getDefault().post(new DbChangeEvent());
                    }
                });
            } else {
                previous.setChatTime(new Date());
                if (!CommonUtils.isEmpty(lastContent)) {
                    previous.setLastContent(lastContent);
                }
                DbRelated.updateCon(getActivity(), previous);
                EventBus.getDefault().post(new DbChangeEvent());
            }
        }
    }

    private void getUserInfo(int userId, final InfoLoadingListener listener) {
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity().getApplicationContext()).getUserInfoById(userId, new Callback<SimpleInfo>() {
                @Override
                public void success(SimpleInfo simpleInfo, Response response) {
                    if (simpleInfo != null) {
                        listener.onSuccess(simpleInfo);
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

    /**
     * 处理推送过来的消息
     * 同理，避免无效消息，此处加了 conversation id 判断
     */
    public void onEvent(ImTypeMessageEvent event) {
        if (null != imConversation && null != event &&
                imConversation.getConversationId().equals(event.conversation.getConversationId())) {
            itemAdapter.addMessage(event.message);
            itemAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    /**
     * 重新发送已经发送失败的消息
     */
    public void onEvent(final ImTypeMessageResendEvent event) {
        if (null != imConversation && null != event) {
            if (AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed == event.message.getMessageStatus()
                    && imConversation.getConversationId().equals(event.message.getConversationId())) {
                imConversation.sendMessage(event.message, new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        itemAdapter.notifyDataSetChanged();
//                        recordToLast(event.message);
                    }
                });
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

//    private void recordToLast(AVIMMessage message) {
//        if (message instanceof AVIMTextMessage) {
//            lastContent = ((AVIMTextMessage) message).getText();
//        } else {
//            lastContent = getString(R.string.unspport_message_type);
//        }
////        storeConIntoDb(memberId);
//    }

    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
    }

    protected void toast(String str) {
        Snackbar.make(getActivity().getWindow().getDecorView(), str, Snackbar.LENGTH_SHORT).show();
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == STORE_INTO_DB) {
            storeConIntoDb(msg.arg1);
        }
        return true;
    }

    private interface InfoLoadingListener {
        void onSuccess(SimpleInfo info);

        void onFail();
    }

}

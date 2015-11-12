package com.dreamspace.superman.UI.Activity.Main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.dreamspace.superman.Common.AVImClientManager;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Fragment.ChatFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AbsActivity {

    private ChatFragment chatFragment;
    private String memberId;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void prepareDatas() {
        String memberId = getIntent().getStringExtra(Constant.MEMBER_ID);
        this.memberId = memberId;
    }

    @Override
    protected void initViews() {
        setTitle("对话中");
        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        getConversation(memberId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (null != extras && extras.containsKey(Constant.MEMBER_ID)) {
            String memberId = extras.getString(Constant.MEMBER_ID);
            setTitle("对话中");
            getConversation(memberId);
        }
    }

    private void getConversation(final String memberId) {
        final AVIMClient client = AVImClientManager.getInstance().getClient();
        if(client==null){
            String uid= PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.UID);
            AVImClientManager.getInstance().open(uid, new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (filterException(e)) {
                        final AVIMClient client = AVImClientManager.getInstance().getClient();
                        getCon(client);
                    }
                }
            });
        }else{
            getCon(client);
        }

    }
    private void getCon(final AVIMClient client){
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.withMembers(Arrays.asList(memberId), true);
        conversationQuery.whereEqualTo("customConversationType", 1);
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (filterException(e)) {
                    //注意：此处仍有漏洞，如果获取了多个 conversation，默认取第一个
                    if (null != list && list.size() > 0) {
                        chatFragment.setConversation(list.get(0),Integer.parseInt(memberId));
                    } else {
                        HashMap<String, Object> attributes = new HashMap<String, Object>();
                        attributes.put("customConversationType", 1);
                        client.createConversation(Arrays.asList(memberId), null, attributes, false, new AVIMConversationCreatedCallback() {
                            @Override
                            public void done(AVIMConversation avimConversation, AVIMException e) {
                                chatFragment.setConversation(avimConversation,Integer.parseInt(memberId));
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


}

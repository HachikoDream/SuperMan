package com.dreamspace.superman.UI.Activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.AVImClientManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.UserInfo;
import com.umeng.update.UmengUpdateAgent;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class StartPageActivity extends Activity implements android.os.Handler.Callback {
//// TODO: 2015/11/18 达人状态改变的相关提示
    private Handler mHandler;
    private static final int check_is_login = 233;
    private static final String KEY = "IS_FIRST";
    private static final String COME_FROM_START = "start";
    private static final String SOURCE = "SOURCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        prepareDatas();
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
    }

    protected void prepareDatas() {
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(check_is_login, 1000);
    }


    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == check_is_login) {
            if (PreferenceUtils.hasKey(getApplicationContext(), KEY)) {
                //非第一次登陆
                if (!CommonUtils.isEmpty(PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.ACCOUNT))) {
                    getUserInfo();
                } else {
                    gotoMainWithInfo(MainActivity.NOT_FIRST_IN);
                }

            } else {
                //第一次打开软件时 设置已经打开过，增加初始化分类
                Log.i("INFO", "first");
                PreferenceUtils.putBoolean(getApplicationContext(), KEY, true);
                PreferenceUtils.writeClassifyIntoSpForFirst(getApplicationContext());
                gotoMainWithInfo(MainActivity.FIRST_IN);
            }
        }
        return true;
    }

    private void gotoMainWithInfo(String info) {
        Bundle b = new Bundle();
        b.putString(MainActivity.COME_SOURCE, info);
        Intent intent = new Intent(StartPageActivity.this, MainActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
    private void gotoMainWithAvaterTask(String cachedPath){
        Bundle b = new Bundle();
        b.putString(MainActivity.COME_SOURCE, MainActivity.NOT_FIRST_IN);
        b.putInt(MainActivity.AVATER_TASK,MainActivity.AVATER_LOAD_TASK);
        b.putString(MainActivity.CACHED_PHOTO_PATH,cachedPath);
        Intent intent = new Intent(StartPageActivity.this, MainActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }


    //获取用户信息
    private void getUserInfo() {
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(getApplicationContext()).getUserInfo(new Callback<UserInfo>() {
                @Override
                public void success(UserInfo userInfo, Response response) {
                    if (userInfo != null) {
                        saveUserInfo(userInfo);
                        openChatServer(userInfo.getId());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    String uid = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.UID);
                    if (!CommonUtils.isEmpty(uid)) {
                        openChatServer(uid);
                    } else {
                        gotoMainWithInfo(MainActivity.NOT_FIRST_IN);
                    }

                }
            });

        } else {
            gotoMainWithInfo(MainActivity.NOT_FIRST_IN);
        }
    }

    //打开应用后连接到leancloud服务器
    private void openChatServer(String clientId) {
        AVImClientManager.getInstance().open(clientId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                filterException(e);
                if(PreferenceUtils.hasKey(getApplicationContext(),PreferenceUtils.Key.AVATER_AVAILABLE)&&!PreferenceUtils.getBoolean(getApplicationContext(),PreferenceUtils.Key.AVATER_AVAILABLE)){
                    String cachedPath=PreferenceUtils.getString(getApplicationContext(),PreferenceUtils.Key.AVATER_CACHE_PATH);
                    gotoMainWithAvaterTask(cachedPath);
                }else{
                    gotoMainWithInfo(MainActivity.NOT_FIRST_IN);
                }

            }
        });
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            showToast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    protected void showToast(String msg) {
        if (null != msg && !CommonUtils.isEmpty(msg)) {
            Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    //保存用户信息到本地
    private void saveUserInfo(UserInfo userInfo) {
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.ACCOUNT, userInfo.getNickname());
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.AVATAR, userInfo.getImage());
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.REALNAME, userInfo.getName());
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.SEX, userInfo.getSex());
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.PHONE, userInfo.getPhone());
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.MAST_STATE, userInfo.getMast_state());
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.UID, userInfo.getId());
        if (!CommonUtils.isEmpty(userInfo.getMas_id())) {
            PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.MAS_ID, userInfo.getMas_id());
        }
    }


}

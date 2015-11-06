package com.dreamspace.superman.UI.Activity.Main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.UserInfo;
import com.umeng.update.UmengUpdateAgent;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class StartPageActivity extends AppCompatActivity implements android.os.Handler.Callback {

    private Handler mHandler;
    private static final int check_is_login = 233;
    private static final String KEY = "IS_FIRST";
    private static final String COME_FROM_START="start";
    private static final String SOURCE="SOURCE";

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
                Log.i("INFO", "not first");
                getUserInfo();

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
    private void gotoMainWithInfo(String info){
        Bundle b=new Bundle();
        b.putString(MainActivity.COME_SOURCE,info);
        Intent intent=new Intent(StartPageActivity.this,MainActivity.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
    //获取用户信息
    private void getUserInfo() {
        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).getUserInfo(new Callback<UserInfo>() {
                @Override
                public void success(UserInfo userInfo, Response response) {
                    if (userInfo != null) {
                        saveUserInfo(userInfo);
                        gotoMainWithInfo(MainActivity.NOT_FIRST_IN);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    gotoMainWithInfo(MainActivity.NOT_FIRST_IN);
                }
            });

        }else {
            gotoMainWithInfo(MainActivity.NOT_FIRST_IN);
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
        if(!CommonUtils.isEmpty(userInfo.getMas_id())){
            PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.MAS_ID,userInfo.getMas_id());
        }
    }


}

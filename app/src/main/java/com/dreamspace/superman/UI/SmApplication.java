package com.dreamspace.superman.UI;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.MessageHandler;
import com.dreamspace.superman.Common.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public class SmApplication extends Application {
    private static final String KEY = "IS_FIRST";

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, Constant.APP_ID, Constant.APP_KEY);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(this));
    }
}

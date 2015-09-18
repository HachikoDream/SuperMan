package com.dreamspace.superman.UI;

import android.app.Application;
import android.util.Log;

import com.dreamspace.superman.Common.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public class SmApplication extends Application {
    private static final String KEY="IS_FIRST";
    @Override
    public void onCreate() {
        super.onCreate();
        if(PreferenceUtils.hasKey(getApplicationContext(),KEY)){
            //非第一次登陆
            Log.i("INFO","not first");
        }else{
            //第一次打开软件时 设置已经打开过，增加初始化分类
            Log.i("INFO", "first");
            PreferenceUtils.putBoolean(getApplicationContext(), KEY, true);
            PreferenceUtils.writeClassifyIntoSpForFirst(getApplicationContext());
        }
    }
}

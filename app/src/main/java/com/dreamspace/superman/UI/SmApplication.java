package com.dreamspace.superman.UI;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.MessageHandler;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.ds.greendao.DaoMaster;
import com.ds.greendao.DaoSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public class SmApplication extends Application {
    private static final String KEY = "IS_FIRST";
    private DaoSession daoSession;
    private SQLiteDatabase db;
    public DaoMaster.DevOpenHelper helper;
    public DaoMaster daoMaster;
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, Constant.APP_ID, Constant.APP_KEY);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(this));
        setupDatabase();
    }

    private void setupDatabase() {
        helper=new DaoMaster.DevOpenHelper(this,Constant.DB_NAME,null);
        db=helper.getWritableDatabase();
        daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();
    }
    public DaoSession getDaoSession(){
        return this.daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}

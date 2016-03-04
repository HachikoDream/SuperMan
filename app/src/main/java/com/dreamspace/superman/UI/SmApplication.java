package com.dreamspace.superman.UI;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.MessageHandler;
import com.ds.greendao.DaoMaster;
import com.ds.greendao.DaoSession;
import com.umeng.analytics.MobclickAgent;

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
        MobclickAgent.setDebugMode(true);//todo check whether is in debug mode
        initPushService(this);
        AVOSCloud.initialize(this, Constant.APP_ID, Constant.APP_KEY);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(this));
        setupDatabase();
    }

    private void initPushService(final Context context) {
        AlibabaSDK.asyncInit(context, new InitResultCallback() {
            @Override
            public void onSuccess() {
                Log.i("info", "alibaba-sdk init success");
                CloudPushService pushService = AlibabaSDK.getService(CloudPushService.class);
                if (pushService != null) {
                    pushService.register(context, new CommonCallback() {
                        @Override
                        public void onSuccess() {
                            Log.i("info", "push service connect success");
                        }

                        @Override
                        public void onFailed(String errorcode, String message) {
                            Log.i("info", "push service connect failed,the errorcode is " + errorcode + ",the message is " + message);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("info", "alibaba-sdk init failed," + s);
            }
        });
    }

    private void setupDatabase() {
        helper = new DaoMaster.DevOpenHelper(this, Constant.DB_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return this.daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }


    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.dreamspace.superman.API;

import com.dreamspace.superman.Common.Constant;

import retrofit.RestAdapter;

/**
 * Created by Administrator on 2015/8/24 0024.
 */
public final class ApiManager {
    public static String BASE_URL = "http://api2.hloli.me:9777/v1.0";
    private static SupermanService mService;
    static volatile RestAdapter restAdapter = null;

    private ApiManager() {

    }

    public static RestAdapter getAdapter() {
        if (restAdapter == null) {
            synchronized (ApiManager.class) {
                if (restAdapter == null) {
                    restAdapter = new RestAdapter.Builder().setEndpoint(Constant.BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                            .build();
                }
            }
        }
        return restAdapter;
    }
    public static void initRegionApi() {
        if (mService == null) {
            synchronized (ApiManager.class) {
                if (mService == null) {
                    mService = getAdapter().create(SupermanService.class);
                }
            }
        }
    }
    public static SupermanService getService() {
        initRegionApi();
        return mService;
    }
}

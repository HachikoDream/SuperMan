package com.dreamspace.superman.API;

import android.content.Context;

import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.PreferenceUtils;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Administrator on 2015/8/24 0024.
 */
public final class ApiManager {
    //    public static String BASE_URL = "http://api2.hloli.me:9777/v1.0";
    public static String BASE_URL = "https://api.idarenhui.com/";
    private static SupermanService mService;
    static volatile RestAdapter restAdapter = null;

    private ApiManager() {
    }

    public static RestAdapter getAdapter(final Context mContext) {
        if (restAdapter == null) {
            synchronized (ApiManager.class) {
                if (restAdapter == null) {
                    RequestInterceptor requestInterceptor = new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader(PreferenceUtils.Key.ACCESS, PreferenceUtils.getString(mContext, "access_token"));
                        }
                    };
                    restAdapter = new RestAdapter.Builder().setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL).setRequestInterceptor(requestInterceptor)
                            .build();
                }
            }
        }
        return restAdapter;
    }

    public static void initRegionApi(Context mContext) {
        if (mService == null) {
            synchronized (ApiManager.class) {
                if (mService == null) {
                    mService = getAdapter(mContext).create(SupermanService.class);
                }
            }
        }
    }

    public static SupermanService getService(Context mContext) {
        initRegionApi(mContext);
        return mService;
    }

    public static void clear() {
        mService = null;
        restAdapter = null;
    }
}

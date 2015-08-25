package com.dreamspace.superman.API;

import com.dreamspace.superman.Common.Constant;

import retrofit.RestAdapter;

/**
 * Created by Administrator on 2015/8/24 0024.
 */
public final class GetService {
    public static String BASE_URL="http://api2.hloli.me:9777/v1.0";
    public static RestAdapter getRestClient(){
        RestAdapter restAdapter=new RestAdapter.Builder()
                .setEndpoint(Constant.BASE_URL)
                .build();
        return restAdapter;
    }
    public static SupermanService getService(RestAdapter restAdapter){
        return restAdapter.create(SupermanService.class);
    }
}

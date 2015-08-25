package com.dreamspace.superman.API;

import com.dreamspace.superman.model.LoginReq;
import com.dreamspace.superman.model.RegisterReq;
import com.dreamspace.superman.model.RegisterRes;
import com.dreamspace.superman.model.SendVerifyReq;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public interface SupermanService {
    @POST("/auth")
    void createAccessToken(@Body LoginReq request, Callback<String> cb);

    @POST("/auth/code")
    void sendVerifyCode(@Body SendVerifyReq req, Callback<Response> cb);

    @POST("/auth/register_token")
    void createRegisterToken(@Body RegisterReq req,Callback<RegisterRes> cb);
}

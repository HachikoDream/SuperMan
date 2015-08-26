package com.dreamspace.superman.API;

import com.dreamspace.superman.model.api.LoginReq;
import com.dreamspace.superman.model.api.LoginRes;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.RegisterReq;
import com.dreamspace.superman.model.api.RegisterRes;
import com.dreamspace.superman.model.api.RegistertokenReq;
import com.dreamspace.superman.model.api.RegistertokenRes;
import com.dreamspace.superman.model.api.SendVerifyReq;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public interface SupermanService {
    @POST("/auth")
    void createAccessToken(@Body LoginReq request, Callback<LoginRes> cb);

    @POST("/auth/code")
    void sendVerifyCode(@Body SendVerifyReq req, Callback<Response> cb);

    @POST("/auth/register_token")
    void createRegisterToken(@Body RegistertokenReq req,Callback<RegistertokenRes> cb);

    @POST("/static/token")
    void createQiNiuToken(Callback<QnRes> cb);

    @POST("/user")
    void register(@Body RegisterReq req,Callback<RegisterRes> cb);
}

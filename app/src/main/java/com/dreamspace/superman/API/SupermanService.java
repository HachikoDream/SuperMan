package com.dreamspace.superman.API;

import com.dreamspace.superman.model.UserInfo;
import com.dreamspace.superman.model.api.BalanceRes;
import com.dreamspace.superman.model.api.CollectReq;
import com.dreamspace.superman.model.api.CommentReq;
import com.dreamspace.superman.model.api.CommentRes;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.ModifyLessonReq;
import com.dreamspace.superman.model.api.ModifyReq;
import com.dreamspace.superman.model.api.OperatorReq;
import com.dreamspace.superman.model.api.OrderDetailRes;
import com.dreamspace.superman.model.api.PageReq;
import com.dreamspace.superman.model.api.PublishReq;
import com.dreamspace.superman.model.api.SearchReq;
import com.dreamspace.superman.model.api.SmIdRes;
import com.dreamspace.superman.model.api.SmInfo;
import com.dreamspace.superman.model.api.SmLessonList;
import com.dreamspace.superman.model.api.SubscribeReq;
import com.dreamspace.superman.model.api.SubscribeRes;
import com.dreamspace.superman.model.api.UpdateReq;
import com.dreamspace.superman.model.api.LoginReq;
import com.dreamspace.superman.model.api.LoginRes;
import com.dreamspace.superman.model.api.PublishRes;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.RegisterReq;
import com.dreamspace.superman.model.api.RegistertokenReq;
import com.dreamspace.superman.model.api.RegistertokenRes;
import com.dreamspace.superman.model.api.SendVerifyReq;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public interface SupermanService {
    //创建访问凭据
    @POST("/auth")
    void createAccessToken(@Body LoginReq request, Callback<LoginRes> cb);

    //发送手机验证码
    @POST("/auth/code")
    void sendVerifyCode(@Body SendVerifyReq req, Callback<Response> cb);

    //创建用户注册时的凭据
    @POST("/auth/register_token")
    void createRegisterToken(@Body RegistertokenReq req, Callback<RegistertokenRes> cb);

    //创建七牛的上传凭证
    @POST("/static/token")
    void createQiNiuToken(Callback<QnRes> cb);

    //创建用户信息
    @POST("/user")
    void register(@Body RegisterReq req, Callback<LoginRes> cb);

    //更新用户信息
    @PUT("/user")
    void updateUserInfo(@Body UpdateReq req, Callback<Response> cb);

    //修改用户密码
    @PUT("/user/password")
    void modifyPwd(@Body ModifyReq req, Callback<Response> cb);

    //获取当前用户信息
    @GET("/user")
    void getUserInfo(Callback<UserInfo> cb);

    //获取用户当前账户余额
    @GET("/user/balance")
    void getUserBalance(Callback<BalanceRes> cb);

    //用户收藏课程
    @POST("/user/collection")
    void collectLesson(@Body CollectReq req,Callback<Response> cb);
    //获取用户收藏的全部课程
    @GET("/user/collections")
    void getAllCollections(@Query("page") int req,Callback<Response> cb);
    //用户删除收藏
    @DELETE("/user/collection/{less_id}")
    void deleteCollectionById(@Path("less_id") int less_id,Callback<Response> cb);
    //管理员更新一个分类信息
    //获取分类
    //管理员可以删除这个分类
    //一般用户成为达人
//    @POST("/master")
//    void applytoSuperMan(@Body ApplyReq req,new Callback<>)
    //达人绑定支付宝账号
    //获取本账号达人信息
    @GET("/master")
    void getSmId(Callback<SmIdRes> cb);
    //获取达人信息
    @GET("/master/{mas_id}")
    void getSmDetailInfo(@Path("mas_id") String mas_id,Callback<SmInfo> cb);
    //获取达人发布的课程信息
    @GET("/master/{mas_id}/lessons")
    void getLessonsbyMid(@Path("mas_id")int mas_id,@Query("page") int page,Callback<SmLessonList> cb);
    //由达人发布
    @POST("/lesson")
    void publishLessonBySm(@Body PublishReq req,Callback<PublishRes> cb);
    //修改课程
    @PUT("/lesson/{les_id}")
    void modifyLessonInfo(@Path("les_id")String les_id,@Body ModifyLessonReq req,Callback<Response> cb);
    //用名称模糊查找课程
    @GET("/lessons")
    void searchLessons(@Query("key_word") String key_word,Callback<SmLessonList> cb);
    //获取课程详情 ???可省略
    @GET("/lesson/{les_id}")
    void getLessonDetail(@Path("les_id") String les_id,Callback<LessonInfo> cb);
    //删除课程
    @DELETE("/lesson/{les_id}")
    void deleteLesson(@Path("les_id") String les_id,Callback<Response> cb);
    //对课程评论
    @POST("/lesson/{les_id}/comment")
    void comment(@Path("les_id")String les_id,@Body CommentReq req,Callback<CommentRes> cb);

    //获取课程评论
    @GET("/lesson/{les_id}/comments")
    void getCommentsById(@Path("les_id") int les_id,@Query("page")int page);
    //用户预约订单
    @POST("/user/order")
    void subscribeOrder(@Body SubscribeReq req,Callback<SubscribeRes> cb);
     //达人确认/拒绝订单
    @PUT("/master/order/{ord_id}")
    void operateOrderBySm(@Body OperatorReq req,Callback<Response> cb);
    //达人获取定订单详情
    @GET("/master/order/{ord_id}")
    void getOrderDetail(@Path("ord_id") String ord_id,Callback<OrderDetailRes> cb);
    //达人获取订单列表
   //用户获取订单详情
    //用户获取订单列表
   //达人方取消订单
    //用户方取消订单
    //获取当前版本
    //创建反馈


}

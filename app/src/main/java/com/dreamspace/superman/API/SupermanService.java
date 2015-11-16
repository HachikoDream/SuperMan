package com.dreamspace.superman.API;

import com.dreamspace.superman.UI.Activity.Superman.OrderListActivity;
import com.dreamspace.superman.model.Order;
import com.dreamspace.superman.model.TempRes;
import com.dreamspace.superman.model.UserInfo;
import com.dreamspace.superman.model.api.ApplyInfoRes;
import com.dreamspace.superman.model.api.BalanceRes;
import com.dreamspace.superman.model.api.BindReq;
import com.dreamspace.superman.model.api.CollectReq;
import com.dreamspace.superman.model.api.CommentList;
import com.dreamspace.superman.model.api.CommentReq;
import com.dreamspace.superman.model.api.CommentRes;
import com.dreamspace.superman.model.api.EmptyBody;
import com.dreamspace.superman.model.api.FeedbackReq;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.ModifyLessonReq;
import com.dreamspace.superman.model.api.ModifyReq;
import com.dreamspace.superman.model.api.OperatorReq;
import com.dreamspace.superman.model.api.OrderDetailRes;
import com.dreamspace.superman.model.api.OrderlistRes;
import com.dreamspace.superman.model.api.PayRes;
import com.dreamspace.superman.model.api.PublishReq;
import com.dreamspace.superman.model.api.QRRes;
import com.dreamspace.superman.model.api.SimpleInfo;
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
import com.dreamspace.superman.model.api.ToBeSmReq;
import com.dreamspace.superman.model.api.payAccountRes;
import com.squareup.okhttp.Call;

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
    void createQiNiuToken(@Body EmptyBody body,Callback<QnRes> cb);

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
    @GET("/user/{id}")
    void getUserInfoById(@Path("id") int id,Callback<SimpleInfo> cb);
    //获取用户当前账户余额
    @GET("/user/balance")
    void getUserBalance(Callback<BalanceRes> cb);
    //获取指定分类下的课程
    @GET("/catalog/{cata_id}/lessons")
    void getCoursesByCatalog(@Path("cata_id") int cataId,@Query("page") int page, Callback<SmLessonList> cb);
    //用户收藏课程
    @POST("/user/collection")
    void collectLesson(@Body CollectReq req,Callback<Response> cb);
    //获取用户收藏的全部课程
    @GET("/user/collections")
    void getAllCollections(@Query("page") int page,Callback<SmLessonList> cb);
    //用户删除收藏
    @DELETE("/user/collection/{less_id}")
    void deleteCollectionById(@Path("less_id") int less_id,Callback<Response> cb);
    //一般用户成为达人
    @POST("/master")
    void applytoSuperMan(@Body ToBeSmReq req,Callback<TempRes> cb);
    //达人绑定支付宝账号
    @POST("/master/payaccount")
    void bindWithBankAccount(@Body BindReq req,Callback<Response> cb);
    //获取本账号达人信息
    @GET("/master")
    void getSmId(Callback<SmIdRes> cb);
    //获取达人信息
    @GET("/master/{mas_id}")
    void getSmDetailInfo(@Path("mas_id") String mas_id,Callback<SmInfo> cb);
    //获取达人申请信息
    @GET("/user/master_apply_info")
    void getUserApplyInfo(Callback<ApplyInfoRes> cb);
    //获取达人发布的课程信息
    @GET("/master/{mas_id}/lessons")
    void getLessonsbyMid(@Path("mas_id")String mas_id,@Query("page") int page,@Query("state") String state,Callback<SmLessonList> cb);
    //由达人发布
    @POST("/lesson")
    void publishLessonBySm(@Body PublishReq req,Callback<PublishRes> cb);
    //修改课程
    @PUT("/lesson/{les_id}")
    void modifyLessonInfo(@Path("les_id") int les_id,@Body ModifyLessonReq req,Callback<Response> cb);
    //用名称模糊查找课程
    @GET("/lessons")
    void searchLessons(@Query("key_word") String key_word,@Query("page")int page,Callback<SmLessonList> cb);
    //获取课程详情
    @GET("/lesson/{les_id}")
    void getLessonDetail(@Path("les_id") int les_id,Callback<LessonInfo> cb);
    //删除课程
    @DELETE("/lesson/{les_id}")
    void deleteLesson(@Path("les_id") int les_id,Callback<Response> cb);
    //对课程评论
    @POST("/lesson/{les_id}/comment")
    void comment(@Path("les_id")String les_id,@Body CommentReq req,Callback<CommentRes> cb);

    //获取课程评论
    @GET("/lesson/{les_id}/comments")
    void getCommentsById(@Path("les_id") int les_id,@Query("page")int page,Callback<CommentList> cb);
    //用户预约订单
    @POST("/user/order")
    void subscribeOrder(@Body SubscribeReq req,Callback<SubscribeRes> cb);
     //达人确认/拒绝订单
    @PUT("/master/order/{ord_id}")
    void operateOrderBySm(@Path("ord_id") int ord_id,@Body OperatorReq req,Callback<Response> cb);
    //达人获取定订单详情
    @GET("/master/order/{ord_id}")
    void getSmOrderDetail(@Path("ord_id") int ord_id,Callback<OrderDetailRes> cb);
    //达人获取订单列表
    @GET("/master/orders")
    void getSmOrderListByState(@Query("state") int state, @Query("page") int page, Callback<OrderlistRes> cb);
   //用户获取订单详情
    @GET("/user/order/{ord_id}")
    void getOrderDetailById(@Path("ord_id") int ord_id,Callback<OrderDetailRes> cb);

    //用户获取订单列表
    @GET("/user/orders")
    void getOrderListById(@Query("state") int state,@Query("page") int page,Callback<OrderlistRes> cb);
   //达人方取消订单
    @DELETE("/master/order/{ord_id}")
    void cancelSmOrderById(@Query("ord_id") int ord_id ,Callback<Response> cb);
    //用户方取消订单
    @DELETE("/user/order/{ord_id}")
    void cancelOrderById(@Path("ord_id") int ord_id,Callback<Response> cb);
    //创建反馈
    @POST("/feedback")
    void sendFeedbackInfo(@Body FeedbackReq req,Callback<Response> cb);
    //达人获取隐藏在二维码中的信息
    @GET("/master/order/{ord_id}/confirm")
    void getQRCodeInfo(@Path("ord_id") int ord_id,Callback<QRRes> cb);
    @POST("/user/order/confiem")
    void scanQRCodeInfo(@Body QRRes res,Callback<Response> cb);
    //向后台发起支付请求,在调用后将获得的对象解析为json格式的字符串
    @POST("/user/order/{ord_id}/pay")
    void sendPayRequest(@Path("ord_id") int ord_id,@Body PayRes res,Callback<Response> cb);
    @GET("/master/payaccount")
    void getPayAccount(Callback<payAccountRes> cb);
}

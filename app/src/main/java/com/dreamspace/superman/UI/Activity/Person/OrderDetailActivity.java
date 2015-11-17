package com.dreamspace.superman.UI.Activity.Person;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Main.ChatActivity;
import com.dreamspace.superman.UI.Activity.Main.QRReaderActivity;
import com.dreamspace.superman.event.OrderChangeEvent;
import com.dreamspace.superman.model.Order;
import com.dreamspace.superman.model.api.OrderDetailRes;
import com.dreamspace.superman.model.api.PayRes;
import com.pingplusplus.android.PaymentActivity;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class OrderDetailActivity extends AbsActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PAYMENT = 827;
    private final int TITLE = R.string.title_activity_order_detail;
    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.course_name_tv)
    TextView courseNameTv;
    @Bind(R.id.username_tv)
    TextView usernameTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.connect_sm_btn)
    Button connectSmBtn;
    @Bind(R.id.connect_sm_iv)
    ImageView connectSmIv;
    @Bind(R.id.order_phonenum_btn)
    Button orderPhonenumBtn;
    @Bind(R.id.preview_iv)
    ImageView previewIv;
    @Bind(R.id.preview_tv)
    TextView previewTv;
    @Bind(R.id.confirm_iv)
    ImageView confirmIv;
    @Bind(R.id.confirm_tv)
    TextView confirmTv;
    @Bind(R.id.pay_iv)
    ImageView payIv;
    @Bind(R.id.pay_tv)
    TextView payTv;
    @Bind(R.id.learned_iv)
    ImageView learnedIv;
    @Bind(R.id.learned_tv)
    TextView learnedTv;
    @Bind(R.id.order_number)
    TextView orderNumber;
    @Bind(R.id.order_time)
    TextView orderTime;
    @Bind(R.id.order_price)
    TextView orderPrice;
    @Bind(R.id.order_coursename)
    TextView orderCoursename;
    @Bind(R.id.order_studentname)
    TextView orderStudentname;
    @Bind(R.id.order_subphone)
    TextView orderPhonenum;
    @Bind(R.id.course_time)
    TextView courseTime;
    @Bind(R.id.course_address)
    TextView courseAddress;
    @Bind(R.id.course_remark)
    TextView courseRemark;
    @Bind(R.id.quit_sub_btn)
    Button quitSubBtn;
    @Bind(R.id.detail_info)
    CardView detailContentView;
    @Bind(R.id.status_view)
    RelativeLayout statusView;
    @Bind(R.id.price_tv)
    TextView priceTv;
    @Bind(R.id.quit_sub_btn_in_confirm)
    Button quitSubBtnInConfirm;
    @Bind(R.id.pay_btn_in_confirm)
    Button payBtnInConfirm;
    @Bind(R.id.master_confirm_layout)
    RelativeLayout masterConfirmLayout;
    @Bind(R.id.back_money_btn)
    Button backMoneyBtn;
    @Bind(R.id.confirm_btn)
    Button confirmBtn;
    @Bind(R.id.user_pay_layout)
    RelativeLayout userPayLayout;
    @Bind(R.id.comment_btn)
    Button commentBtn;
    private int order_id;
    private int order_state;
    private String common_price;
    public final static String ORDER_ID = "order_id";
    public final static String STATE = "order_state";
    public final static String COMMON_PRICE = "common_price";
    //用于向评价界面传递数据
    public final static String IMAGE = "image";
    public final static String LESS_KEEP_TIME = "less_keeptime";
    public final static String MAST_NAME = "master_name";
    public final static String LESS_NAME = "less_name";
    public final static String LESS_ID = "less_id";
    public final static int QRREADER_REQUEST_CODE = 156;
    public final static int EVALUATE_REQUEST_CODE = 157;
    private ProgressDialog pd;
    private String mast_phone;//达人联系电话，用于取消预约操作


    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void prepareDatas() {
        order_id = this.getIntent().getIntExtra(ORDER_ID, -1);
        order_state = this.getIntent().getIntExtra(STATE, -1);
        common_price = this.getIntent().getStringExtra(COMMON_PRICE);
        getSupportActionBar().setTitle(TITLE);
    }

    @Override
    protected void initViews() {
        showViewByState(order_state);
        setOnclickListenerOfAllBtn();
    }


    private void showPd(String msg) {
        if (CommonUtils.isEmpty(msg)) {
            msg = "正在提交您的请求,请稍后";
        }
        if (pd != null) {
            if (!pd.isShowing()) {
                pd.show();
            }
        } else {
            pd = ProgressDialog.show(this, "", msg, true, false);
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void showViewByState(int order_state) {
        switch (order_state) {
            case Constant.ORDER_RELATED.BACK_COST:
            case Constant.ORDER_RELATED.CANCEL:
                quitSubBtn.setVisibility(View.GONE);
                masterConfirmLayout.setVisibility(View.GONE);
                userPayLayout.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case Constant.ORDER_RELATED.SUSCRIBE:
                quitSubBtn.setVisibility(View.VISIBLE);
                masterConfirmLayout.setVisibility(View.GONE);
                userPayLayout.setVisibility(View.GONE);
                setStatusIntoView(1);
                break;
            case Constant.ORDER_RELATED.PRE_COST:
                quitSubBtn.setVisibility(View.GONE);
                masterConfirmLayout.setVisibility(View.VISIBLE);
                userPayLayout.setVisibility(View.GONE);
                setStatusIntoView(2);
                break;
            case Constant.ORDER_RELATED.PRE_MEET:
                quitSubBtn.setVisibility(View.GONE);
                masterConfirmLayout.setVisibility(View.GONE);
                userPayLayout.setVisibility(View.VISIBLE);
                setStatusIntoView(3);
                break;
            case Constant.ORDER_RELATED.FINISH:
                quitSubBtn.setVisibility(View.GONE);
                masterConfirmLayout.setVisibility(View.GONE);
                userPayLayout.setVisibility(View.GONE);
                setStatusIntoView(4);
                break;
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return detailContentView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDetailOrderInfoById(order_id);
    }

    private void getDetailOrderInfoById(final int order_id) {
        toggleShowLoading(true, null);
        if (order_id == -1) {
            toggleShowError(true, "获取订单详情失败，请稍后再试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDetailOrderInfoById(order_id);
                }
            });
        } else {
            if (NetUtils.isNetworkConnected(this)) {
                ApiManager.getService(getApplicationContext()).getOrderDetailById(order_id, new Callback<OrderDetailRes>() {
                    @Override
                    public void success(OrderDetailRes orderDetailRes, Response response) {
                        toggleShowLoading(false, null);
                        mast_phone = orderDetailRes.getMast_phone();
                        showDetailInfo(orderDetailRes);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        toggleShowError(true, getInnerErrorInfo(error), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getDetailOrderInfoById(order_id);
                            }
                        });
                    }
                });
            } else {
                toggleNetworkError(true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDetailOrderInfoById(order_id);
                    }
                });
            }

        }
    }

    private void showDetailInfo(final OrderDetailRes orderDetailRes) {
        Tools.showImageWithGlide(this, profileImage, orderDetailRes.getImage());
        courseNameTv.setText(orderDetailRes.getLess_name());
        usernameTv.setText(orderDetailRes.getMast_name());
        timeTv.setText(orderDetailRes.getLess_keeptime() + "元");
        priceTv.setText(common_price);
        orderNumber.setText(String.valueOf(orderDetailRes.getId()));
        orderTime.setText(orderDetailRes.getTime());
        orderPrice.setText(CommonUtils.getStringFromPrice(orderDetailRes.getLess_price()));
        orderCoursename.setText(orderDetailRes.getLess_name());
        orderStudentname.setText(orderDetailRes.getName());
        orderPhonenum.setText(orderDetailRes.getPhone());
        courseTime.setText(orderDetailRes.getStart_time());
        courseAddress.setText(orderDetailRes.getSite());
        courseRemark.setText(orderDetailRes.getRemark());
        if(!CommonUtils.isEmpty(orderDetailRes.getCom_id())){
            commentBtn.setVisibility(View.VISIBLE);
            //// TODO: 2015/11/15 评价事件
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b=new Bundle();
                    b.putString(IMAGE,orderDetailRes.getImage());
                    b.putString(MAST_NAME,orderDetailRes.getMast_name());
                    b.putString(LESS_KEEP_TIME,orderDetailRes.getLess_keeptime());
                    b.putString(LESS_NAME,orderDetailRes.getLess_name());
                    b.putString(COMMON_PRICE,common_price);
                    b.putInt(LESS_ID, orderDetailRes.getLess_id());
                    b.putInt(ORDER_ID,order_id);
                    readyGoForResult(EvaluateActivity.class, EVALUATE_REQUEST_CODE, b);
                }
            });
        }
        orderPhonenumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.callSb(OrderDetailActivity.this,orderDetailRes.getMast_phone());
            }
        });

    }

    private void setStatusIntoView(int step) {
        switch (step) {
            case 1:
                setSubView(true);
                setConfirmView(false);
                setPayView(false);
                setFinishView(false);
                break;
            case 2:
                setSubView(true);
                setConfirmView(true);
                setPayView(false);
                setFinishView(false);
                break;
            case 3:
                setSubView(true);
                setConfirmView(true);
                setPayView(true);
                setFinishView(false);
                break;
            case 4:
                setSubView(true);
                setConfirmView(true);
                setPayView(true);
                setFinishView(true);
                break;
        }
    }

    private void setSubView(boolean visible) {
        if (visible) {
            previewIv.setImageResource(R.drawable.page_point_h);
            previewTv.setTextColor(getResources().getColor(R.color.navi_color));
        } else {
            previewTv.setTextColor(getResources().getColor(R.color.select_tab_color));
            previewIv.setImageResource(R.drawable.page_point_n);
        }
    }

    private void setConfirmView(boolean visible) {
        if (visible) {
            confirmIv.setImageResource(R.drawable.page_point_h);
            confirmTv.setTextColor(getResources().getColor(R.color.navi_color));
        } else {
            confirmTv.setTextColor(getResources().getColor(R.color.select_tab_color));
            confirmIv.setImageResource(R.drawable.page_point_n);
        }
    }

    private void setPayView(boolean visible) {
        if (visible) {
            payIv.setImageResource(R.drawable.page_point_h);
            payTv.setTextColor(getResources().getColor(R.color.navi_color));
        } else {
            payTv.setTextColor(getResources().getColor(R.color.select_tab_color));
            payIv.setImageResource(R.drawable.page_point_n);
        }
    }

    private void setFinishView(boolean visible) {
        if (visible) {
            learnedIv.setImageResource(R.drawable.page_point_h);
            learnedTv.setTextColor(getResources().getColor(R.color.navi_color));
        } else {
            learnedTv.setTextColor(getResources().getColor(R.color.select_tab_color));
            learnedIv.setImageResource(R.drawable.page_point_n);
        }
    }

    private void setOnclickListenerOfAllBtn() {
        //state:1
        quitSubBtn.setOnClickListener(this);
        // 2
        quitSubBtnInConfirm.setOnClickListener(this);
        payBtnInConfirm.setOnClickListener(this);
        //3
        confirmBtn.setOnClickListener(this);
        backMoneyBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit_sub_btn:
                cancelSubOrderBeforeConfirm();
                break;
            case R.id.quit_sub_btn_in_confirm:
                cancelSubOrderAfterConfirm();
                break;
            case R.id.pay_btn_in_confirm:
                payOrder();
                break;
            case R.id.confirm_btn:
                confirmForPay();
                break;
            case R.id.back_money_btn:
                refund();
                break;
        }
    }

    private void showAlertDialog(String msg, String positiveMsg, String negativeMsg, final OnFinish finish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish != null) {
                            finish.finish(true);
                        }
                    }
                });
        if (!CommonUtils.isEmpty(negativeMsg)) {
            builder.setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (finish != null)
                        finish.finish(false);
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*
      在达人已经确认以后取消课程,需要电话联系达人,这里提供达人的电话号码和客服的电话
     */
    private void cancelSubOrderAfterConfirm() {
        showAlertDialog("由于之前达人已经有确认操作，因此您需要联系达人来取消订单.", "联系达人", "联系客服", new OnFinish() {
            @Override
            public void finish(boolean isOk) {
                if (isOk) {
                    if (!CommonUtils.isEmpty(mast_phone)) {
                        Tools.callSb(OrderDetailActivity.this, mast_phone);
                    } else {
                        showAlertDialog("暂无达人联系方式", "联系客服", null, new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                                Tools.callSb(OrderDetailActivity.this, Constant.self_phone);
                            }
                        });
                    }
                } else {
                    //联系客服
                    Tools.callSb(OrderDetailActivity.this, Constant.self_phone);
                }
            }
        });
    }

    /*
      退款
     */
    private void refund() {
        showAlertDialog("您需要联系达人来退款.", "联系达人", "联系客服", new OnFinish() {
            @Override
            public void finish(boolean isOk) {
                if (isOk) {
                    if (!CommonUtils.isEmpty(mast_phone)) {
                        Tools.callSb(OrderDetailActivity.this, mast_phone);
                    } else {
                        showAlertDialog("暂无达人联系方式", "联系客服", null, new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                                Tools.callSb(OrderDetailActivity.this, Constant.self_phone);
                            }
                        });
                    }
                } else {
                    //联系客服
                    Tools.callSb(OrderDetailActivity.this, Constant.self_phone);
                }
            }
        });


    }

    /*
     确认见面
     */
    private void confirmForPay() {
        Bundle b = new Bundle();
        b.putInt(QRReaderActivity.ORD_ID, order_id);
        readyGoForResult(QRReaderActivity.class, QRREADER_REQUEST_CODE, b);
    }

    /*
      付款
     */
    //// TODO: 2015/11/7  注意混淆
    private void payOrder() {
        if (NetUtils.isNetworkConnected(this)) {
            showPd(null);
            PayRes res = new PayRes();
            res.setInfo("info");//这里的body不起作用，防止后台报错
            ApiManager.getService(getApplicationContext()).sendPayRequest(order_id, res, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dismissPd();
                    if (response != null) {
                        String charge = new String(((TypedByteArray) response.getBody()).getBytes());
                        gotoPayView(charge);
                    } else {
                        showAlertDialog("暂时无法完成您的请求，请稍后再试", "确定", null, null);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showAlertDialog(getInnerErrorInfo(error), "确定", null, null);
                }
            });
        } else {
            showAlertDialog("请检查您的网络连接", "确定", null, null);
        }
    }

    /**
     * 调用ping++
     */
    private void gotoPayView(String charge) {
        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }
    private void killSelf(){
        finish();
    }

    /*
       在达人确认之前取消预约
     */
    private void cancelSubOrderBeforeConfirm() {
        if (order_id != -1) {
            showPd(null);
            if (NetUtils.isNetworkConnected(this)) {
                ApiManager.getService(getApplicationContext()).cancelOrderById(order_id, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        if (response != null) {
                            dismissPd();
                            showAlertDialog("您已经取消了本次课程的预订.", "确定", null, new OnFinish() {
                                @Override
                                public void finish(boolean isOk) {
                                    EventBus.getDefault().post(new OrderChangeEvent());
                                    OrderDetailActivity.this.finish();
                                }
                            });
                        } else {
                            dismissPd();
                            showToast("暂时无法提交您的请求");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dismissPd();
                        showNetWorkError();
                    }
                });
            } else {
                showNetWorkError();
            }
        } else {
            showToast("暂时无法提交您的请求");
        }
    }

    interface OnFinish {
        void finish(boolean isOk);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             */
                if (result.equals("success")){
                    showAlertDialog("支付成功!", "确定", null, new OnFinish() {
                        @Override
                        public void finish(boolean isOk) {
                            setResult(RESULT_OK);
                            EventBus.getDefault().post(new OrderChangeEvent());
                            killSelf();
                        }
                    });
                }else if(result.equals("invalid")){
                    showAlertDialog("您尚未安装相关组件","确定",null,null);
                }else if (result.equals("fail")){
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                    showToast(result);
                    showAlertDialog(errorMsg + extraMsg, "确定", null, null);
                }

            }
        }
        //扫码页面返回处理
        else if(requestCode==QRREADER_REQUEST_CODE){
            if(resultCode==Activity.RESULT_OK){
                setResult(RESULT_OK);
                EventBus.getDefault().post(new OrderChangeEvent());
                finish();
            }
        }
        //评价页面返回处理
        else if(requestCode==EVALUATE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                EventBus.getDefault().post(new OrderChangeEvent());
                finish();
            }
        }
    }
}

package com.dreamspace.superman.UI.Activity.Person;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.dreamspace.superman.model.api.OrderDetailRes;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderDetailActivity extends AbsActivity implements View.OnClickListener {
    //// TODO: 2015/10/27 测试
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
    @Bind(R.id.order_phonenum)
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

    private int order_id;
    private int order_state;
    private String common_price;
    public final static String ORDER_ID = "order_id";
    public final static String STATE = "order_state";
    public final static String COMMON_PRICE = "common_price";
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

    //todo 评价按钮
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

    private void showDetailInfo(OrderDetailRes orderDetailRes) {
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

    }

    private void setStatusIntoView(int step) {
        switch (step) {
            case 1:
                setSubView(true);
                setConfirmView(false);
                setFinishView(false);
                setPayView(false);
                break;
            case 2:
                setSubView(true);
                setConfirmView(true);
                setFinishView(false);
                setPayView(false);
                break;
            case 3:
                setSubView(true);
                setConfirmView(true);
                setFinishView(true);
                setPayView(false);
                break;
            case 4:
                setSubView(true);
                setConfirmView(true);
                setFinishView(true);
                setPayView(true);
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
                        finish.finish(true);
                    }
                });
        if (!CommonUtils.isEmpty(negativeMsg)) {
            builder.setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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
                        callSb(mast_phone);
                    } else {
                        showAlertDialog("暂无达人联系方式", "联系客服", null, new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                               callSb(Constant.self_phone);
                            }
                        });
                    }
                } else {
                    //联系客服
                    callSb(Constant.self_phone);
                }
            }
        });
    }
    private void callSb(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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
                        callSb(mast_phone);
                    } else {
                        showAlertDialog("暂无达人联系方式", "联系客服", null, new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                                callSb(Constant.self_phone);
                            }
                        });
                    }
                } else {
                    //联系客服
                    callSb(Constant.self_phone);
                }
            }
        });


    }

    /*
     确认见面
     */
    private void confirmForPay() {

    }

    /*
      付款
     */
    private void payOrder() {

    }

    /*
       在达人确认之前取消预约
       todo 测试存在问题
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
}

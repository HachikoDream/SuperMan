package com.dreamspace.superman.UI.Activity.Superman;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.dreamspace.superman.UI.Activity.Main.QRCodeShowActivity;
import com.dreamspace.superman.UI.Activity.Main.QRReaderActivity;
import com.dreamspace.superman.model.api.OperatorReq;
import com.dreamspace.superman.model.api.OrderDetailRes;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SmOrderDetailActivity extends AbsActivity implements View.OnClickListener {

//// TODO: 2015/11/17 刷新状态列表
    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.course_name_tv)
    TextView courseNameTv;
    @Bind(R.id.username_tv)
    TextView usernameTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.price_tv)
    TextView priceTv;
    @Bind(R.id.connect_student_btn)
    Button connectStudentBtn;
    @Bind(R.id.order_phonenum)
    Button orderPhonenum;
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
    @Bind(R.id.status_view)
    RelativeLayout statusView;
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
    @Bind(R.id.order_phonenum_tv)
    TextView orderPhonenumTv;
    @Bind(R.id.course_time)
    TextView courseTime;
    @Bind(R.id.course_address)
    TextView courseAddress;
    @Bind(R.id.course_remark)
    TextView courseRemark;
    @Bind(R.id.cancel_action)
    Button cancelAction;
    @Bind(R.id.back_money_btn)
    Button backMoneyBtn;
    @Bind(R.id.finish_btn)
    Button finishBtn;
    @Bind(R.id.premeet_layout)
    RelativeLayout premeetLayout;
    @Bind(R.id.agree_btn)
    Button agreeBtn;
    @Bind(R.id.refuse_btn)
    Button refuseBtn;
    @Bind(R.id.sub_layout)
    RelativeLayout subLayout;
    @Bind(R.id.detail_info)
    CardView detailContentView;
    private int order_id;
    private int order_state;
    private String common_price;
    public final static String ORDER_ID = "order_id";
    public final static String STATE = "order_state";
    public final static String COMMON_PRICE = "common_price";
    public final static int REFUSE_REQUEST_CODE = 385;

    private ProgressDialog pd;
    private String student_phone;//学员联系电话

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_sm_order_detail_actvity);
    }

    @Override
    protected void prepareDatas() {
        order_id = this.getIntent().getIntExtra(ORDER_ID, -1);
        order_state = this.getIntent().getIntExtra(STATE, -1);
        common_price = this.getIntent().getStringExtra(COMMON_PRICE);

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
                ApiManager.getService(getApplicationContext()).getSmOrderDetail(order_id, new Callback<OrderDetailRes>() {
                    @Override
                    public void success(OrderDetailRes orderDetailRes, Response response) {
                        toggleShowLoading(false, null);
                        student_phone = orderDetailRes.getPhone();
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
        connectStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int memberId =orderDetailRes.getUser_id();
                Bundle b = new Bundle();
                b.putString(Constant.MEMBER_ID, String.valueOf(memberId));
                readyGo(ChatActivity.class, b);
            }
        });


    }

    @Override
    protected void initViews() {
        showViewByState(order_state);
        setOnclickListenerOBottomLayout();
        orderPhonenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (student_phone != null) {
                    Tools.callSb(SmOrderDetailActivity.this, student_phone);
                } else {
                    showAlertDialog("暂未获取到学员联系方式,请刷新重试", "确定", null, null);
                }
            }
        });

    }

    private void setOnclickListenerOBottomLayout() {
        //state:1
        agreeBtn.setOnClickListener(this);
        refuseBtn.setOnClickListener(this);
        // 2
        cancelAction.setOnClickListener(this);
        //3
        backMoneyBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.agree_btn:
                agreeOrder();
                break;
            case R.id.refuse_btn:
                showAlertDialog("确定要拒绝该订单么?", "确定", "取消", new OnFinish() {
                    @Override
                    public void finish(boolean isOk) {
                        if (isOk) {
                            refuseOrder();
                        }
                    }
                });
                break;
            case R.id.cancel_action:
                showAlertDialog("真的要取消订单么？", "确定", "点错了", new OnFinish() {
                    @Override
                    public void finish(boolean isOk) {
                        if (isOk) {
                            cancelOrderInPrePay();
                        }
                    }
                });
                break;
            case R.id.back_money_btn:
                showAlertDialog("真的要取消订单并退款给学员吗？", "确认退款", "先等等", new OnFinish() {
                    @Override
                    public void finish(boolean isOk) {
                        if (isOk) {
                            backMoneyToStudent();
                        }
                    }
                });
                break;
            case R.id.finish_btn:
                finishMeeting();
                break;
        }
    }

    /**
     * 在待见面环节完成见面
     */
    private void finishMeeting() {
        Bundle b = new Bundle();
        b.putInt(QRCodeShowActivity.ORD_ID, order_id);
        readyGo(QRCodeShowActivity.class, b);
    }

    private void killAct() {
        finish();
    }

    /**
     * 在待见面环节退款(调用达人删除订单的接口，退款操作由后台完成)
     */
    private void backMoneyToStudent() {
        cancelOrder("您已将该订单对应金额退款给学员，您可以在“取消/退款”栏目中查看该订单详情");
    }

    private void cancelOrder(final String msg) {
        if (NetUtils.isNetworkConnected(this)) {
            showPd(null);
            ApiManager.getService(this).cancelSmOrderById(order_id, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if (response != null) {
                        showAlertDialog(msg, "确定", null, new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                                setResult(RESULT_OK);
                                killAct();
                            }
                        });
                    } else {
                        dismissPd();
                        showAlertDialog("暂时不能提交您的请求", "确定", null, null);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showAlertDialog(getInnerErrorInfo(error), "确定", null, null);
                }
            });
        } else {
            showNetWorkErrorDialog();
        }

    }

    private void showNetWorkErrorDialog() {
        showAlertDialog("暂无网络连接，请稍后再试", "确定", null, null);
    }

    /**
     * 在待付款阶段达人取消订单
     */
    private void cancelOrderInPrePay() {
        cancelOrder("您已取消本次订单，学员会收到相应的通知");
    }

    /**
     * 达人拒绝订单
     */
    private void refuseOrder() {
        Bundle b = new Bundle();
        b.putInt(ORDER_ID, order_id);
        readyGoForResult(RefuseActivity.class, REFUSE_REQUEST_CODE, b);
    }

    /**
     * 达人同意订单
     */
    private void agreeOrder() {
        if (NetUtils.isNetworkConnected(this)) {
            showPd(null);
            OperatorReq req = new OperatorReq();
            req.setOpeator(Constant.ORDER_RELATED.SM_OPERATION.CONFIRM);
            ApiManager.getService(getApplicationContext()).operateOrderBySm(order_id, req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dismissPd();
                    showAlertDialog("操作成功,需等待学员付款，您现在可以在待付款中查看该订单的进展", "我知道啦", null, new OnFinish() {
                        @Override
                        public void finish(boolean isOk) {
                            setResult(RESULT_OK);
                            killAct();
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showAlertDialog(getInnerErrorInfo(error), "确定", null, null);
                }
            });
        } else {
            showNetWorkErrorDialog();
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
                    if (finish != null) {
                        finish.finish(true);
                    }
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showViewByState(int order_state) {
        switch (order_state) {
            case Constant.ORDER_RELATED.BACK_COST:
            case Constant.ORDER_RELATED.CANCEL:
                premeetLayout.setVisibility(View.GONE);
                subLayout.setVisibility(View.GONE);
                cancelAction.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case Constant.ORDER_RELATED.SUSCRIBE:
                subLayout.setVisibility(View.VISIBLE);
                premeetLayout.setVisibility(View.GONE);
                cancelAction.setVisibility(View.GONE);
                setStatusIntoView(1);
                break;
            case Constant.ORDER_RELATED.PRE_COST:
                subLayout.setVisibility(View.GONE);
                cancelAction.setVisibility(View.VISIBLE);
                premeetLayout.setVisibility(View.GONE);
                setStatusIntoView(2);
                break;
            case Constant.ORDER_RELATED.PRE_MEET:
                subLayout.setVisibility(View.GONE);
                cancelAction.setVisibility(View.GONE);
                premeetLayout.setVisibility(View.VISIBLE);
                setStatusIntoView(3);
                break;
            case Constant.ORDER_RELATED.FINISH:
                subLayout.setVisibility(View.GONE);
                cancelAction.setVisibility(View.GONE);
                premeetLayout.setVisibility(View.GONE);
                setStatusIntoView(4);
                break;
        }

    }

    @Override
    protected View getLoadingTargetView() {
        return detailContentView;
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
                setPayView(true);
                setFinishView(false);
                break;
            case 4:
                setSubView(true);
                setConfirmView(true);
                setFinishView(true);
                setPayView(true);
                break;
        }
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

    interface OnFinish {
        void finish(boolean isOk);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFUSE_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}

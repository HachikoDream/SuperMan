package com.dreamspace.superman.UI.Activity.Person;

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

public class OrderDetailActivity extends AbsActivity {
    //// TODO: 2015/10/26   增加onclick事件
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
    }

 //todo 评价按钮
    private void showViewByState(int order_state) {
        switch (order_state) {
            case Constant.ORDER_CLASSIFY.BACK_COST:
            case Constant.ORDER_CLASSIFY.CANCEL:
                quitSubBtn.setVisibility(View.GONE);
                masterConfirmLayout.setVisibility(View.GONE);
                userPayLayout.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case Constant.ORDER_CLASSIFY.SUSCRIBE:
                quitSubBtn.setVisibility(View.VISIBLE);
                masterConfirmLayout.setVisibility(View.GONE);
                userPayLayout.setVisibility(View.GONE);
                setStatusIntoView(1);
                break;
            case Constant.ORDER_CLASSIFY.PRE_COST:
                quitSubBtn.setVisibility(View.GONE);
                masterConfirmLayout.setVisibility(View.VISIBLE);
                userPayLayout.setVisibility(View.GONE);
                setStatusIntoView(2);
                break;
            case Constant.ORDER_CLASSIFY.PRE_MEET:
                quitSubBtn.setVisibility(View.GONE);
                masterConfirmLayout.setVisibility(View.GONE);
                userPayLayout.setVisibility(View.VISIBLE);
                setStatusIntoView(3);
                break;
            case Constant.ORDER_CLASSIFY.FINISH:
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
}

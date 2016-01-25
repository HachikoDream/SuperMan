package com.dreamspace.superman.UI.Activity.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Person.OrderDetailActivity;
import com.dreamspace.superman.event.OrderChangeEvent;
import com.dreamspace.superman.model.api.PayRes;
import com.pingplusplus.android.PaymentActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class PayChannelActivity extends AbsActivity {

    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.course_name_tv)
    TextView courseNameTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.course_price)
    TextView coursePrice;
    @Bind(R.id.check_zfb)
    ImageView checkZfb;
    @Bind(R.id.zfb_layout)
    RelativeLayout zfbLayout;
    @Bind(R.id.check_wechat)
    ImageView checkWechat;
    @Bind(R.id.wechat_layout)
    RelativeLayout wechatLayout;
    @Bind(R.id.pay_rightnow)
    Button payRightnow;
    private ProgressDialog pd;
    private static final int REQUEST_CODE_PAYMENT = 827;
    private int order_id = -1;
    private String channel;
    private final static String ALI_PAY = "alipay";
    private final static String WX_PAY = "wx";
    private String course_price;
    private String image;
    private String keeptime;
    private String mast_name;
    private String less_name;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_pay_channel);
    }


    @Override
    protected void prepareDatas() {
        order_id = this.getIntent().getIntExtra(OrderDetailActivity.ORDER_ID, -1);
        course_price = this.getIntent().getStringExtra(OrderDetailActivity.COMMON_PRICE);
        image = this.getIntent().getStringExtra(OrderDetailActivity.IMAGE);
        keeptime = this.getIntent().getStringExtra(OrderDetailActivity.LESS_KEEP_TIME);
        mast_name = this.getIntent().getStringExtra(OrderDetailActivity.MAST_NAME);
        less_name = this.getIntent().getStringExtra(OrderDetailActivity.LESS_NAME);

    }

    @Override
    protected void initViews() {
        showViewbyData();
        zfbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isEmpty(channel) || channel.equals(WX_PAY)) {
                    select(checkZfb);
                } else {
                    reset();
                }
            }
        });
        wechatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isEmpty(channel) || channel.equals(ALI_PAY)) {
                    select(checkWechat);
                } else {
                    reset();
                }
            }
        });
        payRightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_id != -1) {
                    if (CommonUtils.isEmpty(channel)) {
                        showToast("请先选择一个支付方式");
                    } else {
                        payOrder();
                    }
                } else {
                    showToast("暂时无法完成支付请求,请稍后再试");
                }

            }
        });
    }

    private void reset() {
        channel = null;
        checkWechat.setImageResource(R.drawable.icon_comment_n);
        checkZfb.setImageResource(R.drawable.icon_comment_n);
    }

    private void select(ImageView imageView) {
        reset();
        imageView.setImageResource(R.drawable.icon_comment_h);
        if (imageView.getId() == R.id.check_wechat) {
            channel = WX_PAY;
        } else {
            channel = ALI_PAY;
        }

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    /*
     付款
    */
    //// TODO: 2015/11/7  注意混淆
    private void payOrder() {
        if (NetUtils.isNetworkConnected(this)) {
            showPd(null);
            PayRes res = new PayRes();
            res.setChannel(channel);
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

    private void showViewbyData() {
        Tools.showImageWithGlide(this, profileImage, image);
        courseNameTv.setText(less_name);
        timeTv.setText(keeptime);
        coursePrice.setText(course_price);

    }

    interface OnFinish {
        void finish(boolean isOk);

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

    private void killSelf() {
        finish();
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
                if (result.equals("success")) {
                    showAlertDialog("支付成功!", "确定", null, new OnFinish() {
                        @Override
                        public void finish(boolean isOk) {
                            setResult(RESULT_OK);
                            killSelf();
                        }
                    });
                } else if (result.equals("invalid")) {
                    showAlertDialog("您尚未安装相关组件", "确定", null, null);
                } else if (result.equals("fail")) {
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                    showToast(result);
                    showAlertDialog(errorMsg + extraMsg, "确定", null, null);
                }

            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}

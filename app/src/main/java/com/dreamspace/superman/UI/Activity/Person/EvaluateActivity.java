package com.dreamspace.superman.UI.Activity.Person;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.CommentReq;
import com.dreamspace.superman.model.api.CommentRes;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EvaluateActivity extends AbsActivity {

    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.course_name_tv)
    TextView courseNameTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.course_price)
    TextView coursePrice;
    @Bind(R.id.evaluate_content)
    EditText evaluateContent;
    @Bind(R.id.is_name_cb)
    CheckBox isNameCb;
    @Bind(R.id.submit_btn)
    Button submitBtn;
    @Bind(R.id.course_price_right)
    TextView coursePriceRight;
    @Bind(R.id.price_flag)
    TextView priceFlag;

    private int less_id;
    private int order_id;
    private String course_price;
    private String image;
    private String keeptime;
    private String mast_name;
    private String less_name;
    private String evaluate;
    private boolean isAnonimity;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_evaluate);
    }

    @Override
    protected void prepareDatas() {
        less_id = this.getIntent().getIntExtra(OrderDetailActivity.LESS_ID, -1);
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
    }

    private boolean isValid() {
        isAnonimity = isNameCb.isChecked();
        evaluate = evaluateContent.getText().toString();
        if (CommonUtils.isEmpty(evaluate)) {
            showToast("请先输入您的评价内容");
            return false;
        }
        int left = 15 - evaluate.length();
        if (left > 0) {
            showToast("您还差" + left + "字才到15个字哦");
            return false;
        }
        return true;
    }

    private void showViewbyData() {
        coursePriceRight.setVisibility(View.GONE);
        priceFlag.setVisibility(View.GONE);
        Tools.showImageWithGlide(this, profileImage, image);
        courseNameTv.setText(less_name);
        timeTv.setText(keeptime);
        coursePrice.setText(course_price);
        if (order_id != -1 && less_id != -1) {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValid()) {
                        submitEvaluateToServer();
                    }
                }
            });
        } else {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast("暂时无法提交您的请求，请稍后再试");
                }
            });
        }
    }

    private void killSelf() {
        finish();
    }

    private void submitEvaluateToServer() {
        if (NetUtils.isNetworkConnected(this)) {
            CommentReq req = new CommentReq();
            req.setAnonymous(isAnonimity);
            req.setContent(evaluate);
            req.setOrder_id(order_id);
            ApiManager.getService(getApplicationContext()).comment(less_id, req, new Callback<CommentRes>() {
                @Override
                public void success(CommentRes commentRes, Response response) {
                    if (commentRes != null) {
                        showAlertDialog("评价成功，在课程详情中可以看到您的评论。", "确定", null, new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                                setResult(RESULT_OK);
                                killSelf();
                            }
                        });
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });
        } else {
            showNetWorkError();
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

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    interface OnFinish {
        void finish(boolean isOk);

    }

}

package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.FeedbackReq;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeedbackActivity extends AbsActivity {

    @Bind(R.id.feedback_content)
    EditText feedbackContent;
    @Bind(R.id.submit_action)
    Button submitAction;
    private String phone;//用户的电话
    private String content;//用户意见反馈的内容
    private ProgressDialog pd;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.content_feedback);
    }

    @Override
    protected void prepareDatas() {
        phone = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.PHONE);
    }

    @Override
    protected void initViews() {
        submitAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                     sendFeedBackInfo();
                }
            }
        });
    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在提交您的反馈信息，请稍后", true, false);
        } else {
            pd.show();
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
 //TODO 重新测试
    private void sendFeedBackInfo() {
        if (NetUtils.isNetworkConnected(this)) {
            showPd();
            FeedbackReq req = new FeedbackReq();
            req.setContent(content);
            req.setPhone(phone);
            ApiManager.getService(getApplicationContext()).sendFeedbackInfo(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dismissPd();
                    if (response != null) {
                        AlertDialog dialog=new AlertDialog.Builder(FeedbackActivity.this)
                                .setTitle("提示")
                                .setMessage("已经收到您的意见反馈，我们会马上处理。")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .show();
                        dialog.setCanceledOnTouchOutside(false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showInnerError(error);
                }
            });
        } else {
            showNetWorkError();
        }
    }

    private boolean isValid() {
        content = feedbackContent.getText().toString();
        if (CommonUtils.isEmpty(content)) {
            showToast("请先输入您的反馈信息");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

}

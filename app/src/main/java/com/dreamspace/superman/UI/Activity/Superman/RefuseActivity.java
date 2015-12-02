package com.dreamspace.superman.UI.Activity.Superman;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.OperatorReq;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RefuseActivity extends AbsActivity {

    @Bind(R.id.refuse_content)
    EditText refuseContent;
    @Bind(R.id.cancel_action)
    Button cancelAction;
    @Bind(R.id.content_number_hint)
    TextView numberTv;
    private String reason;
    private int order_id;
    private ProgressDialog pd;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.content_refuse);
    }

    @Override
    protected void prepareDatas() {
        order_id = this.getIntent().getIntExtra(SmOrderDetailActivity.ORDER_ID, -1);

    }

    @Override
    protected void initViews() {
        refuseContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = s.length();
                StringBuilder sb = new StringBuilder();
                numberTv.setText(sb.append("已写").append(number).append("字").toString());
            }
        });
        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    refuseOrder(reason);
                }
            }
        });
    }

    private void showPd(String msg) {
        if (msg == null) {
            msg = "正在提交请求,请稍后";
        }
        if (pd == null) {
            pd = ProgressDialog.show(this, "", msg, true, false);
        } else {
            pd.show();
        }
    }

    private void dismissShow() {
        if (pd != null && !pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    public boolean isValid() {
        if (CommonUtils.isEmpty(refuseContent.getText().toString())) {
            showToast("请先输入您的拒绝理由");
            return false;
        } else if (refuseContent.getText().toString().length() < 15) {
            showToast("请至少填写15个字");
            return false;
        } else {
            reason = refuseContent.getText().toString();
            return true;
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

    private void refuseOrder(String reason) {
        if (NetUtils.isNetworkConnected(this)) {
            showPd(null);
            OperatorReq req = new OperatorReq();
            req.setOpeator(Constant.ORDER_RELATED.SM_OPERATION.REFUSE);
            req.setReason(reason);
            ApiManager.getService(getApplicationContext()).operateOrderBySm(order_id, req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dismissShow();
                    if (response != null) {
                        showAlertDialog("您已拒绝该请求,我们会将拒绝理由反馈给达人.", "知道了", null, new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                                if (isOk) {
                                    setResult(RESULT_OK);
                                    killAct();
                                }
                            }
                        });
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissShow();
                    showInnerError(error);
                }
            });
        } else {
            showNetWorkError();
        }
    }

    private void killAct() {
        finish();
    }
}

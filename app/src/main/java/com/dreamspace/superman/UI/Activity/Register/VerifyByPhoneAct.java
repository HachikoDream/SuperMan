package com.dreamspace.superman.UI.Activity.Register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.ModifyReq;
import com.dreamspace.superman.model.api.RegistertokenReq;
import com.dreamspace.superman.model.api.RegistertokenRes;
import com.dreamspace.superman.model.api.SendVerifyReq;
import com.dreamspace.superman.model.api.UpdateReq;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

   /*
   used to register when :
   1.login with thirdplat
   2.modify phoneNumber
   3.the first step in modify password
  the situation will be adjusted by the integer value passed by the comming intent
  the value stored in the Constant.java
   */

public class VerifyByPhoneAct extends AbsActivity implements Handler.Callback {
    @Bind(R.id.phonenum_ev)
    EditText phoneNumEt;
    @Bind(R.id.verification_ed)
    EditText verifyEt;
    @Bind(R.id.send_vercode_btn)
    Button sendBtn;
    @Bind(R.id.mybtn)
    Button mainBtn;
    private final static int TITLE = R.string.title_activity_register_by_phone;
    private String phoneNum;
    private String code;
    private Handler mHandler;
    private int Timer = 60;
    private static final int BEGIN_TIMER = 233;
    private String text = "发送验证码";
    private int source;
    private ProgressDialog pd;
    private final static String MODIFY_PHONE_TXT = "修改手机";
    private final static String MODIFY_PWD_TXT = "修改密码";
    private final static String BAND_PHONE_TXT = "绑定手机";
    private boolean special = false;//用于跳过phone的一般性验证,用于修改密码部分

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_register_by_phone);
    }

    @Override
    protected void prepareDatas() {
        mHandler = new Handler(this);
        source = this.getIntent().getIntExtra(Constant.COME_SOURCE.SOURCE, -1);
        setViewBySource(source);
    }

    private void setViewBySource(int source) {
        setMyBtnText(source);
        if (source == Constant.COME_SOURCE.MODIFY_PWD) {
            phoneNum=PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.PHONE);
            phoneNumEt.setText(fuzzyString(phoneNum));
            phoneNumEt.setEnabled(false);
            special = true;
            sendVerifyCode();
        }
    }

    private String fuzzyString(String phone) {
        StringBuilder sb = new StringBuilder();
        sb.append(phone.substring(0, 3));
        sb.append("******");
        sb.append(phone.substring(phone.length() - 2, phone.length()));
        return sb.toString();
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(TITLE));
    }

    @OnClick(R.id.send_vercode_btn)
    void sendVerifyCode() {
        if (isPhoneValid()) {
            if (NetUtils.isNetworkConnected(this)) {
                SendVerifyReq req = new SendVerifyReq();
                req.setPhone(phoneNum);
                ApiManager.getService(this.getApplicationContext()).sendVerifyCode(req, new Callback<Response>() {
                    @Override
                    public void success(Response o, Response response) {
                        sendBtn.setEnabled(false);
                        mHandler.sendEmptyMessage(BEGIN_TIMER);
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
    }

    //set the text of the main btn
    private void setMyBtnText(int source) {
        switch (source) {
            case Constant.COME_SOURCE.MODIFY_PHOME:
                mainBtn.setText(MODIFY_PHONE_TXT);
                break;
            case Constant.COME_SOURCE.MODIFY_PWD:
                mainBtn.setText(MODIFY_PWD_TXT);
                break;
            case Constant.COME_SOURCE.THIRD_PLAT:
                mainBtn.setText(BAND_PHONE_TXT);
                break;
            case -1:
                Log.i("INFO", "UN KNOWN COMING SOURCE");
                break;
        }

    }

    @OnClick(R.id.mybtn)
    void Register() {
        switch (source) {
            case Constant.COME_SOURCE.MODIFY_PHOME:
                modifyPhone();
                break;
            case Constant.COME_SOURCE.MODIFY_PWD:
                modifyPwd();
                break;
            case Constant.COME_SOURCE.THIRD_PLAT:
                break;
            case -1:
                Log.i("INFO", "UN KNOWN COMING SOURCE");
                break;
        }

    }

    private void modifyPwd() {
        showDialog();
        if (isCodeValid()) {
            if (NetUtils.isNetworkConnected(this)) {
                RegistertokenReq req = new RegistertokenReq();
                req.setCode(code);
                req.setPhone(PreferenceUtils.getString(getApplicationContext(),PreferenceUtils.Key.PHONE));
                ApiManager.getService(this.getApplicationContext()).createRegisterToken(req, new Callback<RegistertokenRes>() {
                    @Override
                    public void success(RegistertokenRes registertokenRes, Response response) {
                        dismissDialog();
                        if (registertokenRes != null) {
                            final String rt=registertokenRes.getRegister_token();
                            showDialogWithEt("请输入6个以上字符作为新密码", "修改密码", new DialogButtonClicked() {
                                @Override
                                public void onPositiveClicked(String content, DialogInterface dialog) {
                                    ModifyReq mreq=new ModifyReq();
                                    mreq.setPassword(content);
                                    mreq.setRegister_token(rt);
                                    dialog.dismiss();
                                    doModify(mreq);
                                }

                                @Override
                                public void onNegativeClicked(DialogInterface dialog) {
                                       dialog.dismiss();
                                }
                            });
                        }else {
                            showToast(response.getReason());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dismissDialog();
                        showInnerError(error);
                    }
                });
            } else {
                dismissDialog();
                showNetWorkError();
            }

        }
    }

    private void doModify(ModifyReq mreq) {
        showDialog();
        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).modifyPwd(mreq, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dismissDialog();
                    if(response!=null){
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        showToast(response.getReason());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissDialog();
                    showInnerError(error);
                }
            });

        }else{
              showNetWorkError();
        }
     }


    /*
        content:EditText init content
        title: dialog's title

     */
    private void showDialogWithEt(final String content, String title, final DialogButtonClicked btnListener) {
        final EditText mEdit = new EditText(VerifyByPhoneAct.this);
        mEdit.setHint(content);
        mEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog builder = new AlertDialog.Builder(VerifyByPhoneAct.this)
                .setTitle(title)
                .setView(mEdit)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newcontent = mEdit.getText().toString();
                        if (newcontent.equals(content)) {
                            dialog.dismiss();
                        } else if (CommonUtils.isEmpty(newcontent)) {
                            showToast("请输入密码");
                        } else if (newcontent.length() < 6) {
                            showToast("长度不足6位");
                            mEdit.selectAll();
                        } else {
                            btnListener.onPositiveClicked(newcontent, dialog);
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnListener.onNegativeClicked(dialog);
                    }
                })
                .show();

    }

    private void showDialog() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在提交数据", true, false);
        } else {
            pd.show();
        }
    }

    private void dismissDialog() {
        if (pd == null) {
            return;
        }
        pd.dismiss();
    }


    private void modifyPhone() {
        if (isPhoneValid() && isCodeValid()) {
            showDialog();
            UpdateReq req = new UpdateReq();
            req.setPhone(phoneNum);
            req.setCode(code);
            if (NetUtils.isNetworkConnected(this)) {
                ApiManager.getService(this.getApplicationContext()).updateUserInfo(req, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        if (response != null) {
                            dismissDialog();
                            saveInLocal();
                            showAlertDialog();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dismissDialog();
                        showInnerError(error);
                    }
                });

            } else {
                dismissDialog();
                showNetWorkError();
            }
        }

    }

    private void saveInLocal() {
        PreferenceUtils.putString(this.getApplicationContext(), PreferenceUtils.Key.PHONE, phoneNum);
    }

    private void showAlertDialog() {
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("手机号修改成功，之后您可以用修改后的手机号进行登陆等相关操作，该账号与原手机不再有任何关联。")
                .setNeutralButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("phoneNum", phoneNum);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).show();
    }

    private boolean isPhoneValid() {
        if (special) {
            return true;
        }
        phoneNum = phoneNumEt.getText().toString();
        if (CommonUtils.isEmpty(phoneNum)) {
            showToast("请先输入您的手机号");
            return false;
        }
        if (phoneNum.length() != 11) {
            showToast("请检查您的手机号是否正确");
            return false;
        }
        return true;
    }

    private boolean isCodeValid() {
        code = verifyEt.getText().toString();
        if (CommonUtils.isEmpty(code)) {
            showToast("请先输入您手机收到的验证码");
            return false;
        }
        return true;
    }


    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == BEGIN_TIMER) {
            if (Timer == 0) {
                if (sendBtn != null) {
                    sendBtn.setText(text);
                    sendBtn.setEnabled(true);
                    Timer = 60;
                }
            } else {
                if (sendBtn != null) {
                    sendBtn.setText(Timer + "秒");
                    Timer--;
                    mHandler.sendEmptyMessageDelayed(BEGIN_TIMER, 1000);
                }
            }
        }
        return true;
    }

    private interface DialogButtonClicked {
        void onPositiveClicked(String content, DialogInterface dialog);

        void onNegativeClicked(DialogInterface dialog);
    }
}

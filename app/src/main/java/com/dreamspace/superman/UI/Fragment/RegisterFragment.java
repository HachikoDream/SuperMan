package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.API.SupermanService;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Register.RegisterInfoActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.model.api.ErrorRes;
import com.dreamspace.superman.model.api.RegistertokenReq;
import com.dreamspace.superman.model.api.RegistertokenRes;
import com.dreamspace.superman.model.api.SendVerifyReq;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements Handler.Callback {

    @Bind(R.id.username_ev)
    EditText phoneNumEt;
    @Bind(R.id.verification_ed)
    EditText verifyEt;
    @Bind(R.id.pwd_ed)
    EditText pwdEt;
    @Bind(R.id.send_vercode_btn)
    Button sendVerifyBtn;
    @Bind(R.id.mybtn)
    Button registerBtn;
    private String phoneNum;
    private Handler mHandler;
    private SupermanService mService;
    private static final int BEGIN_TIMER = 233;
    private int Timer = 60;
    private String text = "发送验证码";
    private String code;
    private String pwd;
    private String register_token;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void initViews(View view) {
    }

    @OnClick(R.id.send_vercode_btn)
    void sendVerifyCode() {
        if (isPhoneValid()) {
            if (NetUtils.isNetworkConnected(getActivity())) {
                SendVerifyReq req = new SendVerifyReq();
                req.setPhone(phoneNum);
                mService.sendVerifyCode(req, new Callback<Response>() {
                    @Override
                    public void success(Response o, Response response) {
                        sendVerifyBtn.setEnabled(false);
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

    @OnClick(R.id.mybtn)
    void Register() {
        if (isRegisterValid()) {
            final RegistertokenReq req = new RegistertokenReq();
            req.setPhone(phoneNum);
            req.setPassword(pwd);
            req.setCode(code);
            mService.createRegisterToken(req, new Callback<RegistertokenRes>() {
                @Override
                public void success(RegistertokenRes s, Response response) {
                    if(response.getStatus()==200){
                        register_token=s.getRegister_token();
                        Bundle b=new Bundle();
                        b.putString("token",register_token);
                        readyGo(RegisterInfoActivity.class, b);
                        killSelf();
                    }else{
                        showToast(response.getReason());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    ErrorRes res = (ErrorRes) error.getBodyAs(ErrorRes.class);
                    Log.i("INFO", error.getMessage());
                    Log.i("INFO", res.toString());
                }
            });
        }
    }
    private void killSelf(){
        mHandler.removeMessages(BEGIN_TIMER);
        getActivity().finish();
    }


    private boolean isPhoneValid() {
        phoneNum = phoneNumEt.getText().toString();
        if (phoneNum.isEmpty()) {
            showToast("请先输入您的手机号");
            return false;
        }
        return true;
    }

    private boolean isRegisterValid() {
        code = verifyEt.getText().toString();
        pwd = pwdEt.getText().toString();
        phoneNum = phoneNumEt.getText().toString();
        if (phoneNum.isEmpty()) {
            showToast("请先输入您的手机号");
            phoneNumEt.requestFocus();
            return false;
        }
        if (phoneNum.length() != 11) {
            showToast("请检查您的手机号是否正确");
            phoneNumEt.requestFocus();
            return false;
        }
        if (code.isEmpty()) {
            showToast("请先输入您输入的验证码");
            verifyEt.requestFocus();
            return false;
        }
        if (pwd.isEmpty()) {
            showToast("请先输入密码");
            pwdEt.requestFocus();
            return false;
        }
        if (pwd.length() <= 6) {
            showToast("请输入不少于6个长度的密码");
            pwdEt.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void initDatas() {
        mHandler = new Handler(this);
        mService = ApiManager.getService();
    }


    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == BEGIN_TIMER) {
            if (Timer == 0) {
                if(sendVerifyBtn!=null){
                    sendVerifyBtn.setText(text);
                    sendVerifyBtn.setEnabled(true);
                    Timer = 60;
                }
            } else {
                if(sendVerifyBtn!=null){
                    sendVerifyBtn.setText(Timer + "秒");
                    Timer--;
                    mHandler.sendEmptyMessageDelayed(BEGIN_TIMER, 1000);
                }
            }
        }
        return false;
    }
}

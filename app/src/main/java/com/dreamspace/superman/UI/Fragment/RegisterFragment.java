package com.dreamspace.superman.UI.Fragment;


import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.GetService;
import com.dreamspace.superman.API.SupermanService;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.model.ErrorRes;
import com.dreamspace.superman.model.RegisterReq;
import com.dreamspace.superman.model.RegisterRes;
import com.dreamspace.superman.model.SendVerifyReq;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;

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
            SendVerifyReq req = new SendVerifyReq();
            req.setPhone(phoneNum);
            mService.sendVerifyCode(req, new Callback<Response>() {
                @Override
                public void success(Response o, Response response) {
                    sendVerifyBtn.setEnabled(false);
                    mHandler.sendEmptyMessageDelayed(BEGIN_TIMER, 1000);
                }

                @Override
                public void failure(RetrofitError error) {
                    showError();
                }
            });
        }
    }

    @OnClick(R.id.mybtn)
    void Register() {
        if (isRegisterValid()) {
            final RegisterReq req = new RegisterReq();
            req.setPhone(phoneNum);
            req.setPassword(pwd);
            req.setCode(code);
            mService.createRegisterToken(req, new Callback<RegisterRes>() {
                @Override
                public void success(RegisterRes s, Response response) {
                    Log.i("INFO", response.getReason());
                    Log.i("INFO", response.getStatus() + "");
//                    ErrorRes res= (ErrorRes) getBodyAs(response.getBody(),new TypeToken<ErrorRes>(){
//
//                    }.getType());
//                    Log.i("INFO",res.toString());

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
    private Object getBodyAs(TypedInput input,Type type){
        GsonConverter converter=new GsonConverter(new Gson());
        try {
            return converter.fromBody(input,type);
        } catch (ConversionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showError() {
        showMsgBySnapbar("请检查您的网络环境");
    }

    private boolean isPhoneValid() {
        phoneNum = phoneNumEt.getText().toString();
        if (phoneNum.isEmpty()) {
            showMsgBySnapbar("请先输入您的手机号");
            return false;
        }
        return true;
    }

    private boolean isRegisterValid() {
        code = verifyEt.getText().toString();
        pwd = pwdEt.getText().toString();
        phoneNum=phoneNumEt.getText().toString();
        if(phoneNum.isEmpty()){
            showMsgBySnapbar("请先输入您的手机号");
            phoneNumEt.requestFocus();
            return false;
        }
        if(phoneNum.length()!=11){
            showMsgBySnapbar("请检查您的手机号是否正确");
            phoneNumEt.requestFocus();
            return false;
        }
        if (code.isEmpty()) {
            showMsgBySnapbar("请先输入您输入的验证码");
            verifyEt.requestFocus();
            return false;
        }
        if (pwd.isEmpty()) {
            showMsgBySnapbar("请先输入密码");
            pwdEt.requestFocus();
            return false;
        }
        if (pwd.length() <= 6) {
            showMsgBySnapbar("请输入不少于6个长度的密码");
            pwdEt.requestFocus();
            return false;
        }
        return true;
    }

    private void showMsgBySnapbar(String msg) {
        Snackbar.make(phoneNumEt, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void initDatas() {
        mHandler = new Handler(this);
        mService = GetService.getService(GetService.getRestClient());
    }


    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == BEGIN_TIMER) {
            if (Timer == 0) {
                sendVerifyBtn.setEnabled(true);
                sendVerifyBtn.setText(text);
                Timer = 60;
            } else {
                sendVerifyBtn.setText(Timer + "秒");
                Timer--;
                mHandler.sendEmptyMessageDelayed(BEGIN_TIMER, 1000);
            }
        }
        return false;
    }
}

package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.API.SupermanService;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.model.api.ErrorRes;
import com.dreamspace.superman.model.api.LoginReq;
import com.dreamspace.superman.model.api.LoginRes;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {
    @Bind(R.id.mybtn)
    Button mButton;
    @Bind(R.id.username_ev)
    EditText phoneEt;
    @Bind(R.id.pwd_ed)
    EditText pwdEt;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initViews(View view) {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum=phoneEt.getText().toString();
                String pwd=pwdEt.getText().toString();
                if(isValid(phoneNum,pwd)){
                    LoginReq req=new LoginReq();
                    req.setPassword(pwd);
                    req.setPhone(phoneNum);
                    login(req);
                }
            }
        });
    }
    private void login(LoginReq req){
        if(NetUtils.isNetworkConnected(getActivity())){
            ApiManager.getService().createAccessToken(req, new Callback<LoginRes>() {
                @Override
                public void success(LoginRes loginRes, Response response) {
                    PreferenceUtils.putString(LoginFragment.this.getActivity().getApplicationContext(),"access_token",loginRes.getAccess_token());
                }

                @Override
                public void failure(RetrofitError error) {
                   showInnerError(error);
                }
            });

        }else{
            showNetWorkError();
        }

    }
    private boolean isValid(String phoneNum,String pwd){
        if(CommonUtils.isEmpty(phoneNum)){
            showToast("请输入您的手机号码");
            phoneEt.requestFocus();
            return false;
        }
        if(phoneNum.length()!=11){
            showToast("请检查您的输入格式");
            phoneEt.requestFocus();
            return false;
        }
        if(CommonUtils.isEmpty(pwd)){
            showToast("请输入您的密码");
            return false;
        }
        if(pwd.length()<6){
            showToast("密码长度不正确");
            return false;
        }
        return true;
    }

    @Override
    public void initDatas() {

    }


}

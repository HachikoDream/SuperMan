package com.dreamspace.superman.UI.Fragment;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.MainActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.model.UserInfo;
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
    @Bind(R.id.phonenum_ev)
    EditText phoneEt;
    @Bind(R.id.pwd_ed)
    EditText pwdEt;
    private ProgressDialog pd;

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
    //登陆操作
    private void login(LoginReq req){
        pd=ProgressDialog.show(getActivity(),"","正在登陆",true,false);
        if(NetUtils.isNetworkConnected(getActivity())){
            ApiManager.getService(getActivity().getApplicationContext()).createAccessToken(req, new Callback<LoginRes>() {
                @Override
                public void success(LoginRes loginRes, Response response) {
                    PreferenceUtils.putString(LoginFragment.this.getActivity().getApplicationContext(), PreferenceUtils.Key.ACCESS,loginRes.getAccess_token());
                    ApiManager.clear();
                    getUserInfo();
                }

                @Override
                public void failure(RetrofitError error) {
                    pd.dismiss();
                    showInnerError(error);
                }
            });

        }else{
            pd.dismiss();
            showNetWorkError();
        }

    }
    //获取用户信息
    private void getUserInfo(){
       ApiManager.getService(getActivity().getApplicationContext()).getUserInfo(new Callback<UserInfo>() {
           @Override
           public void success(UserInfo userInfo, Response response) {
               if (userInfo != null) {
                   Log.i("INFO", userInfo.toString());
                   saveUserInfo(userInfo);
                   pd.dismiss();
                   readyGo(MainActivity.class);
                   getActivity().finish();
               }
           }

           @Override
           public void failure(RetrofitError error) {
               pd.dismiss();
               showInnerError(error);
           }
       });
    }
    //保存用户信息到本地
    private void saveUserInfo(UserInfo userInfo) {
      PreferenceUtils.putString(getActivity().getApplicationContext(),PreferenceUtils.Key.ACCOUNT,userInfo.getNickname());
      PreferenceUtils.putString(getActivity().getApplicationContext(),PreferenceUtils.Key.AVATAR,userInfo.getImage());
      PreferenceUtils.putString(getActivity().getApplicationContext(),PreferenceUtils.Key.REALNAME,userInfo.getName());
      PreferenceUtils.putString(getActivity().getApplicationContext(),PreferenceUtils.Key.SEX,userInfo.getSex());
        PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.PHONE,phoneEt.getText().toString());
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

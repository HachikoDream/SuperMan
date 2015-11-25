package com.dreamspace.superman.UI.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.AVImClientManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.MainActivity;
import com.dreamspace.superman.UI.Activity.Register.VerifyByPhoneAct;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.event.AccountChangeEvent;
import com.dreamspace.superman.model.UserInfo;
import com.dreamspace.superman.model.api.LoginReq;
import com.dreamspace.superman.model.api.LoginRes;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
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
    @Bind(R.id.forget_pwd_tv)
    TextView forgetPwdTv;
    private ProgressDialog pd;
    private final static int REQUEST_MODIFY_PWD=23;


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
                String phoneNum = phoneEt.getText().toString();
                String pwd = pwdEt.getText().toString();
                if (isValid(phoneNum, pwd)) {
                    LoginReq req = new LoginReq();
                    req.setPassword(pwd);
                    req.setPhone(phoneNum);
                    login(req);
                }
            }
        });
    }

    //登陆操作
    private void login(LoginReq req) {
        showPd();
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity().getApplicationContext()).createAccessToken(req, new Callback<LoginRes>() {
                @Override
                public void success(LoginRes loginRes, Response response) {
                    PreferenceUtils.putString(LoginFragment.this.getActivity().getApplicationContext(), PreferenceUtils.Key.ACCESS, loginRes.getAccess_token());
                    ApiManager.clear();
                    getUserInfo();
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showInnerError(error);
                }
            });

        } else {
            dismissPd();
            showNetWorkError();
        }

    }
    private void showPd(){
        if(pd==null){
            pd=ProgressDialog.show(getActivity(),"","正在加载中",true,false);
        }else{
            pd.show();
        }
    }
    private void dismissPd(){
        if (pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

    //获取用户信息
    private void getUserInfo() {
        ApiManager.getService(getActivity().getApplicationContext()).getUserInfo(new Callback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo, Response response) {
                if (userInfo != null) {
                    Log.i("INFO", userInfo.toString());
                    saveUserInfo(userInfo);
                    openChatService(userInfo.getId());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissPd();
                showInnerError(error);
            }
        });
    }
    //使用leancloud打开聊天服务
    private void openChatService(String userId){
        AVImClientManager.getInstance().open(userId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (filterException(e)) {
                    dismissPd();
                    EventBus.getDefault().post(new AccountChangeEvent());
                    getActivity().finish();
                } else {
                    showToast("聊天功能暂时不可用");
                    dismissPd();
                    EventBus.getDefault().post(new AccountChangeEvent());
                    getActivity().finish();
                }

            }
        });
    }
    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            showToast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }
    //保存用户信息到本地
    private void saveUserInfo(UserInfo userInfo) {
        PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.ACCOUNT, userInfo.getNickname());
        PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.AVATAR, userInfo.getImage());
        PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.REALNAME, userInfo.getName());
        PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.SEX, userInfo.getSex());
        PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.PHONE, phoneEt.getText().toString());
        PreferenceUtils.putString(getActivity().getApplicationContext(),PreferenceUtils.Key.MAST_STATE,userInfo.getMast_state());
        PreferenceUtils.putString(getActivity().getApplicationContext(),PreferenceUtils.Key.UID,userInfo.getId());
        if(!CommonUtils.isEmpty(userInfo.getMas_id())){
            PreferenceUtils.putString(getActivity().getApplicationContext(),PreferenceUtils.Key.MAS_ID,userInfo.getMas_id());
        }
    }

    private boolean isValid(String phoneNum, String pwd) {
        if (CommonUtils.isEmpty(phoneNum)) {
            showToast("请输入您的手机号码");
            phoneEt.requestFocus();
            return false;
        }
        if (phoneNum.length() != 11) {
            showToast("请检查您的输入格式");
            phoneEt.requestFocus();
            return false;
        }
        if (CommonUtils.isEmpty(pwd)) {
            showToast("请输入您的密码");
            return false;
        }
        if (pwd.length() < 6) {
            showToast("密码长度不正确");
            return false;
        }
        return true;
    }

    @Override
    public void initDatas() {
       forgetPwdTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle b=new Bundle();
               b.putInt(Constant.COME_SOURCE.SOURCE,Constant.COME_SOURCE.MODIFY_PWD);
//               b.putInt(Constant.COME_SOURCE.SOURCE,-1);
               readyGoForResult(VerifyByPhoneAct.class,REQUEST_MODIFY_PWD,b);
           }
       });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_MODIFY_PWD&&resultCode==getActivity().RESULT_OK){
            AlertDialog dialog=new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("密码修改成功,请使用新密码来登录")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }
    }
}

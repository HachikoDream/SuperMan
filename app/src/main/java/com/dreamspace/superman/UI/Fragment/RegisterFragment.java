package com.dreamspace.superman.UI.Fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.API.SupermanService;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Register.LoginActivity;
import com.dreamspace.superman.UI.Activity.Register.RegisterInfoActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.model.api.ErrorRes;
import com.dreamspace.superman.model.api.ModifyReq;
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

    @Bind(R.id.phonenum_ev)
    EditText phoneNumEt;
    @Bind(R.id.verification_ed)
    EditText verifyEt;
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
    private String register_token;
    private ProgressDialog pd;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(getActivity(), "", "正在提交注册请求", true, false);
        } else {
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void initViews(View view) {
        sendVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerifyCode();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });
    }


    public void sendVerifyCode() {
        if (isPhoneValid()) {
            sendVerifyBtn.setText("提交请求中");
            sendVerifyBtn.setEnabled(false);
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
                        sendVerifyBtn.setEnabled(true);
                        sendVerifyBtn.setText("发送验证码");
                        showInnerError(error);
                    }
                });
            } else {
                sendVerifyBtn.setEnabled(true);
                sendVerifyBtn.setText("发送验证码");
                showNetWorkError();
            }

        }
    }

    private void Register() {
        if (isRegisterValid()) {
            showPd();
            final RegistertokenReq req = new RegistertokenReq();
            req.setPhone(phoneNum);
            req.setCode(code);
            if (NetUtils.isNetworkConnected(getActivity())) {
                mService.createRegisterToken(req, new Callback<RegistertokenRes>() {
                    @Override
                    public void success(RegistertokenRes s, Response response) {
                        if (response.getStatus() == 200) {
                            dismissPd();
                            register_token = s.getRegister_token();
                            if (!s.isRegistered()) {
                                Bundle b = new Bundle();
                                b.putString("token", register_token);
                                b.putString("phoneNum", phoneNum);
                                readyGo(RegisterInfoActivity.class, b);
                                killSelf();
                            } else {
                                showInfoWithDialog("检测到该手机号已被注册，是否现在修改密码？修改完成后您可以用新密码来进行登录操作。", true, new DialogButtonClicked() {
                                    @Override
                                    public void onPositiveClicked(String content, DialogInterface dialog) {
                                        showDialogWithEt("请输入6个以上字符作为新密码", "修改密码", new DialogButtonClicked() {
                                            @Override
                                            public void onPositiveClicked(String content, DialogInterface dialog) {
                                                ModifyReq mreq = new ModifyReq();
                                                mreq.setPassword(content);
                                                mreq.setRegister_token(register_token);
                                                doModify(mreq);
                                            }

                                            @Override
                                            public void onNegativeClicked(DialogInterface dialog) {
                                            }
                                        });

                                    }

                                    @Override
                                    public void onNegativeClicked(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            dismissPd();
                            showToast(response.getReason());
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
    }

    private void killSelf() {
        mHandler.removeMessages(BEGIN_TIMER);
        getActivity().finish();
    }


    private boolean isPhoneValid() {
        phoneNum = phoneNumEt.getText().toString();
        if (phoneNum.isEmpty()) {
            showToast("请先输入您的手机号");
            return false;
        } else if (phoneNum.length() != 11) {
            showToast("您的手机号码格式有误");
            return false;
        }
        return true;
    }

    private boolean isRegisterValid() {
        code = verifyEt.getText().toString();
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
        return true;
    }

    /*
    content:EditText init content
    title: dialog's title

 */
    private void showDialogWithEt(final String content, String title, final DialogButtonClicked btnListener) {
        final EditText mEdit = new EditText(getActivity());
        mEdit.setHint(content);
        mEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog builder = new AlertDialog.Builder(getActivity())
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

    private void doModify(ModifyReq mreq) {
        showPd();
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity().getApplicationContext()).modifyPwd(mreq, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dismissPd();
                    if (response != null) {
                        showInfoWithDialog("密码修改成功，您现在可以用新密码来登录您的账号", false, new DialogButtonClicked() {
                            @Override
                            public void onPositiveClicked(String content, DialogInterface dialog) {
                                LoginActivity parent = (LoginActivity) getActivity();
                                parent.swipeToLogin(phoneNum);
                            }

                            @Override
                            public void onNegativeClicked(DialogInterface dialog) {

                            }
                        });

                    } else {
                        showInfoWithDialog(response.getReason(), false, null);
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

    private void showInfoWithDialog(String info, boolean showNBtn, final DialogButtonClicked listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage(info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onPositiveClicked(null, dialog);
                        }

                    }
                });
        if (showNBtn) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onNegativeClicked(dialog);
                    }
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    @Override
    public void initDatas() {
        mHandler = new Handler(this);
        mService = ApiManager.getService(getActivity().getApplicationContext());
    }


    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == BEGIN_TIMER) {
            if (Timer == 0) {
                if (sendVerifyBtn != null) {
                    sendVerifyBtn.setText(text);
                    sendVerifyBtn.setEnabled(true);
                    Timer = 60;
                }
            } else {
                if (sendVerifyBtn != null) {
                    sendVerifyBtn.setText(Timer + "秒");
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

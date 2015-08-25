package com.dreamspace.superman.UI.Activity.Register;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import butterknife.Bind;
import butterknife.OnClick;
// used to register when login with thirdplat
public class RegisterByPhoneAct extends AbsActivity implements Handler.Callback {
    @Bind(R.id.username_ev)
    EditText usernameEt;
    @Bind(R.id.verification_ed)
    EditText verifyEt;
    @Bind(R.id.send_vercode_btn)
    Button sendBtn;
    @Bind(R.id.mybtn)
    Button registerBtn;
    private Toolbar mToolbar;
    private final static int TITLE = R.string.title_activity_register_by_phone;
    private String phoneNum;
    private String code;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_register_by_phone);
    }

    @Override
    protected void prepareDatas() {
        mHandler = new Handler(this);
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(TITLE));
//      sendBtn.setOnClickListener(new on);
    }

    @OnClick(R.id.send_vercode_btn)
    void sendVerCode() {
        phoneNum = usernameEt.getText().toString();
        showWarningsIfInvalid();
    }

    private boolean showWarningsIfInvalid() {
        if (phoneNum.isEmpty()) {
            Snackbar.make(usernameEt, "请先输入您的手机号", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (phoneNum.length() != 11) {
            Snackbar.make(usernameEt, "请检查您的手机号是否正确", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}

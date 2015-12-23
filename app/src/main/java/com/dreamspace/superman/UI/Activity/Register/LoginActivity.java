package com.dreamspace.superman.UI.Activity.Register;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.LoginFragment;
import com.dreamspace.superman.UI.Fragment.RegisterFragment;
import com.dreamspace.superman.UI.Activity.AbsActivity;

public class LoginActivity extends AbsActivity {

    Fragment loginFragment = new LoginFragment();
    Fragment registerFragment = new RegisterFragment();
    private RadioGroup mRadioGroup;
    private RadioButton loginButton;
    private RadioButton registerButton;
    private FragmentManager mFragmentManager;
    private final static int TITLE = R.string.title_activity_login;


    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(TITLE));
        mFragmentManager = getSupportFragmentManager();
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        loginButton = (RadioButton) findViewById(R.id.login_btn);
        registerButton = (RadioButton) findViewById(R.id.rgt_btn);
        addAll();
        onRadioClickListener();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioClickListener();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioClickListener();
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    private void onRadioClickListener() {
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.login_btn:
                getSupportActionBar().setTitle(getString(R.string.title_activity_login));
                mFragmentManager.beginTransaction().hide(registerFragment).show(loginFragment).commit();
//                mFragmentManager.beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
                break;
            case R.id.rgt_btn:
                getSupportActionBar().setTitle(getString(R.string.title_fragment_register));
                mFragmentManager.beginTransaction().hide(loginFragment).show(registerFragment).commit();
//                mFragmentManager.beginTransaction().replace(R.id.fragment_container, registerFragment).commit();
                break;
        }
    }

    private void addAll() {
        mFragmentManager.beginTransaction().add(R.id.fragment_container, loginFragment).add(R.id.fragment_container, registerFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void swipeToLogin(String phoneNum) {
        loginButton.performClick();
        if (phoneNum != null && loginFragment != null) {
            ((LoginFragment) loginFragment).fillPhoneEditText(phoneNum);
        }
    }
}

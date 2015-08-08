package com.dreamspace.superman.UI.Activity.Login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
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

    Fragment loginFragment =new LoginFragment();
    Fragment registerFragment=new RegisterFragment();
    private RadioGroup mRadioGroup;
    private RadioButton loginButton;
    private RadioButton registerButton;
    private FragmentManager mFragmentManager;
    private final static int TITLE=R.string.title_activity_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void initDatas() {

    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(TITLE));
        mFragmentManager=getSupportFragmentManager();
        mRadioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        loginButton=(RadioButton)findViewById(R.id.login_btn);
        registerButton=(RadioButton)findViewById(R.id.rgt_btn);
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


    private void onRadioClickListener(){
    switch (mRadioGroup.getCheckedRadioButtonId()){
        case R.id.login_btn:
            getSupportActionBar().setTitle(getString(R.string.title_activity_login));
            mFragmentManager.beginTransaction().replace(R.id.fragment_container,loginFragment).commit();
            break;
        case R.id.rgt_btn:
            getSupportActionBar().setTitle(getString(R.string.title_fragment_register));
            mFragmentManager.beginTransaction().replace(R.id.fragment_container,registerFragment).commit();
            break;
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

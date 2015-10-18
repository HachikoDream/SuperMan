package com.dreamspace.superman.UI.Activity.Superman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

public class MyAccountActivity extends AbsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_my_account);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

}

package com.dreamspace.superman.UI.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dreamspace.superman.R;

/**
 * Created by Administrator on 2015/7/25 0025.
 */
public abstract  class AbsActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSelfContentView();
        initToolBar();
        initDatas();
        initViews();
    }

    protected abstract void setSelfContentView();

    protected abstract  void initDatas() ;

    protected abstract void initViews();
    private void initToolBar(){
        mToolBar=(Toolbar)findViewById(R.id.tl_custom);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}

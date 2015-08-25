package com.dreamspace.superman.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamspace.superman.R;

import butterknife.ButterKnife;

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
        prepareDatas();
        initViews();
    }

    protected abstract void setSelfContentView();

    protected abstract  void prepareDatas() ;

    protected abstract void initViews();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar(){
        mToolBar=(Toolbar)findViewById(R.id.tl_custom);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }




}

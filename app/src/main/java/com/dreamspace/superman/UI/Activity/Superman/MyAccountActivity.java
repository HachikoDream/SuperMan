package com.dreamspace.superman.UI.Activity.Superman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import butterknife.Bind;

public class MyAccountActivity extends AbsActivity {

    @Bind(R.id.bind_layout)
    LinearLayout bindLayout;
    private static final int REQUEST_CODE=256;

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
      bindLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              readyGoForResult(BindWithBankActivity.class,REQUEST_CODE);
          }
      });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK){
            //todo 获得绑定的信息 刷新界面
        }
    }
}

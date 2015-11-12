package com.dreamspace.superman.UI.Activity.Superman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.payAccountRes;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyAccountActivity extends AbsActivity {

    @Bind(R.id.bind_layout)
    LinearLayout bindLayout;
    @Bind(R.id.bind_tv)
    TextView bindTv;
    private static final int REQUEST_CODE=256;


    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_my_account);
    }

    @Override
    protected void prepareDatas() {
         getAccountinfo();
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
    private void getAccountinfo(){
       if(NetUtils.isNetworkConnected(this)){
           ApiManager.getService(getApplicationContext()).getPayAccount(new Callback<payAccountRes>() {
               @Override
               public void success(payAccountRes payAccountRes, Response response) {
                   if(payAccountRes!=null){
                       bindTv.setText("已绑定:"+payAccountRes.getPayaccount());
                   }
               }

               @Override
               public void failure(RetrofitError error) {
                    bindTv.setText(getInnerErrorInfo(error));
               }
           });
       }else{
           showNetWorkError();
           bindTv.setText("暂无相关信息");
       }
    }
    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK){
            getAccountinfo();
        }
    }
}

package com.dreamspace.superman.UI.Activity.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.PlatPhoneRes;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AboutusActivity extends AbsActivity {

    @Bind(R.id.service_phone_tv)
    TextView phoneTv;
    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_aboutus);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).getPlatformPhone(new Callback<PlatPhoneRes>() {
                @Override
                public void success(PlatPhoneRes platPhoneRes, Response response) {
                    phoneTv.setText(platPhoneRes.getPhone());
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                    phoneTv.setText("暂无信息");
                }
            });

        }else{
            phoneTv.setText("暂无信息");
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }
}

package com.dreamspace.superman.UI.Activity.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.PlatPhoneRes;
import com.umeng.analytics.MobclickAgent;

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
                public void success(final PlatPhoneRes platPhoneRes, Response response) {
                    phoneTv.setText(platPhoneRes.getPhone());
                    phoneTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Tools.callSb(AboutusActivity.this,platPhoneRes.getPhone());
                        }
                    });
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
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}

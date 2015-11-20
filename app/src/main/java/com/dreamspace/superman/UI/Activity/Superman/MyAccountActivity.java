package com.dreamspace.superman.UI.Activity.Superman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.payAccountRes;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyAccountActivity extends AbsActivity {

    @Bind(R.id.bind_layout)
    LinearLayout bindLayout;
    @Bind(R.id.bind_tv)
    TextView bindTv;
    @Bind(R.id.radiogroup)
    RadioGroup radioGroup;
    private static final int REQUEST_CODE = 256;
    @Bind(R.id.not_in_account_rbtn)
    RadioButton notInAccountRbtn;
    @Bind(R.id.already_in_account_rbtn)
    RadioButton alreadyInAccountRbtn;
    @Bind(R.id.account_show_tv)
    TextView accountShowTv;
    private String arrived;
    private String not_arrived;


    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_my_account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAccountinfo();
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        bindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoForResult(BindWithBankActivity.class, REQUEST_CODE);
            }
        });
        notInAccountRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioClickEvent();
            }
        });
        alreadyInAccountRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioClickEvent();
            }
        });
    }
    private void RadioClickEvent(){
        if (radioGroup.getCheckedRadioButtonId() == R.id.not_in_account_rbtn&&!CommonUtils.isEmpty(not_arrived)) {
            accountShowTv.setText("￥"+not_arrived);
        }else if(radioGroup.getCheckedRadioButtonId()==R.id.already_in_account_rbtn&&!CommonUtils.isEmpty(arrived)){
            accountShowTv.setText("￥"+arrived);
        }
    }
    private void getAccountinfo() {
        if (NetUtils.isNetworkConnected(this)) {
            toggleShowLoading(true,null);
            ApiManager.getService(getApplicationContext()).getPayAccount(new Callback<payAccountRes>() {
                @Override
                public void success(payAccountRes payAccountRes, Response response) {
                    toggleShowLoading(false,null);
                    if (payAccountRes != null) {
                        String account = payAccountRes.getPayaccount();
                        arrived = CommonUtils.getStringFromPrice(payAccountRes.getArrived_balance());
                        not_arrived = CommonUtils.getStringFromPrice(payAccountRes.getNot_arrived_balance());
                        RadioClickEvent();
                        if (!CommonUtils.isEmpty(account)) {
                            bindTv.setText("已绑定:" + account);
                        }

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    toggleShowError(true, "信息获取失败", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getAccountinfo();
                        }
                    });
                    bindTv.setText(getInnerErrorInfo(error));
                }
            });
        } else {
            toggleShowError(true, "信息获取失败", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAccountinfo();
                }
            });
            showNetWorkError();
            bindTv.setText("暂无相关信息");
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return accountShowTv;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getAccountinfo();
        }
    }
}

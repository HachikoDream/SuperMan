package com.dreamspace.superman.UI.Activity.Main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.QRCode.QRCode;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.QRRes;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QRCodeShowActivity extends AbsActivity {


    @Bind(R.id.qrcode_iv)
    ImageView qrcodeIv;
    @Bind(R.id.qr_content)
    RelativeLayout contentView;
    private int ord_id;
    public static String ORD_ID = "ord_id";

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.content_qrcode_show);
    }

    @Override
    protected void prepareDatas() {

        ord_id = this.getIntent().getIntExtra(ORD_ID, -1);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPrivateInfo();
    }

    //从服务器获取订单相关的信息，放入二维码中
    private void getPrivateInfo() {
        toggleShowLoading(true, null);
        if (NetUtils.isNetworkConnected(this)) {
            toggleShowLoading(false, null);
            ApiManager.getService(getApplicationContext()).getQRCodeInfo(ord_id, new Callback<QRRes>() {
                @Override
                public void success(QRRes res, Response response) {
                    if (res != null) {
                        showInfoToView(res.getCode());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    toggleShowError(true, getInnerErrorInfo(error), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPrivateInfo();
                        }
                    });
                }
            });
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPrivateInfo();
                }
            });
        }
    }

    private void showInfoToView(String privateInfo) {
        Bitmap bitmap = QRCode.from(privateInfo).bitmap();
        qrcodeIv.setImageBitmap(bitmap);
    }

    @Override
    protected View getLoadingTargetView() {
        return contentView;
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

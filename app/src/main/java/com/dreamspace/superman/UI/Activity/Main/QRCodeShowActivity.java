package com.dreamspace.superman.UI.Activity.Main;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.dreamspace.superman.Common.QRCode.QRCode;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import butterknife.Bind;

public class QRCodeShowActivity extends AbsActivity {


    @Bind(R.id.qrcode_iv)
    ImageView qrcodeIv;
    private String privateInfo;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.content_qrcode_show);
    }

    @Override
    protected void prepareDatas() {
      privateInfo="wangdd";
    }

    @Override
    protected void initViews() {

    }
    private void getPrivateInfo(){

    }
    private void showInfoToView(){
        Bitmap bitmap= QRCode.from(privateInfo).bitmap();
        qrcodeIv.setImageBitmap(bitmap);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

}

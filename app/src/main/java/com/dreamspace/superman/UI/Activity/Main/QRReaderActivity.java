package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Superman.OnFinish;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QRReaderActivity extends AbsActivity implements QRCodeReaderView.OnQRCodeReadListener {
    @Bind(R.id.qrdecoderview)
    QRCodeReaderView qrdecoderview;
    private ProgressDialog pd;
    //// TODO: 2015/11/5 增加定时设置 超过18s仍未检测到二维码给用户提示
    private boolean isTimeUp=false;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.content_qrreader);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
       qrdecoderview.setOnQRCodeReadListener(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
       showAlertInfo(text,"确定",null);
    }

    @Override
    public void cameraNotFound() {
      showAlertInfo("未检测到相机硬件","确定",null);
    }

    @Override
    public void QRCodeNotFoundOnCamImage() {
//        showAlertInfo("未扫描到二维码相关图像","确定",null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrdecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrdecoderview.getCameraManager().stopPreview();
    }

    private void showAlertInfo(String info, String positiveMsg, final OnFinish finishListener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(info)
                .setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finishListener != null)
                            finishListener.finish(true);
                    }
                })
                .show();
        dialog.setCanceledOnTouchOutside(false);
    }

}

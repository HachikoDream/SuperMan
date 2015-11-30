package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Superman.OnFinish;
import com.dreamspace.superman.event.OrderChangeEvent;
import com.dreamspace.superman.model.api.QRRes;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class QRReaderActivity extends AbsActivity implements QRCodeReaderView.OnQRCodeReadListener {
    @Bind(R.id.qrdecoderview)
    QRCodeReaderView qrdecoderview;
    private ProgressDialog pd = null;
    //// TODO: 2015/11/5 增加定时设置 超过18s仍未检测到二维码给用户提示
    private boolean isTimeUp = false;
    private int ord_id;
    public static final String ORD_ID = "ord_id";
    public static final String COME_SOURCE = "comesource";
    public static final int COME_FROM_LIST = 233;
    private int comesource = -1;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.content_qrreader);
    }

    @Override
    protected void prepareDatas() {
        ord_id = this.getIntent().getIntExtra(ORD_ID, -1);
        comesource=this.getIntent().getIntExtra(COME_SOURCE,-1);
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
        sendQRInfo(text);
    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在提交您扫描到的信息", true, false);
        } else {
            pd.show();
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void sendQRInfo(String info) {
        if (NetUtils.isNetworkConnected(this)) {
            showPd();
            final QRRes res = new QRRes();
            res.setCode(info);
            ApiManager.getService(getApplicationContext()).scanQRCodeInfo(res, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if (response != null) {
                        showAlertInfo("您已成功完成本次课程", "确定", new OnFinish() {
                            @Override
                            public void finish(boolean isOk) {
                                if(comesource==COME_FROM_LIST){
                                    EventBus.getDefault().post(new OrderChangeEvent());
                                }else{
                                    setResult(RESULT_OK);
                                }

                                killSelf();
                            }
                        });
                    }
                }

                @Override
                public void failure(RetrofitError error) {
//                    showAlertInfo(getInnerErrorInfo(error),"确定",null);
                }
            });
        } else {
            showAlertInfo("暂无网络连接，请稍后再试", "确定", null);
        }


    }

    private void killSelf() {
        finish();
    }

    @Override
    public void cameraNotFound() {
        showAlertInfo("未检测到相机硬件", "确定", null);
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

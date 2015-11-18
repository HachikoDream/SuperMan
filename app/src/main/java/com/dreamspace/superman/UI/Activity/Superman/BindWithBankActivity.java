package com.dreamspace.superman.UI.Activity.Superman;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.BindReq;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BindWithBankActivity extends AbsActivity {
//// TODO: 2015/11/18 添加事件
    @Bind(R.id.bankaccount_ev)
    TextInputLayout bankaccountEv;
    @Bind(R.id.repeat_bankaccount_ev)
    TextInputLayout repeatBankaccountEv;
    @Bind(R.id.input_content)
    LinearLayout inputContent;
    @Bind(R.id.mybtn)
    Button mybtn;
    private String bank_count;
    private ProgressDialog pd;
    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_bind_with_bank);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
       mybtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isValid()){
                BindWithBankAccount(bank_count);
               }
           }
       });
    }
    private void showPd(){
       if(pd==null){
           pd=ProgressDialog.show(this,"","正在提交数据",true,false);
       }else{
           if(!pd.isShowing()){
               pd.show();
           }
       }
    }
    private void dismissPd(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }
    private void BindWithBankAccount(String bank_count) {
        showPd();
        if(NetUtils.isNetworkConnected(this)){
            BindReq req=new BindReq();
            req.setPayaccount(bank_count);
            ApiManager.getService(getApplicationContext()).bindWithBankAccount(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if(response!=null){
                        dismissPd();
                       showInfoByDialog("支付宝账号绑定成功，您之后可用该账号进行交易", new OnFinishListener() {
                           @Override
                           public void OnFinish() {
                               setResult(RESULT_OK);
                               finish();
                           }
                       });
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showInfoByDialog(getInnerErrorInfo(error),null);
                }
            });
        }else {
            dismissPd();
            showNetWorkError();
        }
    }

    private void showInfoByDialog(String info, final OnFinishListener finishListener ) {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finishListener != null) {
                            finishListener.OnFinish();
                        }


                    }
                })
                .show();
    }

    private boolean isValid(){
        bankaccountEv.setErrorEnabled(false);
        repeatBankaccountEv.setErrorEnabled(false);
        String account=bankaccountEv.getEditText().getText().toString();
        String repeatAccount=repeatBankaccountEv.getEditText().getText().toString();
        if(CommonUtils.isEmpty(account)){
            bankaccountEv.setErrorEnabled(true);
            bankaccountEv.setError("请先输入您的账号信息");
            return false;
        }else if(CommonUtils.isEmpty(repeatAccount)){
            repeatBankaccountEv.setErrorEnabled(true);
            repeatBankaccountEv.setError("请再次输入您的账号");
            return false;
        }else if(!repeatAccount.equals(account)){
            showToast("两次输入不一致");
            return false;
        }
        bank_count=account;
        return true;
    }
    @Override
    protected View getLoadingTargetView() {
        return inputContent;
    }
    private interface OnFinishListener{
        void OnFinish();
    }
}

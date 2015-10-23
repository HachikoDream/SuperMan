package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import butterknife.Bind;

public class SettingsActivity extends AbsActivity {
//// TODO: 2015/10/24 ADD 意见反馈  版本更新
    @Bind(R.id.check_update_tv)
    TextView checkUpdateTv;
    @Bind(R.id.find_bugs_tv)
    TextView findBugsTv;
    @Bind(R.id.exit_btn)
    Button exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
      exitBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              showWarningInfo();
          }
      });
    }
    private void showWarningInfo(){
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否确定退出登录并清空与您相关的所有数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearUserInfo();
                        //// TODO: 2015/10/24  改变通知方式
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void clearUserInfo(){
        String gsonString=PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.CLASSIFY);
        SharedPreferences defaultSp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        PreferenceUtils.clearPreference(getApplicationContext(), defaultSp);
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.CLASSIFY, gsonString);
        PreferenceUtils.putBoolean(getApplicationContext(), "IS_FIRST", false);
    }
    @Override
    protected View getLoadingTargetView() {
        return null;
    }

}

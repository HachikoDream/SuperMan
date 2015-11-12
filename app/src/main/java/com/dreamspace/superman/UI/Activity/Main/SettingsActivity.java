package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.DbRelated;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Register.LoginActivity;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import butterknife.Bind;

public class SettingsActivity extends AbsActivity {
    @Bind(R.id.check_update_tv)
    TextView checkUpdateTv;
    @Bind(R.id.find_bugs_tv)
    TextView findBugsTv;
    @Bind(R.id.exit_btn)
    Button exitBtn;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SettingsActivity.this;
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
        checkStateAndSetListener();
        checkUpdateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate();
            }
        });
    }

    private void checkUpdate() {
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        showToast("您当前已是最新版本");
                        break;
                    case UpdateStatus.Timeout: // time out
                        showToast("请检查您的网络连接");
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(this);
    }


    private void checkStateAndSetListener() {
        if (CommonUtils.isEmpty(PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.ACCOUNT))) {
            exitBtn.setText("现在登录");
            exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 2015/11/3  变为ForResult
                    finish();
                    readyGo(LoginActivity.class);
                }
            });
            findBugsTv.setVisibility(View.GONE);

        } else {
            exitBtn.setText("退出登录");
            exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWarningInfo();
                }
            });
            findBugsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readyGo(FeedbackActivity.class);
                }
            });

        }
    }

    private void showWarningInfo() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否确定退出登录并清空与您相关的所有数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearUserInfo();
                        DbRelated.clearAll(SettingsActivity.this);
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

    private void clearUserInfo() {
        String gsonString = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.CLASSIFY);
        SharedPreferences defaultSp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        PreferenceUtils.clearPreference(getApplicationContext(), defaultSp);
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.CLASSIFY, gsonString);
        PreferenceUtils.putBoolean(getApplicationContext(), "IS_FIRST", false);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

}

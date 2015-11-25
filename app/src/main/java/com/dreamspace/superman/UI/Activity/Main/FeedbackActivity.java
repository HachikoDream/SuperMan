package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Fragment.Drawer.FeedbackFragment;
import com.dreamspace.superman.model.api.FeedbackReq;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeedbackActivity extends AbsActivity implements FeedbackFragment.FeedbackComplete {

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.content_feedback);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {

    }



    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void complete() {
        finish();
    }
}

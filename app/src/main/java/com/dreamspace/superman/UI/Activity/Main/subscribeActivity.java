package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.DataUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SubscribeReq;
import com.dreamspace.superman.model.api.SubscribeRes;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SubscribeActivity extends AbsActivity {


    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    @Bind(R.id.course_name_tv)
    TextView courseNameTv;
    @Bind(R.id.username_tv)
    TextView usernameTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.price_tv)
    TextView priceTv;
    @Bind(R.id.realname_ev)
    TextInputLayout realNameEv;
    @Bind(R.id.connect_ev)
    TextInputLayout connectEv;
    @Bind(R.id.time_ev)
    TextInputLayout timeEv;
    @Bind(R.id.address_ev)
    TextInputLayout addressEv;
    @Bind(R.id.another_info_ev)
    TextInputLayout anotherInfoEv;
    @Bind(R.id.suscribe_btn)
    Button suscribeBtn;
    private LessonInfo mLessonInfo = null;
    private String realName;
    private String phoneNum;
    private String meetTime;
    private String meetAddress;
    private String anotherInfo;
    private ProgressDialog pd;


    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_subscribe);
    }

    @Override
    protected void prepareDatas() {
        mLessonInfo = this.getIntent().getParcelableExtra(LessonDetailInfoActivity.LESSON_INFO);
    }

    @Override
    protected void initViews() {
        if (mLessonInfo != null) {
            showBaseInfo();
        }
        suscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    SubscribeReq req = new SubscribeReq();
                    req.setName(realName);
                    req.setLess_id(mLessonInfo.getId());
                    req.setPhone(phoneNum);
                    req.setRemark(anotherInfo);
                    req.setSite(meetAddress);
                    req.setTime(meetTime);
                    suscribeLesson(req);
                }
            }
        });
        timeEv.getEditText().setInputType(InputType.TYPE_NULL);
        timeEv.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDataPickerDialog();
                }
                return true;
            }
        });

    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在提交请求", true, false);
        } else {
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void suscribeLesson(SubscribeReq req) {
        showPd();
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(this).subscribeOrder(req, new Callback<SubscribeRes>() {
                @Override
                public void success(SubscribeRes subscribeRes, Response response) {
                    dismissPd();
                    if (subscribeRes != null) {
                        showSuccessDialog();
                    } else {
                        showToast("暂时不能响应您的请求,请稍后再试");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showInnerError(error);
                }
            });
        } else {
            dismissPd();
            showNetWorkError();
        }
    }

    private void showSuccessDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("恭喜您预约成功,您可以在我的订单中看到本订单的详细信息和进展")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    private void showBaseInfo() {
        usernameTv.setText(mLessonInfo.getName());
        courseNameTv.setText(mLessonInfo.getLess_name());
        priceTv.setText(CommonUtils.getPriceWithInfo(mLessonInfo.getPrice()));
        Tools.showImageWithGlide(this, profileImage, mLessonInfo.getImage());
        timeTv.setText(mLessonInfo.getKeeptime() + "小时");
        String realName = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.REALNAME);
        String phoneNum = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.PHONE);
        if (!CommonUtils.isEmpty(realName)) {
            realNameEv.getEditText().setText(realName);
        }
        if (!CommonUtils.isEmpty(phoneNum)) {
            connectEv.getEditText().setText(phoneNum);
        }
    }

    private boolean isValid() {
        setTextInputDefault();
        if (CommonUtils.isEmpty(realNameEv.getEditText().getText().toString())) {
            realNameEv.setErrorEnabled(true);
            realNameEv.setError("请先填写您的姓名");
            return false;
        } else if (CommonUtils.isEmpty(connectEv.getEditText().getText().toString()) || connectEv.getEditText().getText().toString().length() != 11) {
            connectEv.setErrorEnabled(true);
            connectEv.setError("请输入您正确的联系方式");
            return false;
        } else if (CommonUtils.isEmpty(timeEv.getEditText().getText().toString())) {
            timeEv.setErrorEnabled(true);
            timeEv.setError("请输入您的预约时间");
            return false;
        } else if (CommonUtils.isEmpty(addressEv.getEditText().getText().toString())) {
            addressEv.setErrorEnabled(true);
            addressEv.setError("请输入见面地点");
            return false;
        } else {
            realName = realNameEv.getEditText().getText().toString();
            phoneNum = connectEv.getEditText().getText().toString();
            meetTime = timeEv.getEditText().getText().toString();
            meetAddress = addressEv.getEditText().getText().toString();
            anotherInfo = anotherInfoEv.getEditText().getText().toString();
        }
        return true;
    }

    private void setTextInputDefault() {
        realNameEv.setErrorEnabled(false);
        connectEv.setErrorEnabled(false);
        timeEv.setErrorEnabled(false);
        addressEv.setErrorEnabled(false);
        anotherInfoEv.setErrorEnabled(false);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void showDataPickerDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(SubscribeActivity.this).create();
        dialog.show();
        DatePicker picker = new DatePicker(SubscribeActivity.this);
        picker.setDate(DataUtils.getCurrentYear(), DataUtils.getCurrentMonth());
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                dialog.dismiss();
                onDatePickedFromDialog(date);
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(picker, params);
        dialog.getWindow().setGravity(Gravity.CENTER);

    }


    public void onDatePickedFromDialog(String date) {
        if (!CommonUtils.isEmpty(date)) {
            timeEv.getEditText().setText(date);
        }
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

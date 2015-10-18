package com.dreamspace.superman.UI.Activity.Superman;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.PublishReq;
import com.dreamspace.superman.model.api.PublishRes;

import java.text.NumberFormat;
import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddCourseActivity extends AbsActivity {


    @Bind(R.id.coursename_ev)
    TextInputLayout coursenameEv;
    @Bind(R.id.coursetime_ev)
    TextInputLayout coursetimeEv;
    @Bind(R.id.price_ev)
    TextInputLayout priceEv;
    @Bind(R.id.mybtn)
    Button mybtn;
    @Bind(R.id.desc_ev)
    EditText descEv;
    String course_name;
    String keep_time;
    int price;
    String description;
    private ProgressDialog pd;
    public static final String COME_SOURCE="comesource";
    public static final int FROM_ADD=231;
    public static final int FROM_MODIFY=232;
    private String source;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_add_course);
    }

    @Override
    protected void prepareDatas() {
       //todo 判断从哪里来的请求
        source=getIntent().getStringExtra(COME_SOURCE);
        if (source.equals(FROM_ADD)){

        }else if(source.equals(FROM_MODIFY)){
            //todo 获取课程信息,展示出来
        }
    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在提交数据", true, false);
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

    @Override
    protected void initViews() {
        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
                if (isValid()) {
                    PublishReq req=new PublishReq();
                    req.setDescription(description);
                    req.setKeeptime(keep_time);
                    req.setName(course_name);
                    req.setPrice(price);
                    AddCourse(req);
                }
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void AddCourse(PublishReq req) {
        showPd();
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(getApplicationContext()).publishLessonBySm(req, new Callback<PublishRes>() {
                @Override
                public void success(PublishRes publishRes, Response response) {
                    dismissPd();
                    setResult(RESULT_OK);
                    finish();
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

    private void getValue() {
        course_name = coursenameEv.getEditText().getText().toString();
        keep_time = coursetimeEv.getEditText().getText().toString();
        String price_content = priceEv.getEditText().getText().toString();
        if (CommonUtils.isEmpty(price_content)) {
            price = -1;
        } else {
            price = CommonUtils.getPriceFromString(price_content);
        }
        description = descEv.getText().toString();

    }

    private boolean isValid() {
        setDefaultForinput();
        if (CommonUtils.isEmpty(course_name)) {
            coursenameEv.setErrorEnabled(true);
            coursenameEv.setError("课程名不能为空");
            coursenameEv.getEditText().setSelected(true);
            showToast("课程名不能为空");
            return false;
        } else if (course_name.length() >= 20) {
            coursenameEv.setErrorEnabled(true);
            coursenameEv.setError("课程名称不能大于20个字符");
            showToast("课程名称不能大于20个字符");
            return false;
        } else if (CommonUtils.isEmpty(keep_time)) {
            coursetimeEv.setErrorEnabled(true);
            coursetimeEv.setError("请先填写课时");
            showToast("请先填写课时");
            return false;
        } else if (price == -1) {
            priceEv.setErrorEnabled(true);
            priceEv.setError("请填写正确的价格");
            showToast("请填写正确的价格");
            return false;
        } else if (CommonUtils.isEmpty(description)) {
            showToast("请简要描述课程内容");
            descEv.setSelected(true);
            return false;
        }
        return true;
    }

    private void setDefaultForinput() {
        coursenameEv.setErrorEnabled(false);
        coursetimeEv.setErrorEnabled(false);
        priceEv.setErrorEnabled(false);
    }

}

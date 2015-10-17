package com.dreamspace.superman.UI.Activity.Superman;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import java.text.NumberFormat;
import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_add_course);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
       mybtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getValue();
           }
       });
    }
    private void getValue(){
        course_name=coursenameEv.getEditText().getText().toString();
        keep_time=coursetimeEv.getEditText().getText().toString();
        String price_content=priceEv.getEditText().getText().toString();
        if(CommonUtils.isEmpty(price_content)){
            price=-1;
        }else {
            price=CommonUtils.getPriceFromString(price_content);
        }
        description=descEv.getText().toString();

    }
    private boolean isValid(){

     return true;
    }

}

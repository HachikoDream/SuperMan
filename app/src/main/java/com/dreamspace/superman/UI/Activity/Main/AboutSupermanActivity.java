package com.dreamspace.superman.UI.Activity.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import butterknife.Bind;

public class AboutSupermanActivity extends AbsActivity {


    @Bind(R.id.first_page)
    RelativeLayout firstLayout;
    @Bind(R.id.tobesuperman_btn)
    Button tobeSmBtn;
    private int width;
    private int height;


    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_about_superman);
    }

    @Override
    protected void prepareDatas() {
    }

    @Override
    protected void initViews() {
        getSizeInfo();
        firstLayout.setLayoutParams(new LinearLayout.LayoutParams(width,height-150));
        tobeSmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void getSizeInfo() {
        WindowManager wm = this.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

    }
}

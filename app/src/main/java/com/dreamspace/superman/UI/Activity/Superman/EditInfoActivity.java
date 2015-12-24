package com.dreamspace.superman.UI.Activity.Superman;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditInfoActivity extends AbsActivity implements View.OnClickListener {


    @Bind(R.id.sm_tag_tv)
    TextView smTagTv;
    @Bind(R.id.superman_tag_layout)
    RelativeLayout supermanTagLayout;
    @Bind(R.id.sm_glory_tv)
    TextView smGloryTv;
    @Bind(R.id.superman_glory_layout)
    RelativeLayout supermanGloryLayout;
    @Bind(R.id.sm_intro_tv)
    TextView smIntroTv;
    @Bind(R.id.superman_intro_layout)
    RelativeLayout supermanIntroLayout;
    @Bind(R.id.superman_certificate_layout)
    RelativeLayout supermanCertificateLayout;
    private static final String EDIT_GLORY_TITLE = "编辑所获荣誉";
    private static final String EDIT_INTRO_TITLE = "编辑个人简介";
    private static final String EDIT_TAG_TITLE = "编辑达人标签";
    public static final String TITLE_INFO = "title_info";//Bundle中的key，用于传递修改详情界面的标题
    public static final String PRE_INFO = "old_info";//Bundle中的key，用于传递修改详情界面的标题
    public static final String MODIFIED_INFO = "new_info";//Bundle中的key，用于获取由详情界面传回的新的信息
    public static final int MODIFY_TAG_INFO = 235;
    public static final int MODIFY_GLORY_INFO = 236;
    public static final int MODIFY_INTRO_INFO = 237;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_edit_info);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        supermanTagLayout.setOnClickListener(this);
        supermanGloryLayout.setOnClickListener(this);
        supermanIntroLayout.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_finish) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        String title = null;
        int request_code = 0;
        String old_info = null;
        switch (v.getId()) {
            case R.id.superman_intro_layout:
                title = EDIT_INTRO_TITLE;
                request_code = MODIFY_INTRO_INFO;
                old_info = smIntroTv.getText().toString();
                break;
            case R.id.superman_tag_layout:
                title = EDIT_TAG_TITLE;
                request_code = MODIFY_TAG_INFO;
                old_info = smTagTv.getText().toString();
                break;
            case R.id.superman_glory_layout:
                title = EDIT_GLORY_TITLE;
                request_code = MODIFY_GLORY_INFO;
                old_info = smGloryTv.getText().toString();
                break;
        }
        Bundle b = new Bundle();
        b.putString(TITLE_INFO, title);
        b.putString(PRE_INFO, old_info);
        readyGoForResult(EditDetailInfoActivity.class, request_code, b);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String new_info = null;
        if (data != null) {
            new_info = data.getStringExtra(MODIFIED_INFO);
        }
        if (!CommonUtils.isEmpty(new_info)) {
            switch (requestCode) {
                case MODIFY_GLORY_INFO:
                    smGloryTv.setText(new_info);
                    break;
                case MODIFY_TAG_INFO:
                    smTagTv.setText(new_info);
                    break;
                case MODIFY_INTRO_INFO:
                    smIntroTv.setText(new_info);
                    break;
            }

        }
    }
}

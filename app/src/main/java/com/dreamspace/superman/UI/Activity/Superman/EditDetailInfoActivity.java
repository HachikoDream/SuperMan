package com.dreamspace.superman.UI.Activity.Superman;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.InputUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditDetailInfoActivity extends AbsActivity {


    @Bind(R.id.detail_info_et)
    EditText detailInfoEt;
    @Bind(R.id.textnumber_tv)
    TextView textnumberTv;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_edit_detail_info);
    }

    @Override
    protected void prepareDatas() {
        String title = this.getIntent().getStringExtra(EditInfoActivity.TITLE_INFO);
        String old_info = this.getIntent().getStringExtra(EditInfoActivity.PRE_INFO);
        if (!CommonUtils.isEmpty(title)) {
            setTitle(title);
        }
        if (!CommonUtils.isEmpty(old_info)) {
            detailInfoEt.setText(old_info);
            detailInfoEt.setSelection(old_info.length());
            textnumberTv.setText(String.valueOf(old_info.length()));
        }
    }

    @Override
    protected void initViews() {
        detailInfoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textnumberTv.setText(String.valueOf(s.length()));
            }
        });

    }

    @Override
    public void onBackPressed() {
        String new_info = detailInfoEt.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(EditInfoActivity.MODIFIED_INFO, new_info);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

}

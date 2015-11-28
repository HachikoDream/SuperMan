package com.dreamspace.superman.UI.Activity.Register;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Main.MainActivity;
import com.dreamspace.superman.UI.Fragment.Drawer.IndexFragment;
import com.dreamspace.superman.UI.View.SelectorImageView;
import com.dreamspace.superman.model.Catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class ChooseClassifyActivity extends AbsActivity {
    @Bind({R.id.ball, R.id.dance, R.id.paint, R.id.english, R.id.educate, R.id.compete, R.id.code, R.id.camera, R.id.dressup, R.id.game, R.id.speak, R.id.others})
    SelectorImageView[]
            mSelectorImageViews = new SelectorImageView[12];
    private String[] mFragmentNames = {"体育运动", "器乐舞蹈", "书法绘画", "留学外语", "学习辅导", "竞赛指导", "软件编程", "摄影后期", "化妆美甲", "游戏竞技", "演讲主持", "其他"};
    private List<Catalog> mSelectedFNs = new ArrayList<>();
    private List<Catalog> mCatalogs = new ArrayList<>();
    @Bind(R.id.begin_read)
    Button mButton;
    private static final String COME_FROM_INDEX = "INDEX";
    private Map<Catalog, SelectorImageView> maps = new HashMap<>();

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_choose_classify);
    }

    @Override
    protected void prepareDatas() {
        Catalog catalog;
        for (int i = 1; i <= mFragmentNames.length; i++) {
            catalog = new Catalog();
            catalog.setIcon("TEST");
            catalog.setId(i);
            catalog.setName(mFragmentNames[i - 1]);
            mCatalogs.add(catalog);
        }
        for (int i = 0; i < mSelectorImageViews.length; i++) {
            maps.put(mCatalogs.get(i), mSelectorImageViews[i]);
        }
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        showSelectedItems();
        final String source = this.getIntent().getStringExtra("SOURCE");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("INFO", "source: " + source);
                fillSelectedIds();
                if (mSelectedFNs.size() < 6) {
                    showToast("请至少选择6个分类");
                } else {
                    PreferenceUtils.writeClassifyIntoSp(ChooseClassifyActivity.this.getApplicationContext(), mSelectedFNs);
                    if (source.equals(COME_FROM_INDEX)) {
                        ChooseClassifyActivity.this.setResult(RESULT_OK);
                        ChooseClassifyActivity.this.finish();
                    } else {
//                        readyGo(MainActivity.class);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void showSelectedItems() {
        List<Catalog> mSeleted = PreferenceUtils.getClassifyItems(getApplicationContext());
        for (Catalog item : mSeleted) {
            SelectorImageView view = maps.get(item);
            if (view != null)
                view.setIsSelected(true);
        }
    }

    public List<Catalog> getSelectedNames() {
        return mSelectedFNs;
    }

    public void fillSelectedIds() {
        for (int i = 0; i < mSelectorImageViews.length; i++) {
            if (mSelectorImageViews[i].isSelected()) {
                mSelectedFNs.add(mCatalogs.get(i));
            }
        }
    }

}

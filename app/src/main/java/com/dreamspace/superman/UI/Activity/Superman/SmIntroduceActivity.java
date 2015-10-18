package com.dreamspace.superman.UI.Activity.Superman;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.CourseIntroductionFragment;
import com.dreamspace.superman.UI.Fragment.SmCourseListFragment;
import com.dreamspace.superman.UI.Fragment.StudentCommentListFragment;
import com.dreamspace.superman.UI.Fragment.SupermanIntroductionFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class SmIntroduceActivity extends AbsActivity {

    private SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private List<BaseLazyFragment> mFragments = new ArrayList<>();
    private int color = 0;
    private int normalColor = 0;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_sm_introduce);
    }

    @Override
    protected void prepareDatas() {
        color = getResources().getColor(R.color.navi_color);
        normalColor=getResources().getColor(R.color.select_tab_color);
        mFragments.add(new SmCourseListFragment());
        mFragments.add(new SupermanIntroductionFragment());
    }

    @Override
    protected void initViews() {
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_layout);
        mAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mSlidingTabLayout.setStartColor(normalColor);
        mSlidingTabLayout.setViewPager(mViewPager);
        SlidingTabStrip.SimpleTabColorizer colorizer = new SlidingTabStrip.SimpleTabColorizer() {
            //覆盖Tab底部提示条的颜色
            @Override
            public int getIndicatorColor(int position) {
                return color;
            }
            //覆盖滑动到指定Tab处的文字颜色
            @Override
            public int getSelectedTitleColor(int position) {
                return color;
            }

            @Override
            public int getNormalTitleColor(int position) {
                return normalColor;
            }
        };
        mSlidingTabLayout.setCustomTabColorizer(colorizer);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }
}

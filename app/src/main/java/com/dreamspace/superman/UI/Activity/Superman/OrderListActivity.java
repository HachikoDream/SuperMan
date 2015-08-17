package com.dreamspace.superman.UI.Activity.Superman;

import android.support.v4.view.ViewPager;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Superman.SmCancelFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Superman.SmCompleteFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Superman.SmNoMeetFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Superman.SmNopaymentFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Superman.SmSubscribeFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AbsActivity {
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        List<BaseFragment> mFragments = new ArrayList<>();
        mFragments.add(new SmSubscribeFragment());
        mFragments.add(new SmNopaymentFragment());
        mFragments.add(new SmNoMeetFragment());
        mFragments.add(new SmCompleteFragment());
        mFragments.add(new SmCancelFragment());
        mAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        final int color = getResources().getColor(R.color.navi_color);
        final int normalcolor = getResources().getColor(R.color.near_black);
        mSlidingTabLayout.setStartColor(normalcolor);
        mSlidingTabLayout.setFillTheWidth(false);
        mSlidingTabLayout.setViewPager(mViewPager);
        SlidingTabStrip.SimpleTabColorizer colorizer = new SlidingTabStrip.SimpleTabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return color;
            }

            @Override
            public int getSelectedTitleColor(int position) {
                return color;
            }

            @Override
            public int getNormalTitleColor(int position) {
                return normalcolor;
            }
        };
        mSlidingTabLayout.setCustomTabColorizer(colorizer);
    }

}
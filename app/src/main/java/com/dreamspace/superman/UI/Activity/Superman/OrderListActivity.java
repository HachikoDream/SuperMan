package com.dreamspace.superman.UI.Activity.Superman;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.SmOrderContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Orders.Superman.SmSubscribeFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;
import com.dreamspace.superman.model.OrderClassify;

import de.greenrobot.event.EventBus;

public class OrderListActivity extends AbsActivity {
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private SmOrderContainerPagerAdapter mAdapter;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        final OrderClassify[] mEntities = Constant.ORDER_RELATED.orderClassifys;
        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.sliding_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new SmOrderContainerPagerAdapter(getSupportFragmentManager(),mEntities);
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
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SmSubscribeFragment fragment = (SmSubscribeFragment) mViewPager.getAdapter().instantiateItem(mViewPager, position);
                fragment.onPageSelected(position, mEntities[position]);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        SmSubscribeFragment fragment = (SmSubscribeFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        fragment.onPageSelected(0, mEntities[0]);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


}

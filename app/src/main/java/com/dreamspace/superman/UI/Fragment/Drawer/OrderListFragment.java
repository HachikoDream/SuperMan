package com.dreamspace.superman.UI.Fragment.Drawer;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.OrderContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class OrderListFragment extends BaseLazyFragment {

    @Bind(R.id.viewpager)
     ViewPager mViewPager;
    @Bind(R.id.sliding_layout)
     SlidingTabLayout mSlidingTabLayout;
    private OrderContainerPagerAdapter mAdapter;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        List<String> mEntities = Arrays.asList(getResources().getStringArray(R.array.order_list_item));
        mAdapter = new OrderContainerPagerAdapter(getChildFragmentManager(), mEntities);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mEntities.size());
        final int color = getResources().getColor(R.color.navi_color);
        final int startcolr=getResources().getColor(R.color.near_white);
        mSlidingTabLayout.setStartColor(startcolr);
        mSlidingTabLayout.setFillTheWidth(false);
        mSlidingTabLayout.setViewPager(mViewPager);
        SlidingTabStrip.SimpleTabColorizer colorizer = new SlidingTabStrip.SimpleTabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return color;
            }

            @Override
            public int getSelectedTitleColor(int position) {
                return startcolr;
            }

            @Override
            public int getNormalTitleColor(int position) {
                return startcolr;
            }
        };
        mSlidingTabLayout.setCustomTabColorizer(colorizer);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_order_list;
    }
}

package com.dreamspace.superman.UI.Fragment.Drawer;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.OrderContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Index.HandpickFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Person.SubscribeFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;
import com.dreamspace.superman.model.OrderClassify;

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
        final OrderClassify[] mEntities = Constant.ORDER_CLASSIFY.orderClassifys;
        mAdapter = new OrderContainerPagerAdapter(getChildFragmentManager(), mEntities);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mEntities.length);
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
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SubscribeFragment fragment = (SubscribeFragment) mViewPager.getAdapter().instantiateItem(mViewPager, position);
                fragment.onPageSelected(position, mEntities[position]);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        SubscribeFragment fragment = (SubscribeFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        fragment.onPageSelected(0, mEntities[0]);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_order_list;
    }
}

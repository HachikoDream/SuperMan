package com.dreamspace.superman.UI.Fragment.Drawer;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Person.CancelFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Person.CompleteFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Person.NoMeetFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Person.NopaymentFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Person.SubscribeFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends BaseFragment {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void initViews(View view) {
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
    }

    @Override
    public void initDatas() {
        List<BaseFragment> mFragments = new ArrayList<>();
        mFragments.add(new SubscribeFragment());
        mFragments.add(new NopaymentFragment());
        mFragments.add(new NoMeetFragment());
        mFragments.add(new CompleteFragment());
        mFragments.add(new CancelFragment());
        mAdapter = new CommonFragmentAdapter(getChildFragmentManager(), mFragments);
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

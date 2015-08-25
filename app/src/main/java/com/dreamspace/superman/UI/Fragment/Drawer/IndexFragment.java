package com.dreamspace.superman.UI.Fragment.Drawer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.UI.Fragment.Index.BallFragment;
import com.dreamspace.superman.UI.Fragment.Index.GymFragment;
import com.dreamspace.superman.UI.Fragment.Index.HandpickFragment;
import com.dreamspace.superman.UI.Fragment.Index.SwimFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends BaseLazyFragment {

    @Bind(R.id.viewpager)
     ViewPager mViewPager;
    @Bind(R.id.sliding_layout)
     SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;
    public IndexFragment() {
        // Required empty public constructor
    }


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
        List<BaseFragment> mFragments=new ArrayList<>();
        mFragments.add(new HandpickFragment());
        mFragments.add(new SwimFragment());
        mFragments.add(new GymFragment());
        mFragments.add(new BallFragment());
        mAdapter = new CommonFragmentAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mSlidingTabLayout.setFillTheWidth(false);
        mSlidingTabLayout.setViewPager(mViewPager);
        final int color = getResources().getColor(R.color.navi_color);
        final int normalcolor=getResources().getColor(R.color.near_white);
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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_index;
    }



}

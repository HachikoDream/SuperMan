package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;
    private BaseFragment[] mFragments = {new OneFragment(), new TwoFragment(), new ThreeFragment()};

    public IndexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_index, container, false);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_layout);
//        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab_view,R.id.tab_tv);
        mAdapter = new CommonFragmentAdapter(getChildFragmentManager(), mFragments);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
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
//        mSlidingTabLayout.setSelectedIndicatorColors(getColor(R.color.navi_color));
        return view;
    }

    private int getColor(int resID) {
        return getResources().getColor(resID);
    }



}

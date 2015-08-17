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

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;
    public IndexFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("TEST","on Create");
        final View view = inflater.inflate(R.layout.fragment_index, container, false);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_layout);
        List<BaseFragment> mFragments=new ArrayList<>();
        mFragments.add(new HandpickFragment());
        mFragments.add(new SwimFragment());
        mFragments.add(new GymFragment());
        mFragments.add(new BallFragment());
        mAdapter = new CommonFragmentAdapter(getChildFragmentManager(), mFragments);
//        mAdapter = new CommonFragmentAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
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
        return view;
    }


    private BaseListFragment<Course> getBaseListFt(String className){
        try {
            return (BaseListFragment<Course>) Class.forName(className).newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getColor(int resID) {
        return getResources().getColor(resID);
    }



}

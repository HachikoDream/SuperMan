package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.culiu.mhvp.core.InnerScrollerContainer;
import com.culiu.mhvp.core.OuterPagerAdapter;
import com.culiu.mhvp.core.OuterScroller;
import com.dreamspace.superman.UI.Fragment.Index.SmListFragment;

/**
 * Created by Wells on 2016/2/24.
 */
public class SmContainerPagerAdapter extends FragmentPagerAdapter implements OuterPagerAdapter {
    private final static String[] pagerTitles = {"最新入驻", "最受欢迎"};
    public final static String[] SORT_TYPE = {"date", "heat"};
    private OuterScroller mOuterScroller;
    public SmContainerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new SmListFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles[position];
    }

    @Override
    public void setOuterScroller(OuterScroller outerScroller) {
         this.mOuterScroller=outerScroller;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // cuz Fragment has some weird life cycle.
        InnerScrollerContainer fragment =
                (InnerScrollerContainer) super.instantiateItem(container, position);

        if (null != mOuterScroller) {
            fragment.setOuterScroller(mOuterScroller, position);
        }
        return fragment;
    }
}

package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.culiu.mhvp.core.InnerScrollerContainer;
import com.culiu.mhvp.core.OuterPagerAdapter;
import com.culiu.mhvp.core.OuterScroller;
import com.dreamspace.superman.UI.Fragment.Index.LessonListFragment;

/**
 * Created by Wells on 2016/2/25.
 */
public class LessonListContainerPagerAdapter extends FragmentPagerAdapter implements OuterPagerAdapter {
    private final static String[] pagerTitles = {"精选", "上新", "热门"};
    private OuterScroller mOuterScroller;

    public LessonListContainerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new LessonListFragment();
    }

    @Override
    public int getCount() {
        return pagerTitles.length;
    }

    @Override
    public void setOuterScroller(OuterScroller outerScroller) {
        this.mOuterScroller=outerScroller;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles[position];
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

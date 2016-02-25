package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dreamspace.superman.UI.Fragment.Index.SmListFragment;

/**
 * Created by Wells on 2016/2/24.
 */
public class SmContainerPagerAdapter extends FragmentStatePagerAdapter {
    private final static String[] pagerTitles = {"最新入驻", "最受欢迎"};
    public final static String[] SORT_TYPE = {"date", "heat"};

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
}

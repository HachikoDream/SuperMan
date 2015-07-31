package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class CommonFragmentAdapter extends FragmentStatePagerAdapter {

    private Fragment[] mFragments;
    private String[] titleList;
    public void setmFragments(Fragment[] mFragments) {
        this.mFragments = mFragments;
        this.notifyDataSetChanged();
    }

    public CommonFragmentAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments[position];
    }

    @Override
    public int getCount() {
        return this.mFragments.length;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.format("item%d", position);
    }
}
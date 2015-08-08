package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class CommonFragmentAdapter extends FragmentStatePagerAdapter {

    private BaseFragment[] mFragments;
    public void setmFragments(BaseFragment[] mFragments) {
        this.mFragments = mFragments;
        this.notifyDataSetChanged();
    }

    public CommonFragmentAdapter(FragmentManager fm, BaseFragment[] fragments) {
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
        Log.i("ORDER_TEST","getpageTitle + "+position);
        return this.mFragments[position].getTAG();
    }
}
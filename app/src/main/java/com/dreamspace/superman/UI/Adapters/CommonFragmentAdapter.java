package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class CommonFragmentAdapter extends FragmentStatePagerAdapter {

    private List<? extends BaseLazyFragment> mFragments;
    public void setmFragments(List<BaseLazyFragment> mFragments) {
        this.mFragments = mFragments;
        this.notifyDataSetChanged();
    }

    public CommonFragmentAdapter(FragmentManager fm, List<? extends BaseLazyFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.i("ORDER_TEST", "getpageTitle + " + position);
        return this.mFragments.get(position).getTAG();
    }
}
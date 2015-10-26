package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dreamspace.superman.UI.Fragment.Orders.Person.SubscribeFragment;
import com.dreamspace.superman.UI.Fragment.Orders.Superman.SmSubscribeFragment;
import com.dreamspace.superman.model.OrderClassify;

import java.util.List;

/**
 * Created by Wells on 2015/9/21.
 */
public class SmOrderContainerPagerAdapter extends FragmentStatePagerAdapter {

    private OrderClassify[] mCategoryList = null;

    public SmOrderContainerPagerAdapter(FragmentManager fm, OrderClassify[] categoryList) {
        super(fm);
        mCategoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        return new SmSubscribeFragment();
    }

    @Override
    public int getCount() {
        return null != mCategoryList ? mCategoryList.length : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null != mCategoryList ? mCategoryList[position].getName() : null;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;

    }
}

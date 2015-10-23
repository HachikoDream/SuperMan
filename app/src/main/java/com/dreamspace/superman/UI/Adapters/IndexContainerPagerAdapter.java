/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dreamspace.superman.UI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
/*
   FragmentPagerAdapter: 把它所有持有的Fragment保存在内存里面
 */
import com.dreamspace.superman.UI.Fragment.Index.HandpickFragment;
import com.dreamspace.superman.model.Catalog;

import java.util.List;

/**
 * Author:  Tau.Chen
 * Email:   1076559197@qq.com | tauchen1990@gmail.com
 * Date:    2015/4/2.
 * Description:
 */
public class IndexContainerPagerAdapter extends FragmentPagerAdapter {

    private List<Catalog> mCategoryList = null;

    public IndexContainerPagerAdapter(FragmentManager fm, List<Catalog> categoryList) {
        super(fm);
        mCategoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        return new HandpickFragment();
    }

    @Override
    public int getCount() {
        return null != mCategoryList ? mCategoryList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null != mCategoryList ? mCategoryList.get(position).getName() : null;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;

    }
    public void setmCategoryList(List<Catalog> mCategoryList) {
        this.mCategoryList = mCategoryList;
        this.notifyDataSetChanged();
    }
}

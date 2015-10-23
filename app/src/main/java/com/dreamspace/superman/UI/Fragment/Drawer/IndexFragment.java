package com.dreamspace.superman.UI.Fragment.Drawer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Register.ChooseClassifyActivity;
import com.dreamspace.superman.UI.Adapters.IndexContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Index.HandpickFragment;
import com.dreamspace.superman.UI.View.smartlayout.SmartTabLayout;
import com.dreamspace.superman.model.Catalog;

import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends BaseLazyFragment {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.sliding_layout)
    SmartTabLayout mSlidingTabLayout;
    @Bind(R.id.choose_classify)
    ImageView mImageView;
    private IndexContainerPagerAdapter mAdapter;
    private static final int REQUEST_CHOOSE_CLASSIFY = 233;
    private int tabHeight;
    private  List<Catalog> items;

    public IndexFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        setItems(PreferenceUtils.getClassifyItems(getActivity().getApplicationContext()));
        mAdapter = new IndexContainerPagerAdapter(getChildFragmentManager(), items);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(items.size());
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                HandpickFragment fragment = (HandpickFragment) mViewPager.getAdapter().instantiateItem(mViewPager, position);
                fragment.onPageSelected(position, items.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        HandpickFragment fragment = (HandpickFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        fragment.onPageSelected(0,items.get(0));
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("SOURCE", "INDEX");
                readyGoForResult(ChooseClassifyActivity.class, REQUEST_CHOOSE_CLASSIFY, b);
            }
        });
    }

    public void setItems(List<Catalog> items) {
        this.items = items;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_index;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //从sp获取数据 刷新viewpager项目
        if (resultCode == Activity.RESULT_OK) {
            List<Catalog> mCatalogs=PreferenceUtils.getClassifyItems(getActivity().getApplicationContext());
            setItems(mCatalogs);
            mAdapter.setmCategoryList(mCatalogs);
            mSlidingTabLayout.setViewPager(mViewPager);
            HandpickFragment fragment = (HandpickFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
            fragment.onPageSelected(0, items.get(0));
        }
    }
}

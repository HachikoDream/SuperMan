package com.dreamspace.superman.UI.Fragment.Drawer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
        final List<Catalog> items = PreferenceUtils.getClassifyItems(getActivity().getApplicationContext());
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

//        ViewTreeObserver vto = mSlidingTabLayout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mSlidingTabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);//removeOnGlobalLayoutListener
//                tabHeight = mSlidingTabLayout.getHeight();
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, tabHeight);
//                mImageView.setLayoutParams(params);
//            }
//        });
//        final int color = getResources().getColor(R.color.navi_color);
//        final int normalcolor = getResources().getColor(R.color.near_white);
//        SlidingTabStrip.SimpleTabColorizer colorizer = new SlidingTabStrip.SimpleTabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return color;
//            }
//
//            @Override
//            public int getSelectedTitleColor(int position) {
//                return color;
//            }
//
//            @Override
//            public int getNormalTitleColor(int position) {
//                return normalcolor;
//            }
//        };
//        mSlidingTabLayout.setCustomTabColorizer(colorizer);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("SOURCE", "INDEX");
                readyGoForResult(ChooseClassifyActivity.class, REQUEST_CHOOSE_CLASSIFY, b);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_index;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //从sp获取数据 刷新viewpager项目
        if (resultCode == Activity.RESULT_OK) {
            mAdapter.setmCategoryList(PreferenceUtils.getClassifyItems(getActivity().getApplicationContext()));
            mSlidingTabLayout.setViewPager(mViewPager);
        }
    }
}

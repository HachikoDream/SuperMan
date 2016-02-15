package com.dreamspace.superman.UI.Fragment.Drawer;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.ViewFactory;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CatalogAdapter;
import com.dreamspace.superman.UI.Adapters.IndexContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Index.HandpickFragment;
import com.dreamspace.superman.UI.View.smartlayout.SmartTabLayout;
import com.dreamspace.superman.event.RefreshEvent;
import com.dreamspace.superman.model.Catalog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import me.codeboy.android.cycleviewpager.CycleViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends BaseLazyFragment {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.sliding_layout)
    SmartTabLayout mSlidingTabLayout;
    @Bind(R.id.catalog_rv)
    RecyclerView catalogRv;
    @Bind(R.id.index_swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private IndexContainerPagerAdapter mAdapter;
    private List<Catalog> items;

    public IndexFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onFirstUserVisible() {
        HandpickFragment fragment = (HandpickFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
        fragment.onPageSelected(0, items.get(0));
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
        EventBus.getDefault().register(this);
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
        List<View> views = new ArrayList<View>();

        // 将最后一个view添加进来
        views.add(ViewFactory.getImageView(getActivity(), "Hello 3", "title3"));
        for (int i = 0; i < 4; i++) {
            views.add(ViewFactory.getImageView(getActivity(), "Hello " + i, "title" + i));
        }
        // 将第一个view添加进来
        views.add(ViewFactory.getImageView(getActivity(), "Hello 0", "title0"));

        final CycleViewPager cycleViewPager = (CycleViewPager) getChildFragmentManager()
                .findFragmentById(R.id.cycleViewPager);

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views);

        // 设置轮播
        cycleViewPager.setWheel(true);

        // 设置初始高度为屏幕的一半高度
        cycleViewPager.getView().getLayoutParams().height = getResources()
                .getDisplayMetrics().heightPixels / 3;

        cycleViewPager.setIndicatorCenter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        List<Catalog> catalogs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Catalog catalog = new Catalog();
            catalog.setIcon("http://www.baidu.com/img/bd_logo1.png");
            catalog.setName("test" + i);
            catalogs.add(catalog);
        }
        CatalogAdapter cataAdapter = new CatalogAdapter(catalogs, getActivity());
        catalogRv.setLayoutManager(gridLayoutManager);
        catalogRv.setAdapter(cataAdapter);
        mSwipeRefreshLayout.setColorScheme(R.color.navi_color,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPullDown();
            }
        });

    }

    private void onPullDown() {
        int current = mViewPager.getCurrentItem();
        RefreshListener listener = (RefreshListener) mViewPager.getAdapter().instantiateItem(mViewPager, current);
        listener.onPullDown();
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
        List<Catalog> mCatalogs = PreferenceUtils.getClassifyItems(getActivity().getApplicationContext());

        if (mCatalogs.size() == 1) {
            getActivity().finish();
            return;
        }
        //从sp获取数据 刷新viewpager项目
        if (resultCode == Activity.RESULT_OK) {
            setItems(mCatalogs);
            mAdapter.setmCategoryList(mCatalogs);
            mSlidingTabLayout.setViewPager(mViewPager);
            HandpickFragment fragment = (HandpickFragment) mViewPager.getAdapter().instantiateItem(mViewPager, 0);
            fragment.onPageSelected(0, items.get(0));
        }
    }

    public void onEvent(RefreshEvent refreshEvent) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public interface RefreshListener {
        void onPullDown();
    }
}

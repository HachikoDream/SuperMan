package com.dreamspace.superman.UI.Fragment.Index;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.QRCode.DensityUtil;
import com.dreamspace.superman.Common.ViewFactory;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Main.MainActivity;
import com.dreamspace.superman.UI.Adapters.CatalogAdapter;
import com.dreamspace.superman.UI.Adapters.IndexContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Index.HandpickFragment;
import com.dreamspace.superman.UI.View.DispatchChildScrollView;
import com.dreamspace.superman.UI.View.smartlayout.SmartTabLayout;
import com.dreamspace.superman.event.RefreshEvent;
import com.dreamspace.superman.model.Catalog;
import com.dreamspace.superman.model.api.CatalogRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import me.codeboy.android.cycleviewpager.CycleViewPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends BaseLazyFragment {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.sliding_layout)
    SmartTabLayout mSlidingTabLayout;
    @Bind(R.id.index_swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.index_scrollview)
    DispatchChildScrollView scrollView;
    @Bind(R.id.catalog_failed_tv)
    TextView cataLoadFailedTv;
    private IndexContainerPagerAdapter mAdapter;
    private List<Catalog> items;
    private static final int SINGLE_PAGE_CATA_NUM = 8;//每页分类的数量
    private static final int SINGLE_PAGE_COL_NUM = 4;//每页分类的列数
    private static final int ALL_CATALOG_PAGE = 1;
    private static final int ALL_CATALOG_PAGINATION = 100;//保证获取到所有的分类信息
    private CycleViewPager actCycleViewPager;
    private CycleViewPager cataCycleViewPager;

    public IndexFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onFirstUserVisible() {
        fetchCatalogs();
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
        cataCycleViewPager = (CycleViewPager) getChildFragmentManager()
                .findFragmentById(R.id.catalog_cycleViewPager);
        actCycleViewPager = (CycleViewPager) getChildFragmentManager()
                .findFragmentById(R.id.cycleViewPager);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int tabHeight = DensityUtil.dp2px(getActivity(), 40);
                int bottom_height = DensityUtil.dp2px(getActivity(), 60);
                AbsActivity activity = (AbsActivity) getActivity();
                int toobar_height = activity.getSupportActionBar().getHeight();
                int statusbar_height = getStatusBarHeight();
                int screenHeight = getResources()
                        .getDisplayMetrics().heightPixels;
                int listview_height = screenHeight - tabHeight - toobar_height - statusbar_height - bottom_height;
                ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                params.height = listview_height;
                mViewPager.setLayoutParams(params);
            }
        });
        setActViewPager();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setCatalogViewPager(List<Catalog> catalogs) {
        int totalCataNums = catalogs.size();
        int FullPageNum = totalCataNums / SINGLE_PAGE_CATA_NUM;
        int leftCataNum = totalCataNums - FullPageNum * SINGLE_PAGE_CATA_NUM;
        int totalPageNum = FullPageNum + (leftCataNum > 0 ? 1 : 0);
        List<View> catalogRvs = new ArrayList<>();
        for (int i = 0; i < totalPageNum; i++) {
            int start = i * SINGLE_PAGE_CATA_NUM;
            int next = (i + 1) * SINGLE_PAGE_CATA_NUM;
            int end = next > totalCataNums ? totalCataNums : next;
            List<Catalog> cataInPage = catalogs.subList(start, end);
            RecyclerView catalogRv = (RecyclerView) LayoutInflater.from(getActivity()).inflate(R.layout.catalog_viewpager_item, null);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SINGLE_PAGE_COL_NUM);
            CatalogAdapter cataAdapter = new CatalogAdapter(cataInPage, getActivity());
            catalogRv.setLayoutManager(gridLayoutManager);
            catalogRv.setAdapter(cataAdapter);
            catalogRvs.add(catalogRv);
        }
        cataCycleViewPager.setCycle(false);
        cataCycleViewPager.setData(catalogRvs);
        cataCycleViewPager.setWheel(false);
        cataCycleViewPager.setIndicatorCenter();
        MainActivity parent = (MainActivity) getActivity();
        cataCycleViewPager.disableParentViewPagerTouchEvent(parent.getmViewPager());
    }

    /**
     * 从网络上获取分类
     */
    private void fetchCatalogs() {
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getTestService(getActivity()).getAllCatalogs(ALL_CATALOG_PAGE, ALL_CATALOG_PAGINATION, new Callback<CatalogRes>() {
                @Override
                public void success(CatalogRes catalogRes, Response response) {
                    if (catalogRes != null && catalogRes.getStatus().equals(CatalogRes.SUCCESS_STATE)) {
                        setCatalogState(true);
                        setCatalogViewPager(catalogRes.getCatalogs());
                    } else {
                        setCatalogState(false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    setCatalogState(false);
                }
            });
        } else {
            setCatalogState(false);
        }
    }

    /**
     * 设置分类信息是否加载成功，成功则正常显示，失败则显示失败布局。点击布局可以重新加载
     *
     * @param state 表示分类信息是否加载成功
     */
    private void setCatalogState(boolean state) {
        cataLoadFailedTv.setVisibility(View.GONE);
        cataCycleViewPager.getView().setVisibility(View.GONE);
        if (state) {
            cataCycleViewPager.getView().setVisibility(View.VISIBLE);
        } else {
            cataLoadFailedTv.setVisibility(View.VISIBLE);
            cataLoadFailedTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchCatalogs();
                }
            });
        }
    }

    private void setActViewPager() {
        List<View> views = new ArrayList<View>();

        // 将最后一个view添加进来
        views.add(ViewFactory.getImageView(getActivity(), "Hello 3", "title3"));
        for (int i = 0; i < 4; i++) {
            views.add(ViewFactory.getImageView(getActivity(), "Hello " + i, "title" + i));
        }
        // 将第一个view添加进来
        views.add(ViewFactory.getImageView(getActivity(), "Hello 0", "title0"));

        // 设置循环，在调用setData方法前调用
        actCycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        actCycleViewPager.setData(views);

        // 设置轮播
        actCycleViewPager.setWheel(true);

        // 设置初始高度为屏幕的1/3
        int screenHeight = getResources()
                .getDisplayMetrics().heightPixels;
        actCycleViewPager.getView().getLayoutParams().height = screenHeight / 3;
        actCycleViewPager.setIndicatorCenter();
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
        if (refreshEvent.type == RefreshEvent.INDEX)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    public interface RefreshListener {
        void onPullDown();
    }

    public DispatchChildScrollView getScrollView() {
        return scrollView;
    }
}

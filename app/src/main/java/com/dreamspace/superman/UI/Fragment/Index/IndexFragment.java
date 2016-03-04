package com.dreamspace.superman.UI.Fragment.Index;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.culiu.mhvp.core.MagicHeaderUtils;
import com.culiu.mhvp.core.MagicHeaderViewPager;
import com.culiu.mhvp.core.tabs.com.astuetz.PagerSlidingTabStrip;
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
import com.dreamspace.superman.UI.Adapters.LessonListContainerPagerAdapter;
import com.dreamspace.superman.UI.Adapters.SmContainerPagerAdapter;
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

    @Bind(R.id.magic_vp_container)
    LinearLayout magicVpContainer;
    private LessonListContainerPagerAdapter mAdapter;
    private MagicHeaderViewPager mMagicHeaderViewPager;
    private static final int SINGLE_PAGE_CATA_NUM = 8;//每页分类的数量
    private static final int SINGLE_PAGE_COL_NUM = 4;//每页分类的列数
    private static final int ALL_CATALOG_PAGE = 1;
    private static final int ALL_CATALOG_PAGINATION = 100;//保证获取到所有的分类信息
    private CycleViewPager actCycleViewPager;
    private CycleViewPager cataCycleViewPager;
    private TextView cataLoadFailedTv;

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
        mMagicHeaderViewPager = new MagicHeaderViewPager(getActivity()) {
            @Override
            protected void initTabsArea(LinearLayout container) {
                ViewGroup tabsArea = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_layout, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MagicHeaderUtils.dp2px(getActivity(), 48));
                container.addView(tabsArea, lp);
                PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) tabsArea.findViewById(R.id.tabs);
                pagerSlidingTabStrip.setTextColor(Color.BLACK);
                pagerSlidingTabStrip.setBackgroundColor(Color.WHITE);
                setTabsArea(tabsArea);
                setPagerSlidingTabStrip(pagerSlidingTabStrip);
            }
        };
        mAdapter = new LessonListContainerPagerAdapter(getChildFragmentManager());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        magicVpContainer.addView(mMagicHeaderViewPager, lp);
        mMagicHeaderViewPager.setPagerAdapter(mAdapter);
        addHeader();
    }

    private void addHeader() {
        View container = LayoutInflater.from(getActivity()).inflate(R.layout.indexfragment_header, null);
        actCycleViewPager = (CycleViewPager) container.findViewById(R.id.act_cycleViewPager);
        cataCycleViewPager = (CycleViewPager) container.findViewById(R.id.catalog_cycleViewPager);
        cataLoadFailedTv = (TextView) container.findViewById(R.id.catalog_failed_tv);
        setActViewPager();
        mMagicHeaderViewPager.addHeaderView(container);
        mMagicHeaderViewPager.setHeaderTallerThanScreen(true);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchCatalogs();
    }

    /**
     * 设置分类信息是否加载成功，成功则正常显示，失败则显示失败布局。点击布局可以重新加载
     *
     * @param state 表示分类信息是否加载成功
     */
    private void setCatalogState(boolean state) {
        cataLoadFailedTv.setVisibility(View.GONE);
//        cataCycleViewPager.getView().setVisibility(View.GONE);
        if (state) {
//            cataCycleViewPager.getView().setVisibility(View.VISIBLE);
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
        actCycleViewPager.getLayoutParams().height = screenHeight / 3;
        actCycleViewPager.setIndicatorCenter();
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_index;
    }

}

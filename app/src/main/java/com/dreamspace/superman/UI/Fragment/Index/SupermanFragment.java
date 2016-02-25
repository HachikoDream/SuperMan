package com.dreamspace.superman.UI.Fragment.Index;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.Common.QRCode.DensityUtil;
import com.dreamspace.superman.Common.ViewFactory;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.SmContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.View.DispatchChildScrollView;
import com.dreamspace.superman.UI.View.smartlayout.SmartTabLayout;
import com.dreamspace.superman.event.RefreshEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.codeboy.android.cycleviewpager.CycleViewPager;

/**
 * Created by Wells on 2016/2/23.
 */
public class SupermanFragment extends BaseLazyFragment {
    @Bind(R.id.sliding_layout)
    SmartTabLayout slidingLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.superman_scrollview)
    DispatchChildScrollView supermanScrollview;
    @Bind(R.id.superman_swiperefresh)
    SwipeRefreshLayout supermanSwiperefresh;
    private CycleViewPager actCycleViewPager;
    private SmContainerPagerAdapter mAdapter;

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
        EventBus.getDefault().register(this);
        actCycleViewPager = (CycleViewPager) getChildFragmentManager()
                .findFragmentById(R.id.activity_cycleViewPager);
        mAdapter = new SmContainerPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(mAdapter);
        viewpager.setOffscreenPageLimit(2);
        slidingLayout.setViewPager(viewpager);
        slidingLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SmListFragment fragment = (SmListFragment) viewpager.getAdapter().instantiateItem(viewpager, position);
                fragment.onPageSelected(position, SmContainerPagerAdapter.SORT_TYPE[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setActViewPager();
        supermanSwiperefresh.setColorScheme(R.color.navi_color,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        supermanSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPullDown();
            }
        });
    }

    private void onPullDown() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        supermanScrollview.post(new Runnable() {
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
                ViewGroup.LayoutParams params = viewpager.getLayoutParams();
                params.height = listview_height;
                viewpager.setLayoutParams(params);
            }
        });

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragement_superman;
    }


    public void onEvent(RefreshEvent event) {

    }


    public DispatchChildScrollView getScrollView() {
        return supermanScrollview;
    }
}

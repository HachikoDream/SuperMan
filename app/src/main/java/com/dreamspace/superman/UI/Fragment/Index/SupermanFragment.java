package com.dreamspace.superman.UI.Fragment.Index;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.culiu.mhvp.core.MagicHeaderUtils;
import com.culiu.mhvp.core.MagicHeaderViewPager;
import com.culiu.mhvp.core.tabs.com.astuetz.PagerSlidingTabStrip;
import com.dreamspace.superman.Common.ViewFactory;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.SmContainerPagerAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.codeboy.android.cycleviewpager.CycleViewPager;

/**
 * Created by Wells on 2016/2/23.
 */
public class SupermanFragment extends BaseLazyFragment {

    @Bind(R.id.magic_vp_container)
    LinearLayout magicVpContainer;
    private SmContainerPagerAdapter mAdapter;
    private MagicHeaderViewPager mMagicHeaderViewPager;
    private CycleViewPager actCycleViewPager;

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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        magicVpContainer.addView(mMagicHeaderViewPager, lp);
        mAdapter = new SmContainerPagerAdapter(getChildFragmentManager());
        mMagicHeaderViewPager.setPagerAdapter(mAdapter);
        addHeader();
    }

    private void addHeader() {
        View container = LayoutInflater.from(getActivity()).inflate(R.layout.smfragment_header, null);
        actCycleViewPager = (CycleViewPager) container.findViewById(R.id.act_cyclevp);
        setActViewPager();
        mMagicHeaderViewPager.addHeaderView(container);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragement_superman;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

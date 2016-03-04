package com.dreamspace.superman.UI.Fragment.Index;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.culiu.mhvp.core.InnerListView;
import com.culiu.mhvp.core.InnerScroller;
import com.culiu.mhvp.core.InnerScrollerContainer;
import com.culiu.mhvp.core.OuterScroller;
import com.culiu.mhvp.integrated.ptr.PullToRefreshInnerListView;
import com.culiu.mhvp.integrated.ptr.pulltorefresh.PullToRefreshBase;
import com.dreamspace.superman.Common.QRCode.DensityUtil;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;

/**
 * Created by Wells on 2016/2/25.
 */
public class LessonListFragment extends Fragment implements InnerScrollerContainer,PullToRefreshBase.OnRefreshListener2{

    protected PullToRefreshInnerListView mPullToRefreshListView;
    private BasisAdapter mAdapter;
    public final static int LOAD = 233;
    public final static int ADD = 234;
    public String type;//课程排序依据 date/heat
    public static final String SORT_BY_HEAT = "heat";
    public static final String SORT_BY_DATE = "date";
    protected View viewThis;
    protected InnerListView mListView;
    protected OuterScroller mOuterScroller;
    protected int mIndex;
    @Override
    public void setOuterScroller(OuterScroller outerScroller, int myPosition) {
        if (outerScroller == mOuterScroller && myPosition == mIndex) {
            return;
        }
        mOuterScroller = outerScroller;
        mIndex = myPosition;

        if (getInnerScroller() != null) {
            getInnerScroller().register2Outer(mOuterScroller, mIndex);
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mListView != null && viewThis != null) {
            if (viewThis.getParent() != null) {
                ((ViewGroup) viewThis.getParent()).removeView(viewThis);
            }
            return viewThis;
        }
        viewThis = inflater.inflate(R.layout.base_ptr_listview, null);
        mPullToRefreshListView = (PullToRefreshInnerListView) viewThis.findViewById(R.id.pull_refresh_inner_listview);
        mPullToRefreshListView.setScaleRefreshing(0.268f);

        if (mPullToRefreshListView != null) {
            mPullToRefreshListView.setOnRefreshListener(this);
        }
        mListView = mPullToRefreshListView.getRefreshableView();

        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mListView.setDividerHeight(DensityUtil.dp2px(getActivity(),10));
        mListView.register2Outer(mOuterScroller, mIndex);
        mAdapter = new IndexAdapter(getActivity());
        mPullToRefreshListView.setAdapter(mAdapter);
        getInitDate();
        return viewThis;
    }

    @Override
    public InnerScroller getInnerScroller() {
        return mListView;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    public void getInitDate() {


    }
}

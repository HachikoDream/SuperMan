package com.dreamspace.superman.UI.Fragment.Base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Adapters.OrderAdapter;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Course;
import com.dreamspace.superman.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public abstract  class BaseOrderListFragment extends BaseFragment {
    private LoadMoreListView moreListView;
    private OrderAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Order> mOrders = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.fragment_one;
    }
    @Override
    public void initViews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_id);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPullDown();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        moreListView = (LoadMoreListView) view.findViewById(R.id.load_more_lv);
        moreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onPullUp();
                moreListView.setLoading(false);

            }
        });
    }

    @Override
    public void initDatas() {
        mAdapter=new OrderAdapter(getActivity(),mOrders);
        moreListView.setAdapter(mAdapter);
        getInitData();
    }


    public abstract void onPullUp();

    public abstract void onPullDown();

    public abstract void getInitData();

    public void setmCourses(List<Order> mOrders) {
        this.mOrders = mOrders;
    }

    public void refreshDate(List<Order> mOrders) {
        mAdapter.setmEntities(mOrders);
        mAdapter.notifyDataSetChanged();
    }

}

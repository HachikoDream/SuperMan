package com.dreamspace.superman.UI.Fragment.Base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Adapters.OrderAdapter;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Order;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public abstract  class BaseLazyOrderFragment<T> extends BaseLazyFragment {
    @Bind(R.id.load_more_lv)
    LoadMoreListView moreListView;
    private BasisAdapter mAdapter;
    @Bind(R.id.swiperefresh_id)
    SwipeRefreshLayout mSwipeRefreshLayout;
    public final static int LOAD=233;
    public final static int ADD=234;

    @Override
    protected void onFirstUserVisible() {
        getInitData();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }
    public void onPullUpFinished(){
        moreListView.setLoading(false);
    }
    public void onPullDownFinished(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPullDown();
            }
        });
        moreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onPullUp();
            }
        });
        initDatas();
    }
    public void initDatas() {
        if(getOrderAdapter()==null){
            mAdapter=new OrderAdapter(getActivity());
        }else{
            mAdapter=getOrderAdapter();
        }
        moreListView.setAdapter(mAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemPicked((T) mAdapter.getItem(position), position);
                Log.i("INFO", "position:  " + position);
            }
        });

    }
    //为了解决达人订单列表与普通用户列表的差异化
    public BasisAdapter getOrderAdapter(){
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.base_list_fragment;
    }
    protected abstract void getInitData();

    protected abstract void onItemPicked(T item, int position);

    protected abstract void onPullUp();

    protected abstract void onPullDown();
    public void refreshDate(List<T> mEntities,int type) {
        switch (type){
            case LOAD:
                mAdapter.setmEntities(mEntities);
                break;
            case ADD:
                mAdapter.addEntities(mEntities);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }
}

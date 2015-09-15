package com.dreamspace.superman.UI.Fragment.Base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.View.LoadMoreListView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public abstract class BaseLazyCourseFragment<T> extends BaseLazyFragment {
    @Bind(R.id.load_more_lv)
    LoadMoreListView moreListView;
    private BasisAdapter mAdapter;
    @Bind(R.id.swiperefresh_id)
    SwipeRefreshLayout mSwipeRefreshLayout;


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

    @Override
    protected View getLoadingTargetView() {
        return null;
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
//        try {
//            Constructor c=mAClass.getConstructor(Context.class);
//            mAdapter= (BasisAdapter) c.newInstance(getActivity());
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (java.lang.InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        mAdapter=new IndexAdapter(getActivity());
        moreListView.setAdapter(mAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemPicked((T) mAdapter.getItem(position), position);
                Log.i("INFO", "position:  " + position);
            }
        });

    }

    protected abstract void getInitData();

    protected abstract void onItemPicked(T item, int position);

    protected abstract void onPullUp();

    protected abstract void onPullDown();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.base_list_fragment;
    }
    public void onPullUpFinished(){
        moreListView.setLoading(false);
    }
    public void onPullDownFinished(){
        mSwipeRefreshLayout.setRefreshing(false);
    }
    public void refreshDate(List<T> mEntities) {
        mAdapter.setmEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }
    
}

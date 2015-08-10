package com.dreamspace.superman.UI.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.MyCourseAdapter;
import com.dreamspace.superman.UI.View.LoadMoreListView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Administrator on 2015/8/10 0010.
 */
public abstract class BaseListAct<T> extends AbsActivity {
    private LoadMoreListView moreListView;
    private BasisAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Class<? extends BasisAdapter> mAClass;

    public BaseListAct(Class mAClass) {
       this.mAClass=mAClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDates();
    }

    private void initDates() {
        try {
            Constructor c=mAClass.getConstructor(Context.class);
            mAdapter= (BasisAdapter) c.newInstance(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        moreListView.setAdapter(mAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemPicked((T) mAdapter.getItem(position), position);
                Log.i("INFO", "position:  " + position);
            }
        });
        getInitData();
    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.base_list_activity);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh_id);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPullDown();
            }
        });
        moreListView = (LoadMoreListView)findViewById(R.id.load_more_lv);
        moreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onPullUp();
            }
        });
    }
    public void onPullUpFinished(){
        moreListView.setLoading(false);
    }
    public void onPullDownFinished(){
        mSwipeRefreshLayout.setRefreshing(false);
    }
    public  void onItemPicked(T mEntity,int position){

    }


    public abstract void onPullUp();

    public abstract void onPullDown();

    public abstract void getInitData();

    public void refreshDate(List<T> mEntities) {
        mAdapter.setmEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }
}

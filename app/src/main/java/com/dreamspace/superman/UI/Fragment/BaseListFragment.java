package com.dreamspace.superman.UI.Fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/1 0001.
 */
public abstract class BaseListFragment extends BaseFragment {
    private LoadMoreListView moreListView;
    private IndexAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Course> mCourses = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.fragment_one;
    }

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
        mAdapter = new IndexAdapter(mCourses, getActivity());
        moreListView.setAdapter(mAdapter);
        getInitData();
    }

    public abstract void onPullUp();

    public abstract void onPullDown();

    public abstract void getInitData();

    public void setmCourses(List<Course> mCourses) {
        this.mCourses = mCourses;
    }

    public void refreshDate(List<Course> mCourses) {
        mAdapter.setmEntities(mCourses);
        mAdapter.notifyDataSetChanged();
    }



}

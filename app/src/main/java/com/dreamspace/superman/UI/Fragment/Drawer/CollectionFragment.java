package com.dreamspace.superman.UI.Fragment.Drawer;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.View.MenuLoadMoreListView;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends BaseFragment {

    private MenuLoadMoreListView mSwipeMenuListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private IndexAdapter mAdapter;


    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public void refreshDate(List<Course> mEntities) {
        mAdapter.setmEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }
    public void getInitData() {
        refreshDate(getTestData());
    }
    public List<Course> getTestData() {
        List<Course> mCourses = new ArrayList<>();
        Course course;
        for (int i = 0; i < 10; i++) {
            course = new Course();
            course.setCourseName("技术盲如何在创业初期搞定技术，低成本推出产品" + i);
            mCourses.add(course);
        }
        return mCourses;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collection;
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

            }
        });
        mAdapter = new IndexAdapter(getActivity());
        mSwipeMenuListView = (MenuLoadMoreListView) view.findViewById(R.id.listview);
        mSwipeMenuListView.setAdapter(mAdapter);
        mSwipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                Log.i("SWIP", "position:" + position);
                // false : close the menu; true : not close the menu
                mAdapter.removeItem(position);
                return false;
            }
        });
        getInitData();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(R.color.delete_bg);
                // set item width
                deleteItem.setWidth(dp2px(60));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        mSwipeMenuListView.setMenuCreator(creator);
        mSwipeMenuListView.setOnLoadMoreListener(new MenuLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mSwipeMenuListView.setLoading(false);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void initDatas() {

    }

}

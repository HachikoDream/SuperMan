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
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.View.MenuLoadMoreListView;
import com.dreamspace.superman.model.Lesson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CollectionFragment extends BaseLazyFragment {

    @Bind(R.id.listview)
    MenuLoadMoreListView mSwipeMenuListView;
    @Bind(R.id.swiperefresh_id)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private IndexAdapter mAdapter;
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public void refreshDate(List<Lesson> mEntities) {
        mAdapter.setmEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }
    public void getInitData() {
        refreshDate(getTestData());
    }
    public List<Lesson> getTestData() {
        List<Lesson> mLessons = new ArrayList<>();
        Lesson lesson;
        for (int i = 0; i < 10; i++) {
            lesson = new Lesson();
            lesson.setCourseName("技术盲如何在创业初期搞定技术，低成本推出产品" + i);
            mLessons.add(lesson);
        }
        return mLessons;
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
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        mAdapter = new IndexAdapter(getActivity());
        mSwipeMenuListView.setAdapter(mAdapter);
        mSwipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                Log.i("SWIP", "position:" + position);
                // false : close the menu; true : not close the menu
//                View view=mSwipeMenuListView.getChildAt(position);
//                Log.i("INFO","position: "+position);
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
    protected int getContentViewLayoutID() {
        return R.layout.fragment_collection;
    }
}

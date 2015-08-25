package com.dreamspace.superman.UI.Activity.Main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.ConListAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.View.MenuLoadMoreListView;
import com.dreamspace.superman.model.ConList;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

public class ConListActivity extends AbsActivity {

    private MenuLoadMoreListView mSwipeMenuListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConListAdapter mAdapter;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_con_list);
    }

    @Override
    protected void prepareDatas() {

    }

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
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

            }
        });
        mAdapter=new ConListAdapter(this);
        mSwipeMenuListView=(MenuLoadMoreListView)findViewById(R.id.listview);
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
                        ConListActivity.this);
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
    }
    public void refreshDate(List<ConList> mEntities) {
        mAdapter.setmEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }
    public void getInitData() {
        refreshDate(getTestData());
    }
    public List<ConList> getTestData() {
        List<ConList> mConLists = new ArrayList<>();
        ConList mConList;
        for (int i = 0; i < 10; i++) {
            mConList = new ConList();
            mConList.setLatestContent("技术盲如何在创业初期搞定技术，低成本推出产品" + i);
            mConLists.add(mConList);
        }
        return mConLists;
    }

}

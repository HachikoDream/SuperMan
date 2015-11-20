package com.dreamspace.superman.UI.Activity.Main;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.ConverListLoader;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.ConListAdapter;
import com.dreamspace.superman.UI.View.MenuLoadMoreListView;
import com.dreamspace.superman.event.DbChangeEvent;
import com.ds.greendao.Conversation;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ConListActivity extends AbsActivity implements LoaderManager.LoaderCallbacks<List<Conversation>> {

    private MenuLoadMoreListView mSwipeMenuListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConListAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private boolean isVisible=false;
    private boolean shouldRefresh=false;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_con_list);
    }

    @Override
    protected void prepareDatas() {
        EventBus.getDefault().register(this);
    }

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    @Override
    protected void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_id);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        mAdapter = new ConListAdapter(this);
        mSwipeMenuListView = (MenuLoadMoreListView) findViewById(R.id.listview);
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
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int memberId = mAdapter.getItem(position).getMemberId().intValue();
                String memberName=mAdapter.getItem(position).getMemberName();
                Bundle b = new Bundle();
                b.putString(Constant.MEMBER_ID, String.valueOf(memberId));
                b.putString(Constant.MEMBER_NAME,memberName);
                readyGo(ChatActivity.class, b);

            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    public void refreshDate(List<Conversation> mEntities) {
        mAdapter.setmEntities(mEntities);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<List<Conversation>> onCreateLoader(int id, Bundle args) {
        return new ConverListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Conversation>> loader, List<Conversation> data) {
        if (data == null && data.isEmpty()) {
            toggleShowEmpty(true, "暂无联系人", null);
        } else {
            toggleShowEmpty(false, null, null);
            mAdapter.refreshDate(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Conversation>> loader) {
        mAdapter.refreshDate(new ArrayList<Conversation>());
    }

    public void onEvent(DbChangeEvent event) {
        Log.i("EVENTBUS","Msg come");
       if(isVisible){
           getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
       }else{
           shouldRefresh=true;
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible=true;
        if(shouldRefresh){
            shouldRefresh=false;
            getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible=false;

    }
}

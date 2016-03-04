package com.dreamspace.superman.UI.Fragment.Base;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Index.IndexFragment;
import com.dreamspace.superman.UI.View.LoadMoreListView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public abstract class BaseIndexFragment<T> extends BaseLessonFragment {
    @Bind(R.id.load_more_lv)
    LoadMoreListView moreListView;
    private BasisAdapter mAdapter;
    public final static int LOAD = 233;
    public final static int ADD = 234;

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
        return moreListView;
    }

    @Override
    protected void initViewsAndEvents() {
        moreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onPullUp();
            }
        });
        moreListView.setIn(true);
        initDatas();
    }

    public void initDatas() {
        mAdapter = new IndexAdapter(getActivity());
        moreListView.setAdapter(mAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemPicked((T) mAdapter.getItem(position), position);
                Log.i("INFO", "position:  " + position);
            }
        });
//        moreListView.setScrollToTopListener(new LoadMoreListView.OnScrollToTopListener() {
//            @Override
//            public void onScrollToTop() {
//                IndexFragment indexFragment = (IndexFragment) getParentFragment();
////                indexFragment.getScrollView().setEnableScroll(true);
//            }
//        });

    }

    protected abstract void getInitData();

    protected abstract void onItemPicked(T item, int position);

    protected abstract void onPullUp();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.base_listview;
    }

    public void onPullUpFinished() {
        moreListView.setLoading(false);
    }


    public void refreshDate(List<T> mEntities, int type) {
        switch (type) {
            case LOAD:
                mAdapter.setmEntities(mEntities);
                break;
            case ADD:
                mAdapter.addEntities(mEntities);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    public LoadMoreListView getMoreListView() {
        return moreListView;
    }
}

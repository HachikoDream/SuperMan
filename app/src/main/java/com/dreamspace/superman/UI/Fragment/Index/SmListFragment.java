package com.dreamspace.superman.UI.Fragment.Index;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Adapters.SmListAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLessonFragment;
import com.dreamspace.superman.UI.Fragment.Index.IndexFragment;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Catalog;
import com.dreamspace.superman.model.api.SmInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public class SmListFragment extends BaseLazyFragment {
    @Bind(R.id.load_more_lv)
    LoadMoreListView moreListView;
    private BasisAdapter mAdapter;
    public final static int LOAD = 233;
    public final static int ADD = 234;
    public String type;//达人排序依据 date/heat
    public static final String SORT_BY_HEAT = "heat";
    public static final String SORT_BY_DATE = "date";
    private boolean onFirst;
    private boolean firstLoad = true;//只在第一次滑动时加载数据 避免每次滑动到某一分类都重新从网络上进行获取

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
        mAdapter = new SmListAdapter(getActivity());
        moreListView.setAdapter(mAdapter);
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemPicked((SmInfo) mAdapter.getItem(position), position);
                Log.i("INFO", "position:  " + position);
            }
        });
        moreListView.setScrollToTopListener(new LoadMoreListView.OnScrollToTopListener() {
            @Override
            public void onScrollToTop() {
                SupermanFragment supermanFragment = (SupermanFragment) getParentFragment();
                supermanFragment.getScrollView().setEnableScroll(true);
            }
        });

    }

    protected void getInitData() {
        onFirst = true;
        if (type != null) {
            loadingDataWhenInit();
        }
    }

    protected void onItemPicked(SmInfo item, int position) {

    }

    protected void onPullUp() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.base_listview;
    }

    public void onPullUpFinished() {
        moreListView.setLoading(false);
    }


    public void refreshDate(List<SmInfo> mEntities, int type) {
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

    public void onPageSelected(int position, String catalog) {
        if (firstLoad) {
            firstLoad = false;
            type = catalog;
            if (onFirst) {
                loadingDataWhenInit();
            }
        }

    }

    private void loadingDataWhenInit() {
        List<SmInfo> smInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SmInfo info = new SmInfo();
            info.setName("Test" + i);
            info.setTags("Tags,Tags" + i);
            info.setCollection_count(i);
            smInfoList.add(info);
        }
        refreshDate(smInfoList, LOAD);
    }
}

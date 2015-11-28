package com.dreamspace.superman.UI.Fragment.Drawer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.OnRefreshListener;
import com.dreamspace.superman.UI.View.MenuLoadMoreListView;
import com.dreamspace.superman.event.CollectionChangeEvent;
import com.dreamspace.superman.model.api.CollectRes;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;
import com.google.gson.Gson;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CollectionFragment extends BaseLazyFragment {

    private static final int LOAD = 245;
    private static final int ADD = 246;
    @Bind(R.id.listview)
    MenuLoadMoreListView mSwipeMenuListView;
    @Bind(R.id.swiperefresh_id)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private IndexAdapter mAdapter;
    private int page = 1;
    private final int INIT_PAGE = 1;
    private ProgressDialog pd;

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public void refreshDate(List<LessonInfo> mEntities, int type) {
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

    @Override
    protected void onFirstUserVisible() {
        loadDataWhenInit();

    }

    public void loadingDataByPage(int page, final OnRefreshListener onRefreshListener) {

        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity().getApplicationContext()).getAllCollections(page, new Callback<SmLessonList>() {
                @Override
                public void success(SmLessonList smLessonList, Response response) {
                    if (smLessonList != null) {
                        onRefreshListener.onFinish(smLessonList.getLessons());
                    } else {
                        showToast(response.getReason());
                        onRefreshListener.onError();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    onRefreshListener.onError();
                    showInnerError(error);
                }
            });

        } else {
            onRefreshListener.onError();
            showNetWorkError();
        }

    }

    private void loadDataWhenInit() {
        page=1;
        if (NetUtils.isNetworkConnected(getActivity())) {
            toggleShowLoading(true, getString(R.string.common_loading_message));
            ApiManager.getService(getActivity().getApplicationContext()).getAllCollections(1, new Callback<SmLessonList>() {
                @Override
                public void success(SmLessonList smLessonList, Response response) {
                    toggleShowLoading(false, getString(R.string.common_loading_message));
                    List<LessonInfo> mLessons = smLessonList.getLessons();
                    if (mLessons.size() == 0) {
                        toggleShowEmpty(true, "您还没有收藏课程", null);
                    } else {
                        refreshDate(mLessons, LOAD);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    toggleShowLoading(false, getString(R.string.common_loading_message));
                    toggleShowError(true, getInnerErrorInfo(error), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadDataWhenInit();
                        }
                    });
                }
            });
        } else {
            toggleShowError(true, "暂无网络连接", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadDataWhenInit();
                }
            });
        }
    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(getActivity(), "", "正在提交请求", true, false);
        } else {
            pd.show();
        }
    }

    private void dismissPd() {
        if (pd != null) {
            pd.dismiss();
        }
    }


    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void initViewsAndEvents() {
        EventBus.getDefault().register(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDataByPage(INIT_PAGE, new OnRefreshListener<LessonInfo>() {
                    @Override
                    public void onFinish(List<LessonInfo> lessons) {
                        page = INIT_PAGE;
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (lessons.size() == 0) {
                            toggleShowEmpty(true, "您还没有收藏课程", null);
                        } else {
                            refreshDate(lessons, LOAD);
                        }

                    }

                    @Override
                    public void onError() {
                        page = INIT_PAGE;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
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
//                mAdapter.removeItem(position);
                deleteCollectionLessonsById(mAdapter.getItem(position).getLess_id(), position);
                return false;
            }
        });
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
                loadingDataByPage(++page, new OnRefreshListener<LessonInfo>() {
                    @Override
                    public void onFinish(List<LessonInfo> lessons) {
                        mSwipeMenuListView.setLoading(false);
                        if (lessons.size() == 0) {
                            --page;
                            showToast(getString(R.string.common_nomore_data));
                        } else {
                            refreshDate(lessons, ADD);
                        }
                    }

                    @Override
                    public void onError() {
                        mSwipeMenuListView.setLoading(false);
                    }
                });
            }
        });
        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LessonInfo item = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt(LessonDetailInfoActivity.LESSON_INFO, item.getLess_id());
                readyGo(LessonDetailInfoActivity.class, bundle);
            }
        });
    }

    private void deleteCollectionLessonsById(int id, final int position) {
        showPd();
        ApiManager.getService(getActivity().getApplicationContext()).deleteCollectionById(id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                dismissPd();
                mAdapter.removeItem(position);
            }

            @Override
            public void failure(RetrofitError error) {
                dismissPd();
                showInnerError(error);
            }
        });
    }
    public void onEvent(CollectionChangeEvent event){
        loadDataWhenInit();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_collection;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}

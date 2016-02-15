package com.dreamspace.superman.UI.Fragment.Index;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseIndexFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyCourseFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.IndexFragment;
import com.dreamspace.superman.UI.Fragment.OnRefreshListener;
import com.dreamspace.superman.event.LessonListRefreshEvent;
import com.dreamspace.superman.event.RefreshEvent;
import com.dreamspace.superman.model.Catalog;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;
import com.google.gson.Gson;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandpickFragment extends BaseIndexFragment<LessonInfo> implements IndexFragment.RefreshListener {
    public Catalog selfCatalog;
    public int page = 1;
    public final int INIT_PAGE = 1;
    private boolean onFirst = false;

    public HandpickFragment() {

    }


    @Override
    public void onPullUp() {
        loadingDataByPage(++page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> lessons) {
                if (lessons.size() == 0) {
                    page--;
                    showToast(getString(R.string.common_nomore_data));
                } else {
                    refreshDate(lessons, BaseLazyCourseFragment.ADD);
                }
                onPullUpFinished();
            }

            @Override
            public void onError() {
                onPullUpFinished();
            }
        });
    }

    @Override
    public void onPullDown() {
        loadingDataByPage(INIT_PAGE, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> lessons) {
                page = 1;
                refreshDate(lessons, BaseLazyCourseFragment.LOAD);
                onPullDownFinished();
            }


            @Override
            public void onError() {
                page = 1;
                onPullDownFinished();
            }
        });
    }

    private void onPullDownFinished() {
        EventBus.getDefault().post(RefreshEvent.newInstance());
    }

    @Override
    public void getInitData() {
        onFirst = true;
        if (selfCatalog != null) {
            loadingDataWhenInit();
        }

    }

    private void loadingDataWhenInit() {
        page = 1;
        toggleShowLoading(true, getString(R.string.common_loading_message));
        loadingDataByPage(INIT_PAGE, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> lessons) {
                toggleShowLoading(false, null);
                if (lessons.size() == 0) {
                    toggleShowEmpty(true, getString(R.string.common_empty_catalog_msg), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingDataWhenInit();
                        }
                    });
                } else {
                    refreshDate(lessons, BaseLazyCourseFragment.LOAD);
                }

            }

            @Override
            public void onError() {
                toggleShowError(true, getString(R.string.common_empty_msg), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDataWhenInit();
                    }
                });
            }
        });

    }

    @Override
    protected void onItemPicked(LessonInfo item, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("LESSON_INFO", item.getId());
        readyGo(LessonDetailInfoActivity.class, bundle);

    }

    public void loadingDataByPage(int page, final OnRefreshListener onRefreshListener) {

        if (NetUtils.isNetworkConnected(getActivity().getApplicationContext())) {
            if (selfCatalog.getId() == -1) {
                ApiManager.getService(getActivity().getApplicationContext()).getRecommentLessons(page, new Callback<SmLessonList>() {
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
                ApiManager.getService(getActivity().getApplicationContext()).getCoursesByCatalog(selfCatalog.getId(), page, new Callback<SmLessonList>() {
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
            }

        } else {
            onRefreshListener.onError();
            showNetWorkError();
        }

    }

    public void onPageSelected(int position, Catalog catalog) {
        Log.i("HP", "On PageSelected  " + this.toString());
        selfCatalog = catalog;
        if (onFirst) {
            loadingDataWhenInit();
        }
    }

    @Override
    public void getLessonInfo(LessonInfo mLessonInfo) {

    }

    public void onEvent(LessonListRefreshEvent event) {
        loadingDataWhenInit();
    }
}

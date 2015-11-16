package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.BaseListAct;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyCourseFragment;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmCourseListFragment extends BaseLazyCourseFragment<LessonInfo> {
    public static final String TAG="全部课程";
    private boolean onFirst=false;
    private boolean getLesson=false;
    private String mast_id;
    private int page = 1;
    private final int DEFAULT_PAGE = 1;

    public SmCourseListFragment() {
        setTAG(TAG);
    }


    @Override
    public void onPullUp() {
        getDataByPage(++page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                if (mEntities.size() == 0) {
                    showToast(getString(R.string.common_nomore_data));
                } else {
                    refreshDate(mEntities, BaseListAct.ADD);
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
        page = DEFAULT_PAGE;
        getDataByPage(page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                refreshDate(mEntities, BaseListAct.LOAD);
                onPullDownFinished();
            }

            @Override
            public void onError() {
                onPullDownFinished();
            }
        });
    }

    @Override
    public void getInitData() {
        onFirst=true;
        if(getLesson&& !CommonUtils.isEmpty(mast_id)){
           getLessonListOnInit();
        }
    }

    @Override
    protected void onItemPicked(LessonInfo item, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("LESSON_INFO", item.getId());
        readyGo(LessonDetailInfoActivity.class, bundle);

    }
    //按照页数获取达人发布的课程列表
    public void getDataByPage(int page, final OnRefreshListener<LessonInfo> listener) {
        if (NetUtils.isNetworkConnected(getActivity())) {
            if (!CommonUtils.isEmpty(mast_id)) {
                ApiManager.getService(getActivity().getApplicationContext()).getLessonsbyMid(mast_id, page,"on", new Callback<SmLessonList>() {
                    @Override
                    public void success(SmLessonList smLessonList, Response response) {
                        if (smLessonList != null) {
                            listener.onFinish(smLessonList.getLessons());
                        } else {
                            listener.onError();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showInnerError(error);
                        listener.onError();
                    }
                });
            } else {
                showToast("暂时查询不到您的课程信息");
            }

        } else {
            showNetWorkError();
            listener.onError();
        }
    }
    private void getLessonListOnInit(){
        toggleShowLoading(true,null);
        page = DEFAULT_PAGE;
        getDataByPage(page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                if(mEntities.size()==0){
                    toggleShowEmpty(true, "TA还没有发布过课程",null);
                }else{
                    toggleShowLoading(false,null);
                    refreshDate(mEntities, BaseListAct.LOAD);
                }
            }

            @Override
            public void onError() {
                toggleShowLoading(false,null);
            }
        });
    }

    @Override
    public void getLessonInfo(LessonInfo mLessonInfo) {
        getLesson=true;
        mast_id=mLessonInfo.getMast_id();
        if(onFirst){
            getLessonListOnInit();
        }
    }
}

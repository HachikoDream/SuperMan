package com.dreamspace.superman.UI.Fragment;


import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Adapters.CommentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyCommentFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Comment;
import com.dreamspace.superman.model.Lesson;
import com.dreamspace.superman.model.api.CommentList;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StudentCommentListFragment extends BaseLazyCommentFragment<Comment> {

    private final static String TAG = "课程评论";
    private int les_id;
    private final int INIT_PAGE = 1;
    private int current_page;
    private boolean onFirst = false;
    private boolean getLesson = false;


    public StudentCommentListFragment() {
        setTAG(TAG);
    }

    @Override
    protected void getInitData() {
        onFirst=true;
        if(getLesson=true){
            if(les_id!=-1){
                getCommentListForInit();
            }else {
                toggleNetworkError(true,null);
            }
        }
    }

    public void loadingDataByPage(int page, final OnRefreshListener onRefreshListener) {

        if (NetUtils.isNetworkConnected(getActivity().getApplicationContext())) {
            ApiManager.getService(getActivity().getApplicationContext()).getCommentsById(les_id, page, new Callback<CommentList>() {
                @Override
                public void success(CommentList commentList, Response response) {
                    if (commentList != null) {
                        onRefreshListener.onFinish(commentList.getComments());
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

    private void getCommentListForInit() {
        toggleShowLoading(true, getString(R.string.common_loading_message));
        loadingDataByPage(INIT_PAGE, new OnRefreshListener<Comment>() {
            @Override
            public void onFinish(List<Comment> mEntities) {
                if (mEntities != null && mEntities.size() == 0) {
                    toggleShowEmpty(true, getString(R.string.common_empty_msg), null);
                } else {
                    toggleShowLoading(false, null);
                    refreshDate(mEntities, BaseLazyCommentFragment.LOAD);
                }
            }

            @Override
            public void onError() {
                toggleShowError(true, getString(R.string.common_error_msg), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCommentListForInit();
                    }
                });
            }
        });
    }

    @Override
    protected void onItemPicked(Comment item, int position) {

    }

    //上拉加载
    @Override
    protected void onPullUp() {
        loadingDataByPage(++current_page, new OnRefreshListener<Comment>() {
            @Override
            public void onFinish(List<Comment> mEntities) {
                refreshDate(mEntities, BaseLazyCommentFragment.ADD);
                onPullUpFinished();
            }

            @Override
            public void onError() {
                onPullUpFinished();
            }
        });

    }

    //下拉刷新
    @Override
    protected void onPullDown() {
        current_page = 1;
        loadingDataByPage(current_page, new OnRefreshListener<Comment>() {
            @Override
            public void onFinish(List<Comment> mEntities) {
                refreshDate(mEntities, BaseLazyCommentFragment.LOAD);
                onPullDownFinished();
            }

            @Override
            public void onError() {
                onPullDownFinished();
            }
        });
    }

    @Override
    public void getLessonInfo(LessonInfo mLessonInfo) {
        {
            getLesson = true;
            if (onFirst) {
                if (mLessonInfo != null) {
                    les_id = mLessonInfo.getId();
                    getCommentListForInit();
                } else {
                    toggleNetworkError(true, null);
                }
            } else {
                if (mLessonInfo != null) {
                    les_id = mLessonInfo.getId();
                } else {
                    les_id = -1;
                }
            }
        }
    }
}

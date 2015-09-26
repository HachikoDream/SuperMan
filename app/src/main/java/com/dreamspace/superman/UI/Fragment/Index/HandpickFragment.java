package com.dreamspace.superman.UI.Fragment.Index;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyCourseFragment;
import com.dreamspace.superman.model.Catalog;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;
import com.google.gson.Gson;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandpickFragment extends BaseLazyCourseFragment<LessonInfo> {
    public Catalog selfCatalog;
    public int page=1;
    public final int INIT_PAGE=1;
    public HandpickFragment() {

    }


    @Override
    public void onPullUp() {
       loadingDataByPage(++page, new OnRefreshListener() {
           @Override
           public void onFinish(List<LessonInfo> lessons) {
               if(lessons.size()==0){
                   showToast(getString(R.string.common_nomore_data));
               }else{
                   refreshDate(lessons,BaseLazyCourseFragment.ADD);
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
      loadingDataByPage(INIT_PAGE, new OnRefreshListener() {
          @Override
          public void onFinish(List<LessonInfo> lessons) {
              refreshDate(lessons,BaseLazyCourseFragment.LOAD);
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
        if(selfCatalog==null){
            selfCatalog=new Catalog();
            selfCatalog.setIcon("TEST");
            selfCatalog.setName("精选");
            selfCatalog.setId(1);
        }
        loadingDataWhenInit();
    }
    private void loadingDataWhenInit(){
        toggleShowLoading(true,getString(R.string.common_loading_message));
        loadingDataByPage(INIT_PAGE, new OnRefreshListener() {
            @Override
            public void onFinish(List<LessonInfo> lessons) {
                toggleShowLoading(false,null);
                refreshDate(lessons,BaseLazyCourseFragment.LOAD);
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
        Bundle bundle=new Bundle();
        Gson gson=new Gson();
        bundle.putString("LESSON_INFO", gson.toJson(item));
        readyGo(LessonDetailInfoActivity.class,bundle);

    }

    public void loadingDataByPage(int page, final OnRefreshListener onRefreshListener) {

        if(NetUtils.isNetworkConnected(getActivity().getApplicationContext())){
            ApiManager.getService(getActivity().getApplicationContext()).getCoursesByCatalog(selfCatalog.getId(),page, new Callback<SmLessonList>() {
                @Override
                public void success(SmLessonList smLessonList, Response response) {
                    if(smLessonList!=null){
                        onRefreshListener.onFinish(smLessonList.getLessons());
//                          refreshDate(smLessonList.getLessons());
                    }else{
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

        }else{
            onRefreshListener.onError();
            showNetWorkError();
        }

    }

    public void onPageSelected(int position, Catalog catalog) {
        Log.i("INFO","On page selected");
        selfCatalog = catalog;
    }
   public interface OnRefreshListener{
       void onFinish(List<LessonInfo> lessons);
       void onError();
   }
}

package com.dreamspace.superman.UI.Fragment.Index;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyCourseFragment;
import com.dreamspace.superman.model.Catalog;
import com.dreamspace.superman.model.Lesson;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandpickFragment extends BaseLazyCourseFragment<LessonInfo> {
    public Catalog selfCatalog;
    public int page=0;
    public HandpickFragment() {

    }


    @Override
    public void onPullUp() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("onLoad", "on load complete");
                onPullUpFinished();
            }
        }, 3000);
    }

    @Override
    public void onPullDown() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onPullDownFinished();
            }
        }, 3000);
    }


    @Override
    public void getInitData() {
        Log.i("INFO","get init data");
//        Log.i("INFO", "TAG IS :" + selfCatalog.getName());
        if(selfCatalog==null){
            selfCatalog=new Catalog();
            selfCatalog.setIcon("TEST");
            selfCatalog.setName("精选");
            selfCatalog.setId(1);
        }
        loadingInitData();
    }

    @Override
    protected void onItemPicked(LessonInfo item, int position) {

    }

    public void loadingInitData() {
        if(NetUtils.isNetworkConnected(getActivity().getApplicationContext())){
            ApiManager.getService(getActivity().getApplicationContext()).getCoursesByCatalog(selfCatalog.getId(),1, new Callback<SmLessonList>() {
                @Override
                public void success(SmLessonList smLessonList, Response response) {
                    if(smLessonList!=null){
                          refreshDate(smLessonList.getLessons());
                    }else{
                        showToast(response.getReason());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });

        }else{
            showNetWorkError();
        }

    }

    public void onPageSelected(int position, Catalog catalog) {
        Log.i("INFO","On page selected");
        selfCatalog = catalog;
    }

}

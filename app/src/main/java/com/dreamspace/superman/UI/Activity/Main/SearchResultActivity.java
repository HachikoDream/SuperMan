package com.dreamspace.superman.UI.Activity.Main;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.BaseListAct;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.OnRefreshListener;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchResultActivity extends BaseListAct<LessonInfo> {

    private String query_content;
    private final int INIT_PAGE=1;
    private ProgressDialog pd;
    private int page;
    public SearchResultActivity() {
        super(IndexAdapter.class);
    }
    private void showPd(){
        if(pd==null){
            pd=ProgressDialog.show(this,"","正在获取数据，请稍后",true,false);
        }else {
            pd.show();
        }

    }
    private void dismissPd(){
        if(pd!=null){
            pd.dismiss();
        }
    }
    @Override
    protected void prepareDatas() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query_content= intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    public void onPullUp() {
      loadSearchResultByPage(++page, new OnRefreshListener<LessonInfo>() {
          @Override
          public void onFinish(List<LessonInfo> mEntities) {
              onPullUpFinished();
              if(mEntities.size()==0){
                  --page;
                  showToast("没有更多数据");
              }else{
                  refreshDate(mEntities,BaseListAct.ADD);
              }
          }

          @Override
          public void onError() {
            onPullUpFinished();
          }
      });
    }

    @Override
    public void onPullDown() {
       page=1;
        loadSearchResultByPage(page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                refreshDate(mEntities,BaseListAct.LOAD);
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
        toggleShowLoading(true,null);
        loadSearchResultByPage(INIT_PAGE, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                if(mEntities.size()==0){
                    toggleShowEmpty(true,"暂无相关课程",null);
                }else {
                    toggleShowLoading(false,null);
                    refreshDate(mEntities,BaseListAct.LOAD);
                }
            }

            @Override
            public void onError() {
                 toggleShowError(true,getString(R.string.common_error_msg),null);
            }
        });
    }

    /**
     * get info online by the query_string
     */
    private void loadSearchResultByPage(int page, final OnRefreshListener<LessonInfo> listener) {

        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).searchLessons(query_content,INIT_PAGE,new Callback<SmLessonList>() {
                @Override
                public void success(SmLessonList smLessonList, Response response) {
                    if(smLessonList!=null){
                          listener.onFinish(smLessonList.getLessons());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    listener.onError();
                    showInnerError(error);
                }

            });
        }else {
            showNetWorkError();
            listener.onError();
        }
    }

    @Override
    public void onItemPicked(LessonInfo mEntity, int position) {
        Bundle b=new Bundle();
        b.putInt("LESSON_INFO",mEntity.getId());
        readyGo(LessonDetailInfoActivity.class,b);
    }
}

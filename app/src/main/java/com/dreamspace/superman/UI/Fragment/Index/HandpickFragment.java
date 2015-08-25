package com.dreamspace.superman.UI.Fragment.Index;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyListFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandpickFragment extends BaseLazyListFragment<Course> {
    public static String TAG = "精选";

    public HandpickFragment() {
//        super(IndexAdapter.class);
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
                },3000);
    }

    @Override
    public void onItemPicked(Course mEntity, int position) {
        Log.i("INFO", mEntity.toString());
    }

    @Override
    public void getInitData() {
        Log.i("INFO","TAG IS :"+TAG);
      refreshDate(getTestData());
    }
    public List<Course> getTestData() {
        List<Course> mCourses = new ArrayList<>();
        Course course;
        for (int i = 0; i < 10; i++) {
            course = new Course();
            course.setCourseName("技术盲如何在创业初期搞定技术，低成本推出产品" + i);
            mCourses.add(course);
        }
        return mCourses;
    }
    public void onPageSelected(int position,String name){
          TAG=name;
    }

}

package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends BaseListFragment {

    private final String TAG="IT技术";

    public TwoFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }

    @Override
    public void initDatas() {
        super.initDatas();
    }

    @Override
    public void onPullUp() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("onLoad", "on load complete");
            }
        }, 3000);
    }

    @Override
    public void onPullDown() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);
    }

    @Override
    public void getInitData() {
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
}

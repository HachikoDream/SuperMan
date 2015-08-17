package com.dreamspace.superman.UI.Fragment.Index;

import android.os.Handler;
import android.util.Log;

import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/13 0013.
 */
public class BallFragment extends BaseListFragment<Course> {
    private final static String TAG = "球类运动";

    public BallFragment() {
        super(IndexAdapter.class);
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
        }, 3000);
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

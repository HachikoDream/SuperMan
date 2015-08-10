package com.dreamspace.superman.UI.Fragment.Index;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends BaseListFragment<Course> {

    private final static String TAG="健身";
    public ThreeFragment() {
        super(IndexAdapter.class);
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

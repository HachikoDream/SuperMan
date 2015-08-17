package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.BasisAdapter;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmCourseListFragment extends BaseListFragment<Course> {
    public static final String TAG="全部课程";
    public SmCourseListFragment() {
        super(IndexAdapter.class);
        setTAG(TAG);
    }


    @Override
    public void onPullUp() {

    }

    @Override
    public void onPullDown() {

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

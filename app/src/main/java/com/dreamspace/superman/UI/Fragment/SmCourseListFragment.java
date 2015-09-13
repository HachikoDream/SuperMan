package com.dreamspace.superman.UI.Fragment;


import android.support.v4.app.Fragment;

import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.model.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmCourseListFragment extends BaseListFragment<Lesson> {
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

    public List<Lesson> getTestData() {
        List<Lesson> mLessons = new ArrayList<>();
        Lesson lesson;
        for (int i = 0; i < 10; i++) {
            lesson = new Lesson();
            lesson.setCourseName("技术盲如何在创业初期搞定技术，低成本推出产品" + i);
            mLessons.add(lesson);
        }
        return mLessons;
    }
}

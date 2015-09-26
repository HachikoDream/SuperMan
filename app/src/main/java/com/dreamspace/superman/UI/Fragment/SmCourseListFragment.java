package com.dreamspace.superman.UI.Fragment;


import android.support.v4.app.Fragment;

import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyCourseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.model.Lesson;
import com.dreamspace.superman.model.api.LessonInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmCourseListFragment extends BaseLazyCourseFragment<LessonInfo> {
    public static final String TAG="全部课程";
    public SmCourseListFragment() {
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
//        refreshDate(getTestData());
    }

    @Override
    protected void onItemPicked(LessonInfo item, int position) {

    }


}

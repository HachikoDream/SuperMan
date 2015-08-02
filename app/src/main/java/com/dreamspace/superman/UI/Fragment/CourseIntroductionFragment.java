package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseIntroductionFragment extends BaseFragment {

    private final static String TAG="课程介绍";

    public CourseIntroductionFragment() {
        // Required empty public constructor
        setTAG(TAG);

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_introduction;
    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void initDatas() {
    }
}

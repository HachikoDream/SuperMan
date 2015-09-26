package com.dreamspace.superman.UI.Fragment;


import android.support.v4.app.Fragment;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseIntroductionFragment extends BaseLazyFragment {

    private final static String TAG="课程介绍";

    public CourseIntroductionFragment() {
        // Required empty public constructor
        setTAG(TAG);

    }

    @Override
    protected void onFirstUserVisible() {
        toggleShowLoading(true,getString(R.string.common_loading_message));
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return ButterKnife.findById(getActivity(),R.id.card_view);
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_course_introduction;
    }
}

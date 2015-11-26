package com.dreamspace.superman.UI.Fragment;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLessonFragment;
import com.dreamspace.superman.model.api.LessonInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */

public class CourseIntroductionFragment extends BaseLessonFragment {
    @Bind(R.id.id_course_desc)
    TextView contentTv;
    @Bind(R.id.course_keeptime_tv)
    TextView courseKeeptimeTv;
    private final static String TAG = "课程介绍";
    private String desc = null;//课程描述
    private String lesson_keeptime = null;//课程时长
    private boolean onFirst = false;
    private boolean getLesson = false;

    public CourseIntroductionFragment() {
        // Required empty public constructor
        setTAG(TAG);

    }

    @Override
    protected void onFirstUserVisible() {
        onFirst = true;
        if (getLesson) {
            if (desc != null && lesson_keeptime != null) {
                setDataIntoView();
            } else {
                toggleShowError(true, getString(R.string.common_error_msg), null);
            }
        }
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return ButterKnife.findById(getActivity(), R.id.card_view);
    }

    @Override
    protected void initViewsAndEvents() {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_course_introduction;
    }

    @Override
    public void getLessonInfo(LessonInfo mLessonInfo) {
        getLesson = true;
        if (mLessonInfo != null) {
            desc = mLessonInfo.getDescription();
            lesson_keeptime = new StringBuilder().append(mLessonInfo.getKeeptime()).append("小时").toString();
        }
        if (onFirst) {
            if (desc != null && lesson_keeptime != null) {
                setDataIntoView();
            } else {
                toggleNetworkError(true, null);
            }
        }
    }

    private void setDataIntoView() {
        contentTv.setText(desc);
        courseKeeptimeTv.setText(lesson_keeptime);
    }
}

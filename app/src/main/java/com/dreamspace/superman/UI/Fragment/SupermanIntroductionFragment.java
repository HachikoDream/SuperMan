package com.dreamspace.superman.UI.Fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLessonFragment;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupermanIntroductionFragment extends BaseLessonFragment {
    @Bind(R.id.introduction_tv)
    TextView introduction_tv;
    @Bind(R.id.glory_content)
    TextView glory_tv;
    private final static String TAG = "达人简介";
    String mas_id;
    private boolean onFirst=false;
    private boolean getLesson=false;

    public SupermanIntroductionFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }


    @Override
    protected void onFirstUserVisible() {
        onFirst=true;
        if(getLesson){
            if(mas_id!=null){
                getSmInfo();
            }else {
                toggleNetworkError(true,null);
            }
        }
    }

    private void getSmInfo() {
        toggleShowLoading(true, getString(R.string.common_loading_message));
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity().getApplicationContext()).getSmDetailInfo(mas_id, new Callback<SmInfo>() {
                @Override
                public void success(SmInfo smInfo, Response response) {
                    toggleShowLoading(false, null);
                    if(smInfo!=null){
                        introduction_tv.setText(smInfo.getResume());
                        glory_tv.setText(smInfo.getGlory());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    toggleShowError(true, getInnerErrorInfo(error), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSmInfo();
                        }
                    });
                }
            });
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSmInfo();
                }
            });
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
        return ButterKnife.findById(getActivity(), R.id.sm_card_view);
    }

    @Override
    protected void initViewsAndEvents() {
//        LessonDetailInfoActivity mother = (LessonDetailInfoActivity) getActivity();
//        mas_id = mother.getmLessonInfo().getMast_id();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_superman_introduction;
    }


    @Override
    public void getLessonInfo(LessonInfo mLessonInfo) {
        getLesson=true;
        if(onFirst){
            if(mLessonInfo!=null){
                mas_id=mLessonInfo.getMast_id();
                getSmInfo();
            }else{
                toggleNetworkError(true,null);
            }
        }else {
            if (mLessonInfo!=null){
                mas_id=mLessonInfo.getMast_id();
            }else{
                mas_id=null;
            }
        }

    }
}

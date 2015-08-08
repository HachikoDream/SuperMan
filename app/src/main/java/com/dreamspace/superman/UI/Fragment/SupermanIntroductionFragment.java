package com.dreamspace.superman.UI.Fragment;


import android.support.v4.app.Fragment;
import android.view.View;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupermanIntroductionFragment extends BaseFragment {

    private final static String TAG="达人简介";
    public SupermanIntroductionFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_superman_introduction;
    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void initDatas() {


    }
}

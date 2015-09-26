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
public class SupermanIntroductionFragment extends BaseLazyFragment {

    private final static String TAG="达人简介";
    public SupermanIntroductionFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return ButterKnife.findById(getActivity(),R.id.sm_card_view);
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_superman_introduction;
    }
}

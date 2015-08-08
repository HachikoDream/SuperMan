package com.dreamspace.superman.UI.Fragment.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/8/1 0001.
 */
public abstract class BaseFragment extends Fragment {


    private String TAG="SUPER";
    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getTAG() {

        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(getLayoutId(),container,false);
        initViews(view);
        initDatas();
        return view;
    }
    public abstract int getLayoutId();
    public abstract void initViews(View view);
    public abstract void initDatas();
}

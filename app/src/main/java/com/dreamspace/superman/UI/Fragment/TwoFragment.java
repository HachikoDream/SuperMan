package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends BaseListFragment {

    private final String TAG="IT技术";

    public TwoFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }


    @Override
    public void initDatas() {
        Log.i("ORDER_TEST", "setTAG in two");
        setTAG(TAG);
    }
}

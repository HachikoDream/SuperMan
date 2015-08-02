package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.dreamspace.superman.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends BaseListFragment {

    private final static String TAG="健身";
    public ThreeFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }


    @Override
    public void initDatas() {
        Log.i("ORDER_TEST", "setTAG in three");
        setTAG(TAG);
    }
}

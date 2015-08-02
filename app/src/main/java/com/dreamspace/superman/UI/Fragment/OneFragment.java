package com.dreamspace.superman.UI.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.IndexAdapter;
import com.dreamspace.superman.UI.View.LoadMoreListView;
import com.dreamspace.superman.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends BaseListFragment {

    private LoadMoreListView moreListView;
    private IndexAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String TAG = "精选";

    public OneFragment() {
        // Required empty public constructor
        Log.i("ORDER_TEST","in One contruct");
        setTAG(TAG);
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i("ORDER_TEST","on Attach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("ORDER_TEST","on Create ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initDatas() {
        Log.i("ORDER_TEST","setTAG in one");
    }


}

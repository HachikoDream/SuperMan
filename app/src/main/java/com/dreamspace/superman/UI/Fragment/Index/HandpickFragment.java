package com.dreamspace.superman.UI.Fragment.Index;


import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyListFragment;
import com.dreamspace.superman.model.Lesson;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandpickFragment extends BaseLazyListFragment<Lesson> {
    public static String TAG = "精选";

    public HandpickFragment() {
//        super(IndexAdapter.class);
    }


    @Override
    public void onPullUp() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.i("onLoad", "on load complete");
                onPullUpFinished();
            }
        }, 3000);
    }

    @Override
    public void onPullDown() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onPullDownFinished();
            }
        }, 3000);
    }

    @Override
    public void onItemPicked(Lesson mEntity, int position) {
        Log.i("INFO", mEntity.toString());
    }

    @Override
    public void getInitData() {
        Log.i("INFO", "TAG IS :" + TAG);
//        refreshDate(loadingInitData());
    }

    public void loadingInitData() {
//        ApiManager.getService(getActivity().getApplicationContext()).get
    }

    public void onPageSelected(int position, String name) {
        TAG = name;
    }

}

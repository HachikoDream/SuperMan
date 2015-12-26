package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.MultiShowIvAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLessonFragment;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.entity.Photo;
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
    @Bind(R.id.glory_rv)
    RecyclerView gloryRv;
    private boolean onFirst = false;
    private boolean getLesson = false;
    private MultiShowIvAdapter adapter;

    //// TODO: 2015/12/26  加入荣誉照片点击事件 判断暂无情况
    public SupermanIntroductionFragment() {
        // Required empty public constructor
        setTAG(TAG);
    }


    @Override
    protected void onFirstUserVisible() {
        onFirst = true;
        if (getLesson) {
            if (mas_id != null) {
                getSmInfo();
            } else {
                toggleNetworkError(true, null);
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
                    if (smInfo != null) {
                        introduction_tv.setText(smInfo.getResume());
                        glory_tv.setText(smInfo.getGlory());
                        loadIntoGridViewByUrls(smInfo.getCertificates());
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

    private void loadIntoGridViewByUrls(String[] urls) {
        if (urls == null || urls.length == 0) {
            return;
        }
        List<Photo> mPhotos = new ArrayList<>();
        for (String url : urls) {
            Photo photo = new Photo();
            photo.setLocal(false);
            photo.setPath(url);
            mPhotos.add(photo);
        }
        adapter.setmPhotos(mPhotos);
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
        adapter = new MultiShowIvAdapter(getActivity());
        adapter.setShow_delete_icon(false);
        adapter.setShow_add_icon(false);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        gloryRv.setLayoutManager(manager);
        gloryRv.setAdapter(adapter);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_superman_introduction;
    }


    @Override
    public void getLessonInfo(LessonInfo mLessonInfo) {
        getLesson = true;
        if (onFirst) {
            if (mLessonInfo != null) {
                mas_id = mLessonInfo.getMast_id();
                getSmInfo();
            } else {
                toggleNetworkError(true, null);
            }
        } else {
            if (mLessonInfo != null) {
                mas_id = mLessonInfo.getMast_id();
            } else {
                mas_id = null;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

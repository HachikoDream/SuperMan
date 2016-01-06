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
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.LessonDetailInfoActivity;
import com.dreamspace.superman.UI.Activity.Superman.EditInfoActivity;
import com.dreamspace.superman.UI.Adapters.MultiShowIvAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLessonFragment;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.fragment.ImagePagerFragment;
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
    @Bind(R.id.sm_certificate_title)
    TextView certificate_tv;
    private final static String TAG = "达人简介";
    String mas_id;
    @Bind(R.id.glory_rv)
    RecyclerView gloryRv;
    private boolean onFirst = false;
    private boolean getLesson = false;
    private MultiShowIvAdapter adapter;

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
                        String resume = smInfo.getResume();
                        if (CommonUtils.isEmpty(resume)) {
                            resume = "暂无相关信息";
                        }
                        introduction_tv.setText(resume);
                        String glory = smInfo.getGlory();
                        if (CommonUtils.isEmpty(glory)) {
                            glory = "暂无相关信息";
                        }
                        glory_tv.setText(glory);
                        if (smInfo.getCertificates().length != 0) {
                            loadIntoGridViewByUrls(smInfo.getCertificates());
                        } else {
                            certificate_tv.setVisibility(View.GONE);
                            gloryRv.setVisibility(View.GONE);
                        }

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
        adapter.setPhotoClickListener(new MultiShowIvAdapter.onPhotoClickListener() {
            @Override
            public void onPhotoClick(View v, int pos) {
                List<String> photos = adapter.getCurrentPhotoPaths();

                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                ImagePagerFragment imagePagerFragment =
                        ImagePagerFragment.newInstance(photos, pos, screenLocation, v.getWidth(),
                                v.getHeight());

                ((LessonDetailInfoActivity) getActivity()).addImagePagerFragment(imagePagerFragment);
            }
        });
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
//        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

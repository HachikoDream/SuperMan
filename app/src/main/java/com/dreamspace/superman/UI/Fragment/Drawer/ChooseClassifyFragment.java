package com.dreamspace.superman.UI.Fragment.Drawer;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.View.SelectorImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/8/13 0013.
 */
public class ChooseClassifyFragment extends BaseLazyFragment {
    @Bind({R.id.ball,R.id.it,R.id.body,R.id.educate,R.id.camera,R.id.swim,R.id.music,R.id.paint,R.id.dance,R.id.write,R.id.others})
    SelectorImageView[] mSelectorImageViews = new SelectorImageView[11];
    private static String bfPackage = "com.dreamspace.superman.UI.Fragment.Index";
    private String[] mFragmentNames = {"BallFragment", "ITFragment", "GymFragment", "EducateFragment", "CameraFragment", "SwimFragment", "MusicFragment", "PaintFragment", "DanceFragment", "WriteFragment", "OthersFragment"};
    private List<String> mSelectedFNs=new ArrayList<>();
    @Bind(R.id.begin_read)
    Button mButton;

    public List<String> getSelectedNames() {
        return mSelectedFNs;
    }

    public void fillSelectedIds() {
        for (int i = 0; i < mSelectorImageViews.length; i++) {
            if (mSelectorImageViews[i].isSelected()) {
                mSelectedFNs.add(mFragmentNames[i]);
            }
        }
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
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choose_classify;
    }

}

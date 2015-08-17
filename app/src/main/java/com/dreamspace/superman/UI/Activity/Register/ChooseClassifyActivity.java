package com.dreamspace.superman.UI.Activity.Register;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Fragment.Drawer.IndexFragment;
import com.dreamspace.superman.UI.View.SelectorImageView;

import java.util.ArrayList;
import java.util.List;

public class ChooseClassifyActivity extends AbsActivity {
    private SelectorImageView[] mSelectorImageViews = new SelectorImageView[11];
    private static String bfPackage = "com.dreamspace.superman.UI.Fragment.Index";
    private String[] mFragmentNames = {"BallFragment", "ITFragment", "GymFragment", "EducateFragment", "CameraFragment", "SwimFragment", "MusicFragment", "PaintFragment", "DanceFragment", "WriteFragment", "OthersFragment"};
    private List<String> mSelectedFNs=new ArrayList<>();
    private Button mButton;
    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_choose_classify);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        mButton= (Button) findViewById(R.id.begin_read);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info",mSelectedFNs.toString());
                Intent intent=new Intent(ChooseClassifyActivity.this, IndexFragment.class);
                startActivity(intent);
            }
        });
        mSelectorImageViews[0] = (SelectorImageView) findViewById(R.id.ball);
        mSelectorImageViews[1] = (SelectorImageView) findViewById(R.id.it);
        mSelectorImageViews[2] = (SelectorImageView) findViewById(R.id.body);
        mSelectorImageViews[3] = (SelectorImageView) findViewById(R.id.educate);
        mSelectorImageViews[4] = (SelectorImageView) findViewById(R.id.camera);
        mSelectorImageViews[5] = (SelectorImageView) findViewById(R.id.swim);
        mSelectorImageViews[6] = (SelectorImageView) findViewById(R.id.music);
        mSelectorImageViews[7] = (SelectorImageView) findViewById(R.id.paint);
        mSelectorImageViews[8] = (SelectorImageView) findViewById(R.id.dance);
        mSelectorImageViews[9] = (SelectorImageView) findViewById(R.id.write);
        mSelectorImageViews[10] = (SelectorImageView) findViewById(R.id.others);
    }

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

}

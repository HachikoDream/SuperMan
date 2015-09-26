package com.dreamspace.superman.UI.Activity.Main;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.UI.Fragment.CourseIntroductionFragment;
import com.dreamspace.superman.UI.Fragment.StudentCommentListFragment;
import com.dreamspace.superman.UI.Fragment.SupermanIntroductionFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;
import com.dreamspace.superman.model.api.LessonInfo;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

public class LessonDetailInfoActivity extends AbsActivity {

    private static final int TITLE = R.string.title_activity_course_detail_info;
    @Bind(R.id.sliding_layout)
    SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.user_avater_iv)
    CircleImageView userIv;
    @Bind(R.id.username_tv)
    TextView userNameTv;
    @Bind(R.id.course_name_tv)
    TextView lessonnameTv;
    @Bind(R.id.sm_tag_tv)
    TextView tagTv;
    @Bind(R.id.want_meet_num_tv)
    TextView meet_num_tv;
    @Bind(R.id.success_meet_num_tv)
    TextView success_num_tv;
    @Bind(R.id.price_tv)
    TextView priceTv;
    private List<BaseLazyFragment> mFragments =new ArrayList<>();
    private int color=0;
    private int normalColor=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_course_detail_info);
    }

    @Override
    protected void prepareDatas() {
        color = getResources().getColor(R.color.navi_color);
        normalColor=getResources().getColor(R.color.select_tab_color);
        mFragments.add(new CourseIntroductionFragment());
        mFragments.add(new SupermanIntroductionFragment());
        mFragments.add(new StudentCommentListFragment());
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(TITLE);
        mAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mSlidingTabLayout.setStartColor(normalColor);
        mSlidingTabLayout.setViewPager(mViewPager);
        SlidingTabStrip.SimpleTabColorizer colorizer = new SlidingTabStrip.SimpleTabColorizer() {
            //覆盖Tab底部提示条的颜色
            @Override
            public int getIndicatorColor(int position) {
                return color;
            }
            //覆盖滑动到指定Tab处的文字颜色
            @Override
            public int getSelectedTitleColor(int position) {
                return normalColor;
            }

            @Override
            public int getNormalTitleColor(int position) {
                return normalColor;
            }
        };
        mSlidingTabLayout.setCustomTabColorizer(colorizer);
        String content=this.getIntent().getStringExtra("LESSON_INFO");
        Gson gson=new Gson();
        showLessonInfo(gson.fromJson(content, LessonInfo.class));
    }

    private void showLessonInfo(LessonInfo lessonInfo) {
        Tools.showImageWithGlide(this,userIv,lessonInfo.getImage());
        lessonnameTv.setText(lessonInfo.getLess_name());
        meet_num_tv.setText(String.valueOf(lessonInfo.getCollection_count()));
        success_num_tv.setText(String.valueOf(lessonInfo.getSuccess_count()));
        priceTv.setText(String.valueOf(lessonInfo.getPrice()));
        userNameTv.setText(lessonInfo.getName());
        tagTv.setText(lessonInfo.getDescription());
    }

}

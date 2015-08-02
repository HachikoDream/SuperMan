package com.dreamspace.superman.UI.Main;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.BaseFragment;
import com.dreamspace.superman.UI.Fragment.CourseIntroductionFragment;
import com.dreamspace.superman.UI.Fragment.OneFragment;
import com.dreamspace.superman.UI.Fragment.StudentCommentListFragment;
import com.dreamspace.superman.UI.Fragment.SupermanIntroductionFragment;
import com.dreamspace.superman.UI.Fragment.ThreeFragment;
import com.dreamspace.superman.UI.Fragment.TwoFragment;
import com.dreamspace.superman.UI.View.AbsActivity;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;

public class CourseDetailInfoActivity extends AbsActivity {

    private static final int TITLE = R.string.title_activity_course_detail_info;
    private SlidingTabLayout mSlidingTabLayout;
    private CommonFragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private BaseFragment[] mFragments = {new CourseIntroductionFragment(), new SupermanIntroductionFragment(), new StudentCommentListFragment()};
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
    protected void initDatas() {
        color = getResources().getColor(R.color.navi_color);
        normalColor=getResources().getColor(R.color.select_tab_color);
    }

    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(TITLE);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_layout);
        mAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
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
                return color;
            }

            @Override
            public int getNormalTitleColor(int position) {
                return normalColor;
            }
        };
        mSlidingTabLayout.setCustomTabColorizer(colorizer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_detail_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

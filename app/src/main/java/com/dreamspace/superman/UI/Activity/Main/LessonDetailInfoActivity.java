package com.dreamspace.superman.UI.Activity.Main;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.CommonFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLessonFragment;
import com.dreamspace.superman.UI.Fragment.CourseIntroductionFragment;
import com.dreamspace.superman.UI.Fragment.StudentCommentListFragment;
import com.dreamspace.superman.UI.Fragment.SupermanIntroductionFragment;
import com.dreamspace.superman.UI.View.SlidingTabLayout;
import com.dreamspace.superman.UI.View.SlidingTabStrip;
import com.dreamspace.superman.model.api.CollectReq;
import com.dreamspace.superman.model.api.LessonInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
    private List<BaseLessonFragment> mFragments = new ArrayList<>();
    private int color = 0;
    private int normalColor = 0;
    private LessonInfo mLessonInfo;
    private int less_id;
    private ProgressDialog pd;

    public LessonInfo getmLessonInfo() {
        return mLessonInfo;
    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_course_detail_info);
    }

    @Override
    protected void prepareDatas() {
        color = getResources().getColor(R.color.navi_color);
        normalColor = getResources().getColor(R.color.select_tab_color);
        mFragments.add(new CourseIntroductionFragment());
        mFragments.add(new SupermanIntroductionFragment());
        mFragments.add(new StudentCommentListFragment());
        less_id = this.getIntent().getIntExtra("LESSON_INFO", -1);

    }

    private void passInfoToChild(LessonInfo lessonInfo) {
        for (BaseLessonFragment baseLessonFragment : mFragments) {
            baseLessonFragment.getLessonInfo(lessonInfo);
        }
    }

    @Override
    protected void initViews() {
        Log.i("LDI", "init views");
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
        loadLessonInfo();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void dimissPd() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    private void loadLessonInfo() {
        showProgressDialog();
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(getApplicationContext()).getLessonDetail(less_id, new Callback<LessonInfo>() {
                @Override
                public void success(LessonInfo lessonInfo, Response response) {
                    Log.i("LDI", "success");
                    if (lessonInfo != null) {
                        dimissPd();
                        showLessonInfo(lessonInfo);
                        passInfoToChild(lessonInfo);
                        mLessonInfo = lessonInfo;
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("SM", "retrofit error");
                    dimissPd();
                    showInnerError(error);
                    passInfoToChild(null);
                    mLessonInfo = null;
                }
            });
        } else {
            dimissPd();
            showNetWorkError();
            passInfoToChild(null);
            mLessonInfo = null;
        }
    }

    private void showProgressDialog() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在加载课程数据", true, false);
        } else {
            pd.show();
        }
    }

    private void showLessonInfo(LessonInfo lessonInfo) {
        Tools.showImageWithGlide(this, userIv, lessonInfo.getImage());
        lessonnameTv.setText(lessonInfo.getLess_name());
        meet_num_tv.setText(String.valueOf(lessonInfo.getCollection_count()));
        success_num_tv.setText(String.valueOf(lessonInfo.getSuccess_count()));
        //TODO change the price
        priceTv.setText(String.valueOf(lessonInfo.getPrice()));
        userNameTv.setText(lessonInfo.getName());
        tagTv.setText(lessonInfo.getTags());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("LDI", "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lesson_detail, menu);
        MenuItem menuItem = menu.getItem(0);
        if (mLessonInfo != null) {
            boolean is_collected = mLessonInfo.is_collected();
            if (is_collected) {
                menuItem.setIcon(R.drawable.details_favor_h);
            } else {
                menuItem.setIcon(R.drawable.details_favor_n);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //todo 需判断用户是否登录,若没登录需跳到登录界面,回来之后需要刷新界面,刷新其是否收藏了这个课程
        int id = item.getItemId();
        if (id == R.id.item_collect) {
            if (less_id == -1) {
                showToast("请您稍后再次尝试");
            } else {
                if (mLessonInfo != null) {
                    boolean result = mLessonInfo.is_collected();
                    if(result){
                        ///todo add a dialog for ensure
                        DeleteCollectionsById(less_id,item);
                    }else{
                        CollectLessonById(less_id,item);
                    }
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteCollectionsById(int id, final MenuItem item) {
        showProgressDialog();
        if (NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).deleteCollectionById(id, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dimissPd();
                    showToast(getString(R.string.collect_quit_success));
                    item.setIcon(R.drawable.details_favor_n);
                    mLessonInfo.setIs_collected(!mLessonInfo.is_collected());
                }

                @Override
                public void failure(RetrofitError error) {
                    dimissPd();
                    showInnerError(error);
                }
            });
        }else {
            dimissPd();
            showNetWorkError();
        }
    }

    private void CollectLessonById(int id, final MenuItem item) {
        showProgressDialog();
        if (NetUtils.isNetworkConnected(this)) {
            CollectReq req = new CollectReq();
            req.setLess_id(id);
            ApiManager.getService(getApplicationContext()).collectLesson(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dimissPd();
                    showToast(getString(R.string.collect_success));
                    item.setIcon(R.drawable.details_favor_h);
                    mLessonInfo.setIs_collected(!mLessonInfo.is_collected());
                }

                @Override
                public void failure(RetrofitError error) {
                    dimissPd();
                    showInnerError(error);
                }
            });
        } else {
            dimissPd();
            showNetWorkError();
        }
    }
}

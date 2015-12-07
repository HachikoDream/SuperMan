package com.dreamspace.superman.UI.Activity.Main;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.InputUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.Common.UpLoadUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Person.ModifyInfoActivity;
import com.dreamspace.superman.UI.Activity.Register.LoginActivity;
import com.dreamspace.superman.UI.Adapters.VPFragmentAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.CollectionFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.FeedbackFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.IndexFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.MyWalletFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.OrderListFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.SuperManHomeFragment;
import com.dreamspace.superman.UI.Fragment.Drawer.ToBeSuperFragment;
import com.dreamspace.superman.UI.View.XViewPager;
import com.dreamspace.superman.event.AccountChangeEvent;
import com.dreamspace.superman.event.AvaterChangeEvent;
import com.dreamspace.superman.event.CollectionChangeEvent;
import com.dreamspace.superman.event.LessonListRefreshEvent;
import com.dreamspace.superman.event.MoneyRefreshEvent;
import com.dreamspace.superman.event.OrderChangeEvent;
import com.dreamspace.superman.model.api.EmptyBody;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.RegisterReq;
import com.dreamspace.superman.model.api.UpdateReq;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AbsActivity implements NavigationView.OnNavigationItemSelectedListener, FeedbackFragment.FeedbackComplete {

    @Bind(R.id.dl_left)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.id_navigation)
    NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.header_layout)
    RelativeLayout headerLayout;
    BaseLazyFragment fragments[] = {new IndexFragment(), new MyWalletFragment(), new OrderListFragment(), new CollectionFragment(), new ToBeSuperFragment(), new SuperManHomeFragment(), new FeedbackFragment()};
    @Bind(R.id.footer_item_settings)
    TextView mSettings;
    @Bind(R.id.footer_item_aboutus)
    TextView mAboutus;
    private static final int TITLE = R.string.app_name;
    private int currentPage;
    @Bind(R.id.fragment_container)
    XViewPager mViewPager;
    @Bind(R.id.nav_header_useravater)
    CircleImageView mUserAvater;
    @Bind(R.id.username_tv)
    TextView mUserName;
    Menu slideMenu;
    public static final String COME_SOURCE = "source";
    public static final String FIRST_IN = "first";
    public static final String NOT_FIRST_IN = "notfirst";
    public static final String AVATER_TASK = "avater_task";
    public static final String CACHED_PHOTO_PATH = "cached_photo_path";
    public static final int AVATER_LOAD_TASK = 1;//异步上传图片任务
    private int avater_task_val = -1;
    private String source;
    private String photoPath;//从起始页获得上次上传失败的头像存储路径
    private boolean master_available = true;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.custom_drawerlayout);
    }

    @Override
    protected void prepareDatas() {
        avater_task_val = this.getIntent().getIntExtra(AVATER_TASK, -1);
        source = this.getIntent().getStringExtra(COME_SOURCE);
        photoPath = this.getIntent().getStringExtra(CACHED_PHOTO_PATH);
    }

    public String getComeSource() {
        return source;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        getSupportActionBar().setTitle(getString(TITLE));
        checkIsLogin();
        mNavigationView.setNavigationItemSelectedListener(this);
        slideMenu = mNavigationView.getMenu();
        checkIsSuperMan(AccountChangeEvent.LOGIN);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        mAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(AboutusActivity.class);
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputUtils.closeInputMethod(MainActivity.this);
                invalidateOptionsMenu();
                setTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (null != fragments) {
            mViewPager.setEnableScroll(false);
            mViewPager.setOffscreenPageLimit(fragments.length);
            mViewPager.setAdapter(new VPFragmentAdapter(getSupportFragmentManager(), fragments));
        }
        if (avater_task_val != -1) {
            getUploadToken(photoPath);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    //检测用户是否属于达人用户
    private void checkIsSuperMan(String type) {
        String mast_state = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.MAST_STATE);
        MenuItem shItem = slideMenu.findItem(R.id.nav_superhome);
        MenuItem tobeItem = slideMenu.findItem(R.id.nav_tobesuperman);
        shItem.setVisible(false);
        tobeItem.setVisible(false);
        if (!CommonUtils.isEmpty(mast_state) && mast_state.equals(Constant.USER_APPLY_STATE.NORMAL)) {
            shItem.setVisible(true);
            if (type.equals(AccountChangeEvent.MAST_STATE_CHANGE)) {
                gotoMasterHome();
            }
        } else if (!CommonUtils.isEmpty(mast_state) && mast_state.equalsIgnoreCase(Constant.USER_APPLY_STATE.STOP)) {
            shItem.setVisible(true);
            master_available = false;
        } else {
            tobeItem.setVisible(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_conversation) {
            if (isLogin()) {
                startActivity(new Intent(MainActivity.this, ConListActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFragmentTitle(int id) {
        getSupportActionBar().setTitle(getString(id));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        InputUtils.closeInputMethod(this);
        mDrawerLayout.closeDrawer(mNavigationView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (menuItem.getItemId()) {
            case R.id.nav_index:
                mViewPager.setCurrentItem(0, false);
                EventBus.getDefault().post(new LessonListRefreshEvent());
                setFragmentTitle(R.string.nav_item_index);
                return true;
            case R.id.nav_mybalance:
                if (isLogin()) {
                    mViewPager.setCurrentItem(1, false);
                    EventBus.getDefault().post(new MoneyRefreshEvent());
                    setFragmentTitle(R.string.nav_item_mybalance);

                }
                return true;
            case R.id.nav_myorder:
                if (isLogin()) {
                    mViewPager.setCurrentItem(2, false);
                    EventBus.getDefault().post(new OrderChangeEvent());
                    setFragmentTitle(R.string.nav_item_myorder);
                }
                return true;
            case R.id.nav_mycollect:
                if (isLogin()) {
                    mViewPager.setCurrentItem(3, false);
                    EventBus.getDefault().post(new CollectionChangeEvent());
                    setFragmentTitle(R.string.nav_item_mycollect);
                }
                return true;
            case R.id.nav_tobesuperman:
                if (isLogin()) {
                    if (!PreferenceUtils.hasKey(getApplicationContext(), PreferenceUtils.Key.SM_INFO_READ)) {
                        PreferenceUtils.putBoolean(getApplicationContext(), PreferenceUtils.Key.SM_INFO_READ, true);
                        readyGo(AboutSupermanActivity.class);
                    }
                    mViewPager.setCurrentItem(4, false);
                    setFragmentTitle(R.string.nav_item_tobesuperman);
                }
                return true;
            case R.id.nav_superhome:
                if (master_available) {
                    mViewPager.setCurrentItem(5, false);
                    setFragmentTitle(R.string.nav_item_superhome);
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("您的达人权限已被禁用，如有疑问请联系客服。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
                return true;
            case R.id.nav_feedback:
                mViewPager.setCurrentItem(6, false);
                setFragmentTitle(R.string.nav_item_feedback);
                return true;
        }
        return false;
    }

    private void checkIsLogin() {
        if (!CommonUtils.isEmpty(PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.ACCOUNT))) {
            headerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readyGo(ModifyInfoActivity.class);
                }
            });
            mUserName.setText(PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.ACCOUNT));
            String url = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.AVATAR);
            Tools.showImageWithGlide(this, mUserAvater, url);
        } else {
            mUserName.setText("登录");
            mUserAvater.setImageResource(R.drawable.login_pho);
            headerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readyGo(LoginActivity.class);
                }
            });
        }
    }

    private boolean isLogin() {
        boolean result = !CommonUtils.isEmpty(PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.ACCOUNT));
        if (!result) {
            readyGo(LoginActivity.class);
        }
        return result;
    }

    private void checkLoginState(String type) {
        checkIsLogin();
        checkIsSuperMan(type);
    }

    public void onEvent(AccountChangeEvent event) {
        checkLoginState(event.type);
    }

    public void gotoIndex() {
        mViewPager.setCurrentItem(0, false);
        setFragmentTitle(R.string.nav_item_index);
    }

    //异步上传头像
    public void onEvent(AvaterChangeEvent event) {
        getUploadToken(event.getPhotoPath());
    }

    public void gotoMasterHome() {
        mViewPager.setCurrentItem(5, false);
        setFragmentTitle(R.string.nav_item_superhome);
    }

    @Override
    public void complete() {
        gotoIndex();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //获得七牛（第三方服务）的上传资源的凭证
    private void getUploadToken(final String photoPath) {
        if (NetUtils.isNetworkConnected(MainActivity.this)) {
            EmptyBody body = new EmptyBody();
            body.setInfo(Constant.FEMALE);
            ApiManager.getService(getApplicationContext()).createQiNiuToken(body, new Callback<QnRes>() {
                @Override
                public void success(QnRes qnRes, Response response) {
                    if (qnRes != null) {
                        uploadPhoto(photoPath, qnRes);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    saveWhenFailed(photoPath);
                }
            });
        } else {
            saveWhenFailed(photoPath);
        }

    }

    private void saveWhenFailed(String photoPath) {
        PreferenceUtils.putBoolean(getApplicationContext(), PreferenceUtils.Key.AVATER_AVAILABLE, false);
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.AVATER_CACHE_PATH, photoPath);

    }

    //上传用户的头像到七牛服务器
    private void uploadPhoto(final String photoPath, final QnRes res) {
        UploadManager manager = UpLoadUtils.getInstance();
        manager.put(photoPath, res.getKey(), res.getToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    modifyInfo(key, new FinishUpdate() {
                        @Override
                        public void onFinish() {
//                            PreferenceUtils.putBoolean(getApplicationContext(),PreferenceUtils.Key.AVATER_AVAILABLE,true);
                            PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.QINIU_SOURCE, res.getKey());
                        }

                        @Override
                        public void onError() {
                            saveWhenFailed(photoPath);
                        }
                    });
                } else if (info.isNetworkBroken()) {
                    saveWhenFailed(photoPath);
                } else if (info.isServerError()) {
                    saveWhenFailed(photoPath);
                }
            }
        }, null);
    }

    private void modifyInfo(String value, final FinishUpdate finishUpdate) {
        final UpdateReq req = new UpdateReq();
        req.setImage(value);
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(this.getApplicationContext()).updateUserInfo(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if (response.getStatus() != 200) {
                        finishUpdate.onError();
                        return;
                    }
                    finishUpdate.onFinish();
                }

                @Override
                public void failure(RetrofitError error) {
                    finishUpdate.onError();
                }
            });
        } else {
            finishUpdate.onError();
        }
    }

    private interface FinishUpdate {
        void onFinish();

        void onError();
    }

}

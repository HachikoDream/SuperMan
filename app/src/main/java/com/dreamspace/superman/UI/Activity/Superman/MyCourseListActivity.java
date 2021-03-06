package com.dreamspace.superman.UI.Activity.Superman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.BaseListAct;
import com.dreamspace.superman.UI.Adapters.MyCourseAdapter;
import com.dreamspace.superman.UI.Fragment.OnRefreshListener;
import com.dreamspace.superman.model.Lesson;
import com.dreamspace.superman.model.UserInfo;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.SmLessonList;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyCourseListActivity extends BaseListAct<LessonInfo> {
    private static final int REQUEST_CODE = 2688;
    private ProgressDialog pd;
    private int page = 1;
    private final int DEFAULT_PAGE = 1;

    public MyCourseListActivity() {
        super(MyCourseAdapter.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_course_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (!NetUtils.isNetworkConnected(this)) {
            item.setEnabled(false);
        }
        if (id == R.id.add_course) {
            Bundle b = new Bundle();
            b.putInt(AddCourseActivity.COME_SOURCE, AddCourseActivity.FROM_ADD);
            readyGoForResult(AddCourseActivity.class, REQUEST_CODE, b);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPullUp() {
        getDataByPage(++page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                if (mEntities.size() == 0) {
                    page--;
                    showToast(getString(R.string.common_nomore_data));
                } else {
                    refreshDate(mEntities, BaseListAct.ADD);
                }
                onPullUpFinished();
            }

            @Override
            public void onError() {
                onPullUpFinished();
            }
        });
    }

    @Override
    public void onPullDown() {
        page = DEFAULT_PAGE;
        getDataByPage(page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                refreshDate(mEntities, BaseListAct.LOAD);
                onPullDownFinished();
            }

            @Override
            public void onError() {
                onPullDownFinished();
            }
        });
    }

    @Override
    public void getInitData() {
        toggleShowLoading(true, null);
        page = DEFAULT_PAGE;
        getDataByPage(page, new OnRefreshListener<LessonInfo>() {
            @Override
            public void onFinish(List<LessonInfo> mEntities) {
                if (mEntities.size() == 0) {
                    toggleShowEmpty(true, "您还没有发布过课程,点击图标开启发布之旅~", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            b.putInt(AddCourseActivity.COME_SOURCE, AddCourseActivity.FROM_ADD);
                            readyGoForResult(AddCourseActivity.class, REQUEST_CODE, b);
                        }
                    });
                } else {
                    toggleShowLoading(false, null);
                    refreshDate(mEntities, BaseListAct.LOAD);
                }
            }

            @Override
            public void onError() {
                toggleShowLoading(false, null);
            }
        });
    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在刷新数据", true, false);
        } else {
            pd.show();
        }
    }

    private void dismissPd() {
        if (pd != null & pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void onItemPicked(LessonInfo mEntity, int position) {
        Bundle b = new Bundle();
        b.putInt(AddCourseActivity.COME_SOURCE, AddCourseActivity.FROM_MODIFY);
        b.putInt(AddCourseActivity.COME_INFO, mEntity.getId());
        readyGoForResult(AddCourseActivity.class, REQUEST_CODE, b);
    }

    @Override
    protected View getLoadingTargetView() {
        return mSwipeRefreshLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            showPd();
            getDataByPage(DEFAULT_PAGE, new OnRefreshListener<LessonInfo>() {
                @Override
                public void onFinish(List<LessonInfo> mEntities) {
                    dismissPd();
                    if (mEntities.size() == 0) {
                        toggleShowEmpty(true, "您还没有发布过课程,点击图标开启发布之旅~", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle b = new Bundle();
                                b.putInt(AddCourseActivity.COME_SOURCE, AddCourseActivity.FROM_ADD);
                                readyGoForResult(AddCourseActivity.class, REQUEST_CODE, b);
                            }
                        });

                    } else {
                        toggleShowEmpty(false, null, null);
                        refreshDate(mEntities, BaseListAct.LOAD);
                    }

                }

                @Override
                public void onError() {
                    dismissPd();
                }
            });
        }
    }


    public void getDataByPage(int page, final OnRefreshListener<LessonInfo> listener) {
        if (NetUtils.isNetworkConnected(this)) {
            String mas_id = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.Key.MAS_ID);
            if (!CommonUtils.isEmpty(mas_id)) {
                getCourseInfo(mas_id, listener);
            } else {
                ApiManager.getService(getApplicationContext()).getUserInfo(new Callback<UserInfo>() {
                    @Override
                    public void success(UserInfo userInfo, Response response) {
                        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.MAS_ID, userInfo.getMas_id());
                        getCourseInfo(userInfo.getMas_id(), listener);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showInnerError(error);
                        listener.onError();
                    }
                });
//                listener.onError();
//                showToast("暂时查询不到您的课程信息");
            }

        } else {
            showNetWorkError();
            listener.onError();
        }
    }

    private void getCourseInfo(String mas_id, final OnRefreshListener<LessonInfo> listener) {
        ApiManager.getService(getApplicationContext()).getLessonsbyMid(mas_id, page, "all", new Callback<SmLessonList>() {
            @Override
            public void success(SmLessonList smLessonList, Response response) {
                if (smLessonList != null) {
                    listener.onFinish(smLessonList.getLessons());
                } else {
                    listener.onError();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
                listener.onError();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}

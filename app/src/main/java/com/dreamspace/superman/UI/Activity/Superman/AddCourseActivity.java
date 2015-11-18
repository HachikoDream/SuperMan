package com.dreamspace.superman.UI.Activity.Superman;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.InputUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.model.api.LessonInfo;
import com.dreamspace.superman.model.api.ModifyLessonReq;
import com.dreamspace.superman.model.api.PublishReq;
import com.dreamspace.superman.model.api.PublishRes;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddCourseActivity extends AbsActivity {


    @Bind(R.id.coursename_ev)
    TextInputLayout coursenameEv;
    @Bind(R.id.coursetime_ev)
    TextInputLayout coursetimeEv;
    @Bind(R.id.price_ev)
    TextInputLayout priceEv;
    @Bind(R.id.mybtn)
    Button mybtn;
    @Bind(R.id.desc_ev)
    EditText descEv;
    String course_name;
    String keep_time;
    int price;
    String description;
    @Bind(R.id.modify_btn)
    Button modifyBtn;
    @Bind(R.id.delete_btn)
    Button deleteBtn;
    @Bind(R.id.pause_btn)
    Button pauseBtn;
    @Bind(R.id.modify_layout)
    RelativeLayout modifyLayout;
    private LinearLayout contentView;
    private ProgressDialog pd;
    public static final String COME_SOURCE = "comesource";
    public static final String COME_INFO = "comeinfo";
    public static final int FROM_ADD = 231;
    public static final int FROM_MODIFY = 232;
    private int source;//判断来源
    private int lesson_id;//修改和删除时候使用

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_add_course);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(source==FROM_MODIFY){
            getLessonDetailInfo(lesson_id);
        }
    }

    @Override
    protected void prepareDatas() {
        source = getIntent().getIntExtra(COME_SOURCE,-1);
        if (source==FROM_ADD) {
            mybtn.setVisibility(View.VISIBLE);
            modifyLayout.setVisibility(View.GONE);

        } else if (source==FROM_MODIFY) {
            mybtn.setVisibility(View.GONE);
            modifyLayout.setVisibility(View.VISIBLE);
            lesson_id = getIntent().getIntExtra(COME_INFO,-1);
        }
    }

    private void getLessonDetailInfo(final int lesson_id) {
       toggleShowLoading(true,null);
        if(NetUtils.isNetworkConnected(this)){
           ApiManager.getService(getApplicationContext()).getLessonDetail(lesson_id, new Callback<LessonInfo>() {
               @Override
               public void success(LessonInfo lessonInfo, Response response) {
                   if(lessonInfo!=null){
                       toggleShowLoading(false,null);
                       showExistLessonInfo(lessonInfo);
                   }else {
                       toggleShowError(true, getString(R.string.common_error_msg), new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               getLessonDetailInfo(lesson_id);
                           }
                       });
                   }
               }

               @Override
               public void failure(RetrofitError error) {
                     toggleShowError(true, getInnerErrorInfo(error), new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             getLessonDetailInfo(lesson_id);
                         }
                     });
               }
           });
        }else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getLessonDetailInfo(lesson_id);
                }
            });
        }
    }


    private void showExistLessonInfo(LessonInfo lessonInfo) {
        InputUtils.reverse(this);
        coursenameEv.getEditText().setText(lessonInfo.getLess_name());
        coursetimeEv.getEditText().setText(lessonInfo.getKeeptime());
        priceEv.getEditText().setText(CommonUtils.getStringFromPrice(lessonInfo.getPrice()));
        descEv.setText(lessonInfo.getDescription());
        coursenameEv.requestFocus();
        if(lessonInfo.getState().equals(Constant.LESSON_STATE.ON)){
            pauseBtn.setText("下架课程");
            pauseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWarningDialog("下架之后，本课程只对自己可见，之后您可以选择重新上架该课程，是否进行此操作？", new OnFinishListener() {
                        @Override
                        public void onFinish() {
                            ModifyLessonReq req = new ModifyLessonReq();
                            req.setState(Constant.LESSON_STATE.OFF);
                            ModifyLessonInfoById(lesson_id, req);
                        }
                    });
                }
            });

        }else{
            pauseBtn.setText("上架课程");
            pauseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWarningDialog("上架之后，您的课程可以重新被发现，是否进行此操作？", new OnFinishListener() {
                        @Override
                        public void onFinish() {
                            ModifyLessonReq req = new ModifyLessonReq();
                            req.setState(Constant.LESSON_STATE.ON);
                            ModifyLessonInfoById(lesson_id, req);
                        }
                    });
                }
            });

        }
    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(this, "", "正在提交数据", true, false);
        } else {
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void initViews() {
        contentView= (LinearLayout) findViewById(R.id.content_view);
        mybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
                if (isValid()) {
                    PublishReq req = new PublishReq();
                    req.setDescription(description);
                    req.setKeeptime(keep_time);
                    req.setName(course_name);
                    req.setPrice(price);
                    AddCourse(req);
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWarningDialog("该操作不可恢复，您确定要删除该课程吗？", new OnFinishListener() {

                    public void onFinish() {
                        deleteLessonById(lesson_id);
                    }

                });
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
                if (isValid()) {
                    ModifyLessonReq req = new ModifyLessonReq();
                    req.setDescription(description);
                    req.setPrice(price);
                    req.setKeeptime(keep_time);
                    req.setName(course_name);
                    req.setDescription(description);
                    ModifyLessonInfoById(lesson_id, req);
                }
            }
        });

    }

    private void ModifyLessonInfoById(int lesson_id, ModifyLessonReq req) {

        showPd();
        if (NetUtils.isNetworkConnected(this)){
            ApiManager.getService(getApplicationContext()).modifyLessonInfo(lesson_id, req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if (response != null) {
                        showInfoDialog("课程信息已更新", new OnFinishListener() {
                            @Override
                            public void onFinish() {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    showInfoDialog(getInnerErrorInfo(error), null);
                }
            });
        }else {
            showNetWorkError();
        }
    }

    private void deleteLessonById(int lesson_id) {
        showPd();
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(getApplicationContext()).deleteLesson(lesson_id, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if (response != null) {
                        dismissPd();
                        showInfoDialog("您已成功删除该课程",new OnFinishListener(){

                            @Override
                            public void onFinish() {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                  showInnerError(error);
                }
            });
        } else {
            showNetWorkError();
        }
    }

    private void showInfoDialog(String s, final OnFinishListener onFinishListener) {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(s)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (onFinishListener != null) {
                            onFinishListener.onFinish();
                        }

                    }
                })
                .show();
    }

    private void showWarningDialog(String info,final OnFinishListener onFinishListener) {
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onFinishListener.onFinish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        builder.setCanceledOnTouchOutside(true);
    }

    @Override
    protected View getLoadingTargetView() {
        return contentView;
    }

    private void AddCourse(PublishReq req) {
        showPd();
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(getApplicationContext()).publishLessonBySm(req, new Callback<PublishRes>() {
                @Override
                public void success(PublishRes publishRes, Response response) {
                    dismissPd();
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showInnerError(error);
                }
            });
        } else {
            dismissPd();
            showNetWorkError();
        }
    }

    private void getValue() {
        course_name = coursenameEv.getEditText().getText().toString();
        keep_time = coursetimeEv.getEditText().getText().toString();
        String price_content = priceEv.getEditText().getText().toString();
        if (CommonUtils.isEmpty(price_content)) {
            price = -1;
        } else {
            price = CommonUtils.getPriceFromString(price_content);
        }
        description = descEv.getText().toString();

    }

    private boolean isValid() {
        setDefaultForinput();
        if (CommonUtils.isEmpty(course_name)) {
            coursenameEv.setErrorEnabled(true);
            coursenameEv.setError("课程名不能为空");
            coursenameEv.getEditText().setSelected(true);
            showToast("课程名不能为空");
            return false;
        } else if (course_name.length() >= 20) {
            coursenameEv.setErrorEnabled(true);
            coursenameEv.setError("课程名称不能大于20个字符");
            showToast("课程名称不能大于20个字符");
            return false;
        } else if (CommonUtils.isEmpty(keep_time)) {
            coursetimeEv.setErrorEnabled(true);
            coursetimeEv.setError("请先填写课时");
            showToast("请先填写课时");
            return false;
        } else if (price == -1) {
            priceEv.setErrorEnabled(true);
            priceEv.setError("请填写正确的价格");
            showToast("请填写正确的价格");
            return false;
        } else if (CommonUtils.isEmpty(description)) {
            showToast("请简要描述课程内容");
            descEv.setSelected(true);
            return false;
        }
        return true;
    }

    private void setDefaultForinput() {
        coursenameEv.setErrorEnabled(false);
        coursetimeEv.setErrorEnabled(false);
        priceEv.setErrorEnabled(false);
    }


    private interface OnFinishListener {
        public void onFinish();
//        public void cancel();

    }
}

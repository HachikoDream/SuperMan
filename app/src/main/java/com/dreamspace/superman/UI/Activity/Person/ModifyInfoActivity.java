package com.dreamspace.superman.UI.Activity.Person;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.Common.UpLoadUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Main.MainActivity;
import com.dreamspace.superman.UI.Activity.Register.VerifyByPhoneAct;
import com.dreamspace.superman.event.AccountChangeEvent;
import com.dreamspace.superman.model.UserInfo;
import com.dreamspace.superman.model.api.EmptyBody;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.RegisterReq;
import com.dreamspace.superman.model.api.UpdateReq;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ModifyInfoActivity extends AbsActivity {


    @Bind(R.id.useravater_layout)
    RelativeLayout avaterLayout;
    @Bind(R.id.nickname_layout)
    RelativeLayout nicknameLayout;
    @Bind(R.id.gender_layout)
    RelativeLayout genderLayout;
    @Bind(R.id.realname_layout)
    RelativeLayout realnameLayout;
    @Bind(R.id.phonenum_layout)
    RelativeLayout phoneNumLayout;
    @Bind(R.id.modifypwd_layout)
    RelativeLayout modifyLayout;
    @Bind(R.id.user_avater_iv)
    CircleImageView avaterIv;
    @Bind(R.id.username_tv)
    TextView userNameTv;
    @Bind(R.id.gender_tv)
    TextView genderTv;
    @Bind(R.id.real_name_tv)
    TextView realNameTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    private final static int REQUEST_PHOTO = 233;
    private static final int REQUEST_MODIFY_PHONE = 234;
    private String photoPath;
    //sex,nickname,name,image
    private static final String AVATER = "image";
    private static final String SEX = "sex";
    private static final String NICKNAME = "nickname";
    private static final String USERNAME = "name";
    private ProgressDialog pd;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_modify_info);
    }

    @Override
    protected void prepareDatas() {
        setContentFromLocal();
    }

    private void setContentFromLocal() {
        Tools.showImageWithGlide(this, avaterIv, PreferenceUtils.getString(this.getApplicationContext(), PreferenceUtils.Key.AVATAR));
        phoneTv.setText(fuzzyString(PreferenceUtils.getString(this.getApplicationContext(), PreferenceUtils.Key.PHONE)));
        userNameTv.setText(PreferenceUtils.getString(this.getApplicationContext(), PreferenceUtils.Key.ACCOUNT));
        realNameTv.setText(PreferenceUtils.getString(this.getApplicationContext(), PreferenceUtils.Key.REALNAME));
        String gender = PreferenceUtils.getString(this.getApplicationContext(), PreferenceUtils.Key.SEX);
        String genderContent = "男";
        if (gender.equals("female")) {
            genderContent = "女";
        }
        genderTv.setText(genderContent);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));//// TODO: 2015/11/25  删除缓存图片
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            photoPath = Crop.getOutput(result).getPath();
            showProgressDialog();
            getUploadToken();
            Log.i("info", photoPath);
        } else if (resultCode == Crop.RESULT_ERROR) {
            showToast(Crop.getError(result).getMessage());//// TODO: 2015/11/25  失败考虑默认头像
        }
    }

    @Override
    protected void initViews() {
        avaterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(ModifyInfoActivity.this);
//                PhotoPickerIntent intent = new PhotoPickerIntent(ModifyInfoActivity.this);
//                intent.setPhotoCount(1);
//                intent.setShowCamera(true);
//                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
        nicknameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWithEt(userNameTv.getText().toString(), "输入昵称", new DialogButtonClicked() {
                    @Override
                    public void onPositiveClicked(final String content, DialogInterface dialog) {

                        dialog.dismiss();
                        showProgressDialog();
                        modifyInfo(NICKNAME, content, new FinishUpdate() {
                            @Override
                            public void onFinish() {
                                userNameTv.setText(content);
                                //保存新的用户名到sp
                                PreferenceUtils.putString(ModifyInfoActivity.this.getApplicationContext(), PreferenceUtils.Key.ACCOUNT, content);
                                pd.dismiss();
                                EventBus.getDefault().post(new AccountChangeEvent());
                            }

                            @Override
                            public void onError() {
                                pd.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onNegativeClicked(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        genderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = 0;
                final String[] content = {"男", "女"};
                if (genderTv.getText().toString().equals("女")) {
                    selected = 1;
                }
                final int finalSelected = selected;
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyInfoActivity.this)
                        .setTitle("选择性别")
                        .setCancelable(true)
                        .setSingleChoiceItems(content, selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which != finalSelected) {
                                    final String newGender = content[which];
                                    dialog.dismiss();
                                    String value = "male";
                                    if (which == 1)
                                        value = "female";
                                    final String finalValue = value;
                                    showProgressDialog();
                                    modifyInfo(SEX, value, new FinishUpdate() {
                                        @Override
                                        public void onFinish() {
                                            genderTv.setText(newGender);
                                            PreferenceUtils.putString(ModifyInfoActivity.this.getApplicationContext(), PreferenceUtils.Key.SEX, finalValue);
                                            pd.dismiss();
                                        }

                                        @Override
                                        public void onError() {
                                            pd.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        realnameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWithEt(realNameTv.getText().toString(), "输入真实姓名", new DialogButtonClicked() {
                    @Override
                    public void onPositiveClicked(final String content, DialogInterface dialog) {
                        dialog.dismiss();
                        showProgressDialog();
                        modifyInfo(USERNAME, content, new FinishUpdate() {
                            @Override
                            public void onFinish() {
                                realNameTv.setText(content);
                                PreferenceUtils.putString(ModifyInfoActivity.this.getApplicationContext(), PreferenceUtils.Key.REALNAME, content);
                                pd.dismiss();
                            }

                            @Override
                            public void onError() {
                                pd.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onNegativeClicked(DialogInterface dialog) {

                    }
                });
            }
        });
        phoneNumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoModifyPage(Constant.COME_SOURCE.MODIFY_PHOME);
            }
        });
        modifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoModifyPage(Constant.COME_SOURCE.MODIFY_PWD);
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void GotoModifyPage(int source) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.COME_SOURCE.SOURCE, source);
        readyGoForResult(VerifyByPhoneAct.class, REQUEST_MODIFY_PHONE, bundle);

    }

    //获得七牛（第三方服务）的上传资源的凭证
    private void getUploadToken() {
        if (NetUtils.isNetworkConnected(ModifyInfoActivity.this)) {
            EmptyBody body = new EmptyBody();
            body.setInfo(Constant.FEMALE);
            ApiManager.getService(getApplicationContext()).createQiNiuToken(body, new Callback<QnRes>() {
                @Override
                public void success(QnRes qnRes, Response response) {
                    if (qnRes != null) {
                        uploadPhoto(qnRes);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissDialog();
                    showInnerError(error);
                }
            });
        } else {
            dismissDialog();
            showNetWorkError();
        }

    }

    private void dismissDialog() {
        if (pd != null&&pd.isShowing()) {
            pd.dismiss();
        }
    }

    //上传用户的头像到七牛服务器
    private void uploadPhoto(QnRes res) {
        UploadManager manager = UpLoadUtils.getInstance();
        manager.put(photoPath, res.getKey(), res.getToken(), new UpCompletionHandler() {
            @Override
            public void complete(final String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    modifyInfo(AVATER, key, new FinishUpdate() {
                        @Override
                        public void onFinish() {
                            dismissDialog();
                            PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.QINIU_SOURCE, key);
                            PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.AVATAR, photoPath);
                            Tools.showImageWithGlide(ModifyInfoActivity.this, avaterIv, photoPath);
                            EventBus.getDefault().post(new AccountChangeEvent());
                        }

                        @Override
                        public void onError() {
                            dismissDialog();
                            showToast("头像上传失败，请稍后再试");
                        }
                    });
                } else if (info.isNetworkBroken()) {
                    dismissDialog();
                    showNetWorkError();
                } else if (info.isServerError()) {
                    dismissDialog();
                    showToast("服务暂时不可用，请稍后重试");
                }
            }
        }, null);
    }

    //获取用户信息,暂时不用
    private void getUserInfo() {
        ApiManager.getService(getApplicationContext()).getUserInfo(new Callback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo, Response response) {
                dismissDialog();
                showToast("头像修改成功");
                Tools.showImageWithGlide(ModifyInfoActivity.this, avaterIv, photoPath);
                saveUserInfo(userInfo);
            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
                pd.dismiss();
            }
        });
    }

    //保存用户信息到本地
    private void saveUserInfo(UserInfo userInfo) {
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.AVATAR, userInfo.getImage());
    }

    /*
        content:EditText init content
        title: dialog's title

     */
    private void showDialogWithEt(final String content, String title, final DialogButtonClicked btnListener) {
        final EditText mEdit = new EditText(ModifyInfoActivity.this);
        mEdit.setText(content);
        AlertDialog builder = new AlertDialog.Builder(ModifyInfoActivity.this)
                .setTitle(title)
                .setView(mEdit)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newcontent = mEdit.getText().toString();
                        if (newcontent.equals(content)) {
                            dialog.dismiss();
                        } else if (CommonUtils.isEmpty(newcontent)) {
                            showToast("该项不能为空");
                        } else {
                            btnListener.onPositiveClicked(newcontent, dialog);
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnListener.onNegativeClicked(dialog);
                    }
                })
                .show();

    }

    private String fuzzyString(String phone) {
        StringBuilder sb = new StringBuilder();
        sb.append(phone.substring(0, 3));
        sb.append("******");
        sb.append(phone.substring(phone.length() - 2, phone.length()));
        return sb.toString();
    }

    private void modifyInfo(String key, String value, final FinishUpdate finishUpdate) {
        final UpdateReq req = new UpdateReq();
        if (key.equals(AVATER)) {
            req.setImage(value);
        } else if (key.equals(NICKNAME)) {
            req.setNickname(value);
        } else if (key.equals(SEX)) {
            req.setSex(value);
        } else if (key.equals(USERNAME)) {
            req.setName(value);
        }
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(this.getApplicationContext()).updateUserInfo(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if (response.getStatus() != 200) {
                        showToast(response.getReason());
                        finishUpdate.onError();
                        return;
                    }
                    finishUpdate.onFinish();
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                    finishUpdate.onError();
                }
            });
        } else {
            showNetWorkError();
            finishUpdate.onError();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Crop.REQUEST_PICK:
                    beginCrop(data.getData());
                    break;
                case Crop.REQUEST_CROP:
                    handleCrop(resultCode, data);
                    break;
                case REQUEST_MODIFY_PHONE:
                    if (data != null) {
                        String newPhone = data.getStringExtra("phoneNum");
                        phoneTv.setText(newPhone);
                    } else {
                        phoneTv.setText(PreferenceUtils.getString(this.getApplicationContext(), PreferenceUtils.Key.PHONE));
                    }
                    break;
            }

        }
    }

    private void showProgressDialog() {
        if (pd == null) {
            pd = ProgressDialog.show(ModifyInfoActivity.this, "", "正在提交数据...", true, false);
        } else {
            pd.show();
        }
    }

    private interface FinishUpdate {
        void onFinish();

        void onError();
    }


    private interface DialogButtonClicked {
        void onPositiveClicked(String content, DialogInterface dialog);

        void onNegativeClicked(DialogInterface dialog);
    }
}

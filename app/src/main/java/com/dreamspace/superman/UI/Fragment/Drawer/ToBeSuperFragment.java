package com.dreamspace.superman.UI.Fragment.Drawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.Common.UpLoadUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.MainActivity;
import com.dreamspace.superman.UI.Activity.Superman.OnFinish;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.model.TempRes;
import com.dreamspace.superman.model.api.ApplyInfoRes;
import com.dreamspace.superman.model.api.EmptyBody;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.ToBeSmReq;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ToBeSuperFragment extends BaseLazyFragment {
    @Bind(R.id.mybtn)
    Button finishBtn;
    @Bind(R.id.user_avater_iv)
    CircleImageView userIv;
    @Bind(R.id.radiogroup)
    RadioGroup mRadiogroup;
    @Bind(R.id.gender_man)
    RadioButton genderMan;
    @Bind(R.id.gender_woman)
    RadioButton genderWoman;
    @Bind(R.id.skils_ev)
    TextInputLayout skilsEv;
    @Bind(R.id.tags_ev)
    TextInputLayout tagsEv;
    @Bind(R.id.honour_ev)
    EditText honourEv;
    @Bind(R.id.introduction_ev)
    EditText introductionEv;
    @Bind(R.id.glory_image)
    ImageView gloryIv;
    private String gender;//性别，从本地数据中读取
    private String avater_code;//七牛的原始code，从本地数据中读取
    private String avater_url;
    private String tags;//个人标签
    private String honour;//荣誉
    private String realName;//真实姓名，从本地数据中读取
    private String skills;//技能
    private String phone;//电话，从本地数据中读取
    private String introduction;//介绍
    private final static int REQUEST_CODE = 233;
    private ProgressDialog pd;
    private String photoPath;//荣誉证书照片的本地路径
    private boolean choose_glory_iv = false;//用于表明用户是否选择了荣誉证书的照片进行上传

    @Override
    protected void onFirstUserVisible() {
        getUserInfoForApply(true);

    }

    private void showPd() {
        if (pd == null) {
            pd = ProgressDialog.show(getActivity(), "", getString(R.string.common_loading_message), true, false);
        } else {
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    private void dismissPd() {
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

    @Override
    protected void onUserVisible() {
        getUserInfoForApply(false);
    }

    private void showGender() {
        if (gender.equals(Constant.FEMALE)) {
            genderWoman.setSelected(true);
        } else {
            genderMan.setSelected(true);
        }
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return ButterKnife.findById(getActivity(), R.id.content_view);
    }

    @Override
    protected void initViewsAndEvents() {
        loadFromLocal();
        gloryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                intent.setPhotoCount(1);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

    }

    //检测用户的申请状态
    private void getUserInfoForApply(final boolean isFirst) {
        toggleShowLoading(true, getString(R.string.common_loading_message));
        if (NetUtils.isNetworkConnected(getActivity())) {
            ApiManager.getService(getActivity().getApplicationContext()).getUserApplyInfo(new Callback<ApplyInfoRes>() {
                @Override
                public void success(ApplyInfoRes applyInfoRes, Response response) {
                    toggleShowLoading(false, null);
                    if (applyInfoRes != null) {
                        String state = applyInfoRes.getState();
                        if (state.equals(Constant.USER_APPLY_STATE.STOP)) {
                            toggleShowError(true, getString(R.string.to_be_superman_refuse), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getUserInfoForApply(false);
                                }
                            });
                        } else if (state.equals(Constant.USER_APPLY_STATE.PENDING)) {
                            toggleShowError(true, getString(R.string.to_be_superman_pending), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getUserInfoForApply(false);
                                }
                            });

                        } else {
                            if (isFirst) {
                                genderMan.setEnabled(false);
                                genderWoman.setEnabled(false);
                                Tools.showImageWithGlide(getActivity(), userIv, avater_url);
                                showGender();
                            }
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    toggleShowError(true, getInnerErrorInfo(error), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getUserInfoForApply(false);
                        }
                    });
                }
            });
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUserInfoForApply(false);
                }
            });
        }

    }

    //获得七牛（第三方服务）的上传资源的凭证
    private void getUploadToken() {
        if (NetUtils.isNetworkConnected(getActivity())) {
            EmptyBody body=new EmptyBody();
            body.setInfo(Constant.FEMALE);
            ApiManager.getService(getActivity().getApplicationContext()).createQiNiuToken(body,new Callback<QnRes>() {
                @Override
                public void success(QnRes qnRes, Response response) {
                    if (qnRes != null) {
                        uploadPhoto(qnRes);
                    }
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

    //上传用户的证书信息到七牛服务器
    private void uploadPhoto(QnRes res) {
        UploadManager manager = UpLoadUtils.getInstance();
        manager.put(photoPath, res.getKey(), res.getToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    Log.i("INFO", "upload is Ok");
                    toBeSm(key);

                } else if (info.isNetworkBroken()) {
                    dismissPd();
                    showNetWorkError();
                } else if (info.isServerError()) {
                    dismissPd();
                    showToast("服务暂时不可用，请稍后重试");
                }
            }
        }, null);
    }

    //点击确定按钮后的处理函数
    private void tryToBeSm() {
        if (choose_glory_iv) {
            //选择了证书图片，需要先进行七牛的图片上传服务，再调用API
            showPd();
            getUploadToken();
        } else {
            //直接调用API
            showPd();
            if (NetUtils.isNetworkConnected(getActivity())) {
                toBeSm(null);
            } else {
                showNetWorkError();
                dismissPd();
            }

        }
    }

    //上传相关数据，申请成为达人
    private void toBeSm(String certificate_code) {
        ToBeSmReq req = new ToBeSmReq();
        req.setGlory(honour);
        req.setImage(avater_code);
        req.setName(realName);
        req.setPhone(phone);
        req.setResume(introduction);
        req.setSex(gender);
        req.setTags(tags);
        req.setWant_cata(skills);
        if (certificate_code != null) {
            String[] certificates = {certificate_code};
            req.setCertificates(certificates);
        } else {
            String[] certificates = {};
            req.setCertificates(certificates);
        }
        ApiManager.getService(getActivity().getApplicationContext()).applytoSuperMan(req, new Callback<TempRes>() {
            @Override
            public void success(TempRes tempRes, Response response) {
                if (tempRes != null) {
                    /**todo:
                     * 1.取消toast对id的显示
                     * 2.在本地缓存标志位表示已经进行过申请操作，避免后续的重复申请
                     * 3.显示一个提示对话框
                     * 4.导航到主页面
                     * 测试
                     */

                    dismissPd();
                    showInfoWithDialog("申请成功，我们会以短信的方式通知您的申请结果，请等待.", new OnFinish() {
                        @Override
                        public void finish(boolean isOk) {
                            MainActivity activity= (MainActivity) getActivity();
                            activity.gotoIndex();
                        }
                    });

                }
            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
                dismissPd();
            }
        });
    }
    private void showInfoWithDialog(String msg, final OnFinish listener){
        AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       listener.finish(true);
                    }
                })
                .show();
        dialog.setCanceledOnTouchOutside(false);
//                .

    }

    //从本地加载用户相关的数据
    private void loadFromLocal() {
        gender = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.SEX);
        avater_code = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.QINIU_SOURCE);
        phone = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.PHONE);
        realName = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.REALNAME);
        avater_url = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.AVATAR);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    tryToBeSm();
                }
            }
        });
    }

    //在进行网络操作之前检查用户输入的数据是否合法
    private boolean isValid() {
        skills = skilsEv.getEditText().getText().toString();
        tags = tagsEv.getEditText().getText().toString();
        honour = honourEv.getText().toString();
        introduction = introductionEv.getText().toString();
        skilsEv.setErrorEnabled(false);
        tagsEv.setErrorEnabled(false);
        if (CommonUtils.isEmpty(tags)) {
            showToast("请先为自己写一个标签");
            return false;
        } else if (tags.length() >= 20) {
            tagsEv.setErrorEnabled(true);
            tagsEv.setError("标签不应大于20个字");
            return false;
        } else if (CommonUtils.isEmpty(honour)) {
            honourEv.setSelected(true);
            showToast("请输入您的荣誉");
            return false;
        } else if (CommonUtils.isEmpty(skills)) {
            skilsEv.setErrorEnabled(true);
            skilsEv.setError("请输入您擅长的技能");
            return false;
        } else if (CommonUtils.isEmpty(introduction)) {
            introductionEv.setSelected(true);
            showToast("请输入您的个人介绍");
            return false;
        }
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_to_be_super;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Log.i("INFO", "PHOTO:" + photos.get(0));
                photoPath = photos.get(0);
                Tools.showImageWithGlide(getActivity(), gloryIv, photoPath);
                choose_glory_iv = true;
            }
        }
    }
}

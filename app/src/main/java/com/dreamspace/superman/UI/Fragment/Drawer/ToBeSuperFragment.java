package com.dreamspace.superman.UI.Fragment.Drawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.dreamspace.superman.UI.Adapters.MultiShowIvAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;
import com.dreamspace.superman.event.AccountChangeEvent;
import com.dreamspace.superman.model.TempRes;
import com.dreamspace.superman.model.api.ApplyInfoRes;
import com.dreamspace.superman.model.api.EmptyBody;
import com.dreamspace.superman.model.api.MultiQnReq;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.SingleQnRes;
import com.dreamspace.superman.model.api.ToBeSmReq;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadOptions;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ToBeSuperFragment extends BaseLazyFragment {
    @Bind(R.id.mybtn)
    Button finishBtn;
    @Bind(R.id.user_avater_iv)
    CircleImageView userIv;
    //    @Bind(R.id.radiogroup)
//    RadioGroup mRadiogroup;
//    @Bind(R.id.gender_man)
//    RadioButton genderMan;
//    @Bind(R.id.gender_woman)
//    RadioButton genderWoman;
    @Bind(R.id.skils_ev)
    TextInputLayout skilsEv;
    @Bind(R.id.tags_ev)
    TextInputLayout tagsEv;
    //    @Bind(R.id.realname_ev)
//    TextInputLayout realnameEv;
    @Bind(R.id.honour_ev)
    EditText honourEv;
    @Bind(R.id.introduction_ev)
    EditText introductionEv;
    //    @Bind(R.id.glory_image)
//    ImageView gloryIv;
    @Bind(R.id.multi_image_show)
    RecyclerView multiView;
    private String gender;//性别，从本地数据中读取
    private String avater_code;//七牛的原始code，从本地数据中读取
    private String avater_url;
    private String tags;//个人标签
    private String honour;//荣誉
    private String realName;//真实姓名，从本地数据中读取,允许用户修改,修改后保存在本地
    private String skills;//技能
    private String phone;//电话，从本地数据中读取
    private String introduction;//介绍
    private final static int REQUEST_CODE = 233;
    private ProgressDialog pd;
    private List<Photo> selectedPhotos = new ArrayList<>();//已经选择的图片
    private MultiShowIvAdapter adapter;
    private List<String> keys = new ArrayList<>();//存储七牛的key，用于成为达人的接口
    private boolean cancel_qiniu = false;//取消七牛的上传操作

    @Override
    protected void onFirstUserVisible() {
        getUserInfoForApply(true);

    }

    private void showPd(String text) {
        if (text == null) {
            text = getString(R.string.common_loading_message);
        }
        if (pd == null) {
            pd = ProgressDialog.show(getActivity(), "", text, true, false);
        } else {
            if (!pd.isShowing()) {
                pd.show();
            } else {
                pd.setMessage(text);
            }
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onUserVisible() {
        getUserInfoForApply(false);
    }

//    private void showGender() {
//        if (gender.equals(Constant.FEMALE)) {
//            genderWoman.setChecked(true);
//        } else {
//            genderMan.setChecked(true);
//        }
//    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return ButterKnife.findById(getActivity(), R.id.content_view);
    }

    @Override
    protected void initViewsAndEvents() {
//        loadFromLocal();
        adapter = new MultiShowIvAdapter(selectedPhotos, getActivity());
        adapter.setPhotoClickListener(new MultiShowIvAdapter.onPhotoClickListener() {
            @Override
            public void onPhotoClick() {
                PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
                intent.setPhotoCount(4);
                intent.setShowCamera(true);
                intent.setSeletedPhotos(selectedPhotos);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        multiView.setAdapter(adapter);
        multiView.setLayoutManager(manager);
//        gloryIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Crop.pickImage(getActivity(), ToBeSuperFragment.this);
//                PhotoPickerIntent intent = new PhotoPickerIntent(getActivity());
//                intent.setPhotoCount(1);
//                intent.setShowCamera(true);
//                startActivityForResult(intent, REQUEST_CODE);
//
//            }
//        });


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
                        if (state.equalsIgnoreCase(Constant.USER_APPLY_STATE.STOP)) {
                            toggleShowError(true, getString(R.string.to_be_superman_refuse), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getUserInfoForApply(false);
                                }
                            });
                        } else if (state.equalsIgnoreCase(Constant.USER_APPLY_STATE.PENDING)) {

                            toggleShowError(true, getString(R.string.to_be_superman_pending), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getUserInfoForApply(false);
                                }
                            });

                        } else if (state.equals(Constant.USER_APPLY_STATE.NOT_APPLY)) {
                            loadFromLocal();
//                            realnameEv.getEditText().setText(realName);
                            Tools.showImageWithGlide(getActivity(), userIv, avater_url);
//                            showGender();
//                            genderMan.setEnabled(false);
//                            genderWoman.setEnabled(false);
                        } else if (state.equals(Constant.USER_APPLY_STATE.NORMAL)) {
                            showInfoWithDialog("恭喜您已经通过我们的认证，成为一名达人，您之后可以点击菜单栏中的达人主页来管理您的信息与课程。点击确定跳转到您的达人主页", new OnFinish() {
                                @Override
                                public void finish(boolean isOk) {
                                    PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.MAST_STATE, Constant.USER_APPLY_STATE.NORMAL);
                                    AccountChangeEvent event = new AccountChangeEvent();
                                    event.type = AccountChangeEvent.MAST_STATE_CHANGE;
                                    EventBus.getDefault().post(event);
                                }
                            });
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
            MultiQnReq req = new MultiQnReq();
            req.setQuantity(selectedPhotos.size());
            ApiManager.getService(getActivity().getApplicationContext()).createMultiQiNiuToken(req, new Callback<QnRes>() {
                @Override
                public void success(QnRes res, Response response) {
                    if (res != null) {
                        upLoadPhotos(res.getTokens());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showInnerError(error);
                }
            });
        } else {
            dismissPd();
            showNetWorkError();
        }

    }

    //上传用户的证书图片到七牛服务器
    private void uploadSinglePhoto(SingleQnRes res,String photoPath) {
        UpLoadUtils.upLoadImage(photoPath, res.getKey(), res.getToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    finishFromSingleThread(true, key);

                } else if (info.isNetworkBroken()) {
                    finishFromSingleThread(false, null);
                } else if (info.isServerError()) {
                    finishFromSingleThread(false, null);
                }
            }
        }, new UploadOptions(null, null, false, null, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return cancel_qiniu;
            }
        }));
    }

    //点击确定按钮后的处理函数
    private void tryToBeSm() {
        if (selectedPhotos.isEmpty()) {
            toBeSm(null);
        } else {
           getUploadToken();
        }

//        if (choose_glory_iv) {
//            //选择了证书图片，需要先进行七牛的图片上传服务，再调用API
//            showPd();
//            getUploadToken();
//        } else {
//            //直接调用API
//            showPd();
//            if (NetUtils.isNetworkConnected(getActivity())) {
//                toBeSm(null);
//            } else {
//                showNetWorkError();
//                dismissPd();
//            }
//
//        }
    }

    //上传相关数据，申请成为达人
    private void toBeSm(String[] certificates) {
        showPd("正在上传申请信息到服务器,请稍等...");
        ToBeSmReq req = new ToBeSmReq();
        req.setGlory(honour);
        req.setImage(avater_code);
        req.setName(realName);
        req.setPhone(phone);
        req.setResume(introduction);
        req.setSex(gender);
        req.setTags(tags);
        req.setWant_cata(skills);
        if (certificates != null) {
            req.setCertificates(certificates);
        } else {
            String[] certificates_em = {};
            req.setCertificates(certificates_em);
        }
        ApiManager.getService(getActivity().getApplicationContext()).applytoSuperMan(req, new Callback<TempRes>() {
            @Override
            public void success(TempRes tempRes, Response response) {
                if (tempRes != null) {
                    dismissPd();
                    PreferenceUtils.putString(getActivity().getApplicationContext(), PreferenceUtils.Key.REALNAME, realName);
                    showInfoWithDialog("申请成功，我们会以短信的方式通知您的申请结果，请等待.", new OnFinish() {
                        @Override
                        public void finish(boolean isOk) {
                            MainActivity activity = (MainActivity) getActivity();
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

    private void showInfoWithDialog(String msg, final OnFinish listener) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null)
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
//        realName = realnameEv.getEditText().getText().toString();
        skilsEv.setErrorEnabled(false);
        tagsEv.setErrorEnabled(false);
//        realnameEv.setErrorEnabled(false);
        if (CommonUtils.isEmpty(tags)) {
            showToast("请先为自己写一个标签");
            return false;
        } else if (tags.length() >= 20) {
            tagsEv.setErrorEnabled(true);
            tagsEv.setError("标签不应大于20个字");
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
//        else if (CommonUtils.isEmpty(realName)) {
//            realnameEv.setErrorEnabled(true);
//            realnameEv.setError("请输入您的真实姓名");
//            return false;
//        }
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

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), String.valueOf(new Date().getTime())));//// TODO: 2015/11/25  删除缓存图片
        Crop.of(source, destination).asSquare().start(getActivity(), ToBeSuperFragment.this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == getActivity().RESULT_OK) {
//            photoPath = Crop.getOutput(result).getPath();
            getUploadToken();
        } else if (resultCode == Crop.RESULT_ERROR) {
            showToast(Crop.getError(result).getMessage());//// TODO: 2015/11/25  失败考虑默认头像
        }
    }

    private synchronized void finishFromSingleThread(boolean result, String key) {
        if (result && key != null) {
            keys.add(key);
            showPd("正在上传您的第" + (keys.size() + 1) + "张证书,请稍等..");
            if (keys.size() == selectedPhotos.size()) {
                toBeSm(keys.toArray(new String[selectedPhotos.size()]));
            }
        } else {
            dismissPd();
            cancel_qiniu = true;
            showInfoWithDialog("上传证书信息到服务器时发生错误,请您稍后再试.", null);
        }

    }

    private void upLoadPhotos(List<SingleQnRes> res) {
        for (int i = 0; i < res.size(); i++) {
            uploadSinglePhoto(res.get(i),selectedPhotos.get(i).getPath());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                selectedPhotos =
                        data.getParcelableArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                adapter.setmPhotos(selectedPhotos);
                adapter.notifyDataSetChanged();
//                Log.i("INFO", "PHOTO:" + photos.get(0));
//                photoPath = photos.get(0).getPath();
//                beginCrop(Uri.fromFile(new File(photoPath)));
            }
        }
//        if (requestCode == Crop.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {
//            beginCrop(data.getData());
//        }
        else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }
}

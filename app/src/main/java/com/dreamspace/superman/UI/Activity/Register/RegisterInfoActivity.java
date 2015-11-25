package com.dreamspace.superman.UI.Activity.Register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bumptech.glide.Glide;
import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.API.SupermanService;
import com.dreamspace.superman.Common.AVImClientManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.Common.UpLoadUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Activity.Main.MainActivity;
import com.dreamspace.superman.event.AccountChangeEvent;
import com.dreamspace.superman.model.UserInfo;
import com.dreamspace.superman.model.api.EmptyBody;
import com.dreamspace.superman.model.api.ErrorRes;
import com.dreamspace.superman.model.api.LoginRes;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.RegisterReq;
import com.dreamspace.superman.model.api.RegisterRes;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.soundcloud.android.crop.Crop;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterInfoActivity extends AbsActivity {
    //// TODO: 2015/11/18 添加图片剪裁 默认
    private static final int TITLE = R.string.title_activity_register_info;
    @Bind(R.id.nickname_ev)
    TextInputLayout nameInput;
    @Bind(R.id.real_name_ev)
    TextInputLayout realNameInput;
    @Bind(R.id.next)
    Button nextBtn;
    private String register_token;
    private String sex;
    private String nickname;
    private String name;
    private String password;
    private String phoneNum;
    private boolean choose_avater = false;
    @Bind(R.id.user_avater_iv)
    CircleImageView mImageView;
    private final static int REQUEST_CODE = 233;
    private ProgressDialog pd;
    private SupermanService mService;
    private String photoPath;
    @Bind(R.id.radiogroup)
    RadioGroup mRadioGroup;
    @Bind(R.id.gender_man)
    RadioButton mMan;
    @Bind(R.id.gender_woman)
    RadioButton mWoman;
    @Bind(R.id.pwd_ed)
    TextInputLayout pwdInput;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_register_info);
    }

    @Override
    protected void prepareDatas() {
        mService = ApiManager.getService(getApplicationContext());
        register_token = this.getIntent().getStringExtra("token");
        phoneNum=this.getIntent().getStringExtra("phoneNum");
    }

    @OnClick({R.id.gender_man, R.id.gender_woman})
     void getGenderInfo() {
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.gender_man:
                sex = Constant.MALE;
                break;
            case R.id.gender_woman:
                sex = Constant.FEMALE;
                break;
        }
        Log.i("INFO", "genderInfo: " + sex);
    }
   private void showPd(){
       if(pd==null){
           pd=ProgressDialog.show(this,"","正在加载中,请稍后",true,false);
       }else{
           pd.show();
       }
   }
    private void dismissPd(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }
    @Override
    protected void initViews() {
        getSupportActionBar().setTitle(getString(TITLE));
        getGenderInfo();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameInput.getEditText().getText().toString();
                String realName = realNameInput.getEditText().getText().toString();
                String pwd=pwdInput.getEditText().getText().toString();
                pwdInput.setErrorEnabled(false);
                nameInput.setErrorEnabled(false);
                realNameInput.setErrorEnabled(false);
                String reason="none";
                if(!pwdValid(pwd,reason)){
                    pwdInput.setErrorEnabled(true);
                    pwdInput.setError(reason);
                }
                if (!nameValid(userName)) {
                    nameInput.setErrorEnabled(true);
                    nameInput.setError("请输入您的用户名");
                } else if (!realNameValid(realName)) {
                    nameInput.setErrorEnabled(true);
                    realNameInput.setError("请输入您的真实姓名");
                } else if (!choose_avater) {
                    showToast("请点击图片选择您的头像");
                } else {
                    nickname = userName;
                    name = realName;
                    nameInput.setErrorEnabled(false);
                    realNameInput.setErrorEnabled(false);
                    password=pwd;
                    showPd();
                    getUploadToken();
                }
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(RegisterInfoActivity.this);
//                PhotoPickerIntent intent = new PhotoPickerIntent(RegisterInfoActivity.this);
//                intent.setPhotoCount(1);
//                intent.setShowCamera(true);
//                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    //获得七牛（第三方服务）的上传资源的凭证
    private void getUploadToken() {
        if (NetUtils.isNetworkConnected(RegisterInfoActivity.this)) {
            EmptyBody body=new EmptyBody();
            body.setInfo(Constant.FEMALE);
            mService.createQiNiuToken(body,new Callback<QnRes>() {
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

    //上传用户的头像到七牛服务器
    private void uploadPhoto(QnRes res) {
        UploadManager manager = UpLoadUtils.getInstance();
        manager.put(photoPath, res.getKey(), res.getToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    Log.i("INFO", "upload is Ok");
                    RegisterReq req = new RegisterReq();
                    req.setImage(key);
                    req.setName(name);
                    req.setNickname(nickname);
                    req.setRegister_token(register_token);
                    req.setSex(sex);
                    req.setPassword(password);
                    register(req);

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

    //上传用户信息到业务服务器
    private void register(final RegisterReq req) {
        Log.i("INFO","req: "+req);
        mService.register(req, new Callback<LoginRes>() {
            @Override
            public void success(LoginRes res, Response response) {
                if (res != null) {
                    PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.ACCESS, res.getAccess_token());
                    PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.QINIU_SOURCE, req.getImage());
                    ApiManager.clear();
                    getUserInfo();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissPd();
                showInnerError(error);
            }
        });
    }
    //获取用户信息
    private void getUserInfo(){
        ApiManager.getService(getApplicationContext()).getUserInfo(new Callback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo, Response response) {
                saveUserInfo(userInfo);
                openChatService(userInfo.getId());
            }

            @Override
            public void failure(RetrofitError error) {
                showInnerError(error);
                dismissPd();
            }
        });
    }
    //使用leancloud打开聊天服务
    private void openChatService(String userId){
        AVImClientManager.getInstance().open(userId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if(filterException(e)){
                    dismissPd();
                    EventBus.getDefault().post(new AccountChangeEvent());
                    finish();
                }else{
                    showToast("聊天功能暂时不可用");
                    dismissPd();
                    EventBus.getDefault().post(new AccountChangeEvent());
                    finish();
                }

            }
        });
    }

    //保存用户信息到本地
    private void saveUserInfo(UserInfo userInfo) {
        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.ACCOUNT,userInfo.getNickname());
        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.AVATAR,userInfo.getImage());
        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.REALNAME,userInfo.getName());
        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.SEX, userInfo.getSex());
        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.PHONE,phoneNum);
        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.UID,userInfo.getId());
        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.MAST_STATE,userInfo.getMast_state());
        if(!CommonUtils.isEmpty(userInfo.getMas_id())){
            PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.MAS_ID,userInfo.getMas_id());
        }
    }

    private boolean nameValid(String name) {
        return !(name.isEmpty() || name == null);
    }
    private boolean pwdValid(String pwd,String reason){
       if(CommonUtils.isEmpty(pwd)){
           reason="请输入密码";
           return false;
       }else if(pwd.length()<6){
           reason="密码长度不足";
           return false;
       }
        return true;
    }
    private boolean realNameValid(String realName) {
        return !(realName.isEmpty() || realName == null);
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));//// TODO: 2015/11/25  删除缓存图片
        Crop.of(source, destination).asSquare().start(this);
    }
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            photoPath=Crop.getOutput(result).getPath();
            Log.i("info",photoPath);
            Tools.showImageWithGlide(this,mImageView,photoPath);
            choose_avater = true;
//            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
          showToast(Crop.getError(result).getMessage());//// TODO: 2015/11/25  失败考虑默认头像
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }

//        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            if (data != null) {
//                ArrayList<String> photos =
//                        data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
//                Log.i("INFO", "PHOTO:" + photos.get(0));
//                photoPath = photos.get(0);
//                Tools.showImageWithGlide(this,mImageView,photoPath);
//                choose_avater = true;
//            }
//        }
    }
}

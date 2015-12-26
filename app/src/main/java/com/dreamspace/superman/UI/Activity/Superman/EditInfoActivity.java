package com.dreamspace.superman.UI.Activity.Superman;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.UpLoadUtils;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.AbsActivity;
import com.dreamspace.superman.UI.Adapters.MultiShowIvAdapter;
import com.dreamspace.superman.model.api.LatestMasterInfo;
import com.dreamspace.superman.model.api.MultiQnReq;
import com.dreamspace.superman.model.api.QnRes;
import com.dreamspace.superman.model.api.SingleQnRes;
import com.dreamspace.superman.model.api.SmModifyReq;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditInfoActivity extends AbsActivity implements View.OnClickListener {


    @Bind(R.id.sm_tag_tv)
    TextView smTagTv;
    @Bind(R.id.superman_tag_layout)
    RelativeLayout supermanTagLayout;
    @Bind(R.id.sm_glory_tv)
    TextView smGloryTv;
    @Bind(R.id.superman_glory_layout)
    RelativeLayout supermanGloryLayout;
    @Bind(R.id.sm_intro_tv)
    TextView smIntroTv;
    @Bind(R.id.superman_intro_layout)
    RelativeLayout supermanIntroLayout;
    @Bind(R.id.superman_certificate_layout)
    RelativeLayout supermanCertificateLayout;
    @Bind(R.id.glory_rv)
    RecyclerView gloryRv;
    @Bind(R.id.state_info_layout)
    LinearLayout warningView;
    private static final String EDIT_GLORY_TITLE = "编辑所获荣誉";
    private static final String EDIT_INTRO_TITLE = "编辑个人简介";
    private static final String EDIT_TAG_TITLE = "编辑达人标签";
    public static final String TITLE_INFO = "title_info";//Bundle中的key，用于传递修改详情界面的标题
    public static final String PRE_INFO = "old_info";//Bundle中的key，用于传递修改详情界面的标题
    public static final String MODIFIED_INFO = "new_info";//Bundle中的key，用于获取由详情界面传回的新的信息
    public static final int MODIFY_TAG_INFO = 235;
    public static final int MODIFY_GLORY_INFO = 236;
    public static final int MODIFY_INTRO_INFO = 237;
    private MultiShowIvAdapter adapter;
    private List<Photo> mPhotos = new ArrayList<>();
    private final static int MODIFY_CERTIFATE = 233;
    private final static String apply_success_msg = "您的信息已经提交到后台进行审核,审核通过后便可以展示";
    private final static String warn_info = "在您点击确认后,您的达人资料修改将会提交到后台进行审核.(在您收到短信通知修改成功前,您可以反复修改,我们会以审核前的最近一次修改为准,审核大概一天左右.)";
    private ProgressDialog pd;
    private boolean cancel_qiniu = false;
    private List<String> keys = new ArrayList<>();//存储七牛的key，用于修改达人信息的接口
    private int localPhotos = 0;
    private String new_tag;
    private String new_intro;
    private String new_glory;

    @Override
    protected void setSelfContentView() {
        setContentView(R.layout.activity_edit_info);
    }

    @Override
    protected void prepareDatas() {

    }

    @Override
    protected void initViews() {
        supermanTagLayout.setOnClickListener(this);
        supermanGloryLayout.setOnClickListener(this);
        supermanIntroLayout.setOnClickListener(this);
        adapter = new MultiShowIvAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        gloryRv.setAdapter(adapter);
        gloryRv.setLayoutManager(manager);
        loadLatestInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_finish) {
            new_tag = smTagTv.getText().toString();
            new_intro = smIntroTv.getText().toString();
            new_glory = smGloryTv.getText().toString();
            if (CommonUtils.isEmpty(new_tag)) {
                showToast("请先填写达人标签信息");
            } else if (CommonUtils.isEmpty(new_intro)) {
                showToast("请先填写个人简介信息");
            } else {
                showInfoByDialog(warn_info, "确定", "取消", new DialogClickListener() {
                    @Override
                    public void onClick(boolean positive) {
                        if (positive) {
                            localPhotos = getLocalPhotoCount();
                            if (adapter.getPhotos().size() == 0 || localPhotos == 0) {
                                applyModifyInfo(null);
                            } else {
                                showPd(null);
                                getUploadToken();
                            }

                        }
                    }
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private int getLocalPhotoCount() {
        int count = 0;
        for (Photo photo : adapter.getPhotos()) {
            if (photo.isLocal())
                count++;
        }
        return count;
    }

    private String[] getLocalPhotoPaths() {
        List<String> paths = new ArrayList<>();
        List<Photo> localPhotos = new ArrayList<>();
        for (Photo photo : adapter.getPhotos()) {
            if (photo.isLocal()) {
                paths.add(photo.getPath());
            }
        }
        return paths.toArray(new String[paths.size()]);
    }

    //获得七牛（第三方服务）的上传资源的凭证
    private void getUploadToken() {
        if (NetUtils.isNetworkConnected(this)) {
            MultiQnReq req = new MultiQnReq();
            req.setQuantity(localPhotos);
            ApiManager.getService(getApplicationContext()).createMultiQiNiuToken(req, new Callback<QnRes>() {
                @Override
                public void success(QnRes res, Response response) {
                    if (res != null) {
                        upLoadPhotos(res.getTokens());
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

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void upLoadPhotos(List<SingleQnRes> res) {
        String[] localPaths = getLocalPhotoPaths();
        for (int i = 0; i < res.size(); i++) {
            uploadSinglePhoto(res.get(i), localPaths[i]);
        }

    }

    private void uploadSinglePhoto(SingleQnRes res, String photoPath) {
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

    private synchronized void finishFromSingleThread(boolean result, String key) {
        if (result && key != null) {
            keys.add(key);
            showPd("正在上传您的第" + (keys.size() + 1) + "张证书,请稍等..");
            if (keys.size() == localPhotos) {
                if (localPhotos != adapter.getPhotos().size()) {
                    for (Photo photo : adapter.getPhotos()) {
                        if (!photo.isLocal()) {
                            keys.add(photo.getPath());
                        }
                    }
                }
                applyModifyInfo(keys.toArray(new String[keys.size()]));
            }
        } else {
            dismissPd();
            cancel_qiniu = true;
            showInfoByDialog("上传证书信息到服务器时发生错误,请您稍后再试.", "确定", null, null);
        }

    }

    private void showPd(String text) {
        if (text == null) {
            text = getString(R.string.common_loading_message);
        }
        if (pd == null) {
            pd = ProgressDialog.show(this, "", text, true, false);
        } else {
            if (!pd.isShowing()) {
                pd.show();
            } else {
                pd.setMessage(text);
            }
        }

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        String title = null;
        int request_code = 0;
        String old_info = null;
        switch (v.getId()) {
            case R.id.superman_intro_layout:
                title = EDIT_INTRO_TITLE;
                request_code = MODIFY_INTRO_INFO;
                old_info = smIntroTv.getText().toString();
                break;
            case R.id.superman_tag_layout:
                title = EDIT_TAG_TITLE;
                request_code = MODIFY_TAG_INFO;
                old_info = smTagTv.getText().toString();
                break;
            case R.id.superman_glory_layout:
                title = EDIT_GLORY_TITLE;
                request_code = MODIFY_GLORY_INFO;
                old_info = smGloryTv.getText().toString();
                break;
        }
        Bundle b = new Bundle();
        b.putString(TITLE_INFO, title);
        b.putString(PRE_INFO, old_info);
        readyGoForResult(EditDetailInfoActivity.class, request_code, b);
    }

    /**
     * 获取最近一次达人提交的申请信息,根据state字段判断是否已经通过审核
     */
    private void loadLatestInfo() {
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(getApplicationContext()).getLatestMasterInfo(new Callback<LatestMasterInfo>() {
                @Override
                public void success(LatestMasterInfo latestMasterInfo, Response response) {
                    if (latestMasterInfo != null) {
                        setDateIntoView(latestMasterInfo);
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

    /**
     * 提交修改信息的请求
     */
    private void applyModifyInfo(String[] keys) {
        if (keys == null) {
            keys = new String[0];
        }
        SmModifyReq req = new SmModifyReq();
        req.setCertificates(keys);
        req.setGlory(new_glory);
        req.setResume(new_intro);
        req.setTags(new_tag);
        if (NetUtils.isNetworkConnected(this)) {
            ApiManager.getService(getApplicationContext()).ApplyModifyInfoBySm(req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    if (response != null) {
                        showInfoByDialog(apply_success_msg, "确定", null, new DialogClickListener() {
                            @Override
                            public void onClick(boolean positive) {
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

    private void showInfoByDialog(String info, String positiveMsg, String negativeMsg, final DialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(info)
                .setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(true);
                    }
                });
        if (negativeMsg != null) {
            builder.setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onClick(false);
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private interface DialogClickListener {
        void onClick(boolean positive);
    }

    private void setDateIntoView(LatestMasterInfo latestMasterInfo) {
        if (latestMasterInfo == null) {
            return;
        }
        if (latestMasterInfo.getState().equals(LatestMasterInfo.PENDING)) {
            warningView.setVisibility(View.VISIBLE);
        } else {
            warningView.setVisibility(View.GONE);
        }
        smTagTv.setText(latestMasterInfo.getTags());
        smGloryTv.setText(latestMasterInfo.getGlory());
        smIntroTv.setText(latestMasterInfo.getResume());
        for (String certificate : latestMasterInfo.getCertificates()) {
            Photo photo = new Photo();
            photo.setLocal(false);
            photo.setPath(certificate);
            mPhotos.add(photo);
        }
        adapter.setmPhotos(mPhotos);
        adapter.setPhotoClickListener(new MultiShowIvAdapter.onPhotoClickListener() {
            @Override
            public void onPhotoClick() {
                int maxFromLocal = getMaxPhotoCountFromLocal();
                if (maxFromLocal == 0) {
                    showToast("您最多只能选择4张证书照片");
                    return;
                }
                PhotoPickerIntent intent = new PhotoPickerIntent(EditInfoActivity.this);
                intent.setPhotoCount(maxFromLocal);
                intent.setShowCamera(true);
                intent.setSeletedPhotos(adapter.getPhotos());
                startActivityForResult(intent, MODIFY_CERTIFATE);

            }
        });

    }

    private int getMaxPhotoCountFromLocal() {
        int from_net = 0;
        for (Photo photo : adapter.getPhotos()) {
            if (!photo.isLocal())
                from_net++;
        }
        return 4 - from_net;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String new_info = null;
        if (requestCode != MODIFY_CERTIFATE) {
            if (data != null) {
                new_info = data.getStringExtra(MODIFIED_INFO);
            }
            if (!CommonUtils.isEmpty(new_info)) {
                switch (requestCode) {
                    case MODIFY_GLORY_INFO:
                        smGloryTv.setText(new_info);
                        break;
                    case MODIFY_TAG_INFO:
                        smTagTv.setText(new_info);
                        break;
                    case MODIFY_INTRO_INFO:
                        smIntroTv.setText(new_info);
                        break;
                }

            }
        } else {
            if (data != null) {
                mPhotos =
                        data.getParcelableArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                adapter.setmPhotos(mPhotos);
//                adapter.notifyDataSetChanged();
//                Log.i("INFO", "PHOTO:" + photos.get(0));
//                photoPath = photos.get(0).getPath();
//                beginCrop(Uri.fromFile(new File(photoPath)));
            }

        }

    }

}

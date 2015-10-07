package com.dreamspace.superman.UI.Fragment.Drawer;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.PreferenceUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Fragment.Base.BaseLazyFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private String gender;//性别，从本地数据中读取
    private String avater_code;//七牛的原始code，从本地数据中读取
    private String avater_url;
    private String tags;//个人标签
    private String honour;//荣誉
    private String realName;//真实姓名，从本地数据中读取
    private String skills;//技能
    private String phone;//电话，从本地数据中读取
    private String introduction;//介绍

    @Override
    protected void onFirstUserVisible() {
        genderMan.setEnabled(false);
        genderWoman.setEnabled(false);
        Tools.showImageWithGlide(getActivity(), userIv, avater_url);
        showGender();

    }

    @Override
    protected void onUserVisible() {

    }

    private void showGender() {
        if(gender.equals(Constant.FEMALE)){
            genderWoman.setSelected(true);
        }else{
            genderMan.setSelected(true);
        }
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        loadFromLocal();
    }

    private void loadFromLocal() {
        gender = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.SEX);
        avater_code = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.QINIU_SOURCE);
        phone = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.PHONE);
        realName = PreferenceUtils.getString(getActivity().getApplicationContext(), PreferenceUtils.Key.REALNAME);
        avater_url=PreferenceUtils.getString(getActivity().getApplicationContext(),PreferenceUtils.Key.AVATAR);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){

                }
            }
        });
    }

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
}

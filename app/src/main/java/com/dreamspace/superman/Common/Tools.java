package com.dreamspace.superman.Common;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.api.ErrorRes;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.RetrofitError;

/**
 * Created by Wells on 2015/9/21.
 */
public class Tools {
    //使用Glide加载网络图片
    public static void showImageWithGlide(Context context, final CircleImageView imageView,String url){
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.login_pho)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }
    //使用Glide加载网络图片
    public static void showImageWithGlide(Context context, final ImageView imageView,String url){
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.login_pho)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }
    //按照协议解析服务器返回的错误原因
    public static String getInnerErrorInfo(RetrofitError error){
        ErrorRes res= (ErrorRes) error.getBodyAs(ErrorRes.class);
        if(res!=null){
            return  res.getReason();
        }else {
            return "暂时不能提交您的请求,请稍后再试";
        }

    }

}

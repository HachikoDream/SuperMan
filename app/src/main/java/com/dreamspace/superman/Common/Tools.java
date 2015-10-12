package com.dreamspace.superman.Common;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dreamspace.superman.R;

import de.hdodenhof.circleimageview.CircleImageView;

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
}

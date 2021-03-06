package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dreamspace.superman.R;
import com.jauker.widget.BadgeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.Photo;

/**
 * Created by Wells on 2015/12/20.
 * 多张荣誉证书图片展示的adapter。对应成为达人页面
 */
public class MultiShowIvAdapter extends RecyclerView.Adapter<MultiShowIvAdapter.MultiShowviewHolder> {

    private List<Photo> mPhotos;//展示的图片路径集合
    private Context mContext;
    private LayoutInflater inflater;
    public final static int ITEM_TYPE_ADD = 100;
    public final static int ITEM_TYPE_PHOTO = 101;
    private onPhotoClickListener photoClickListener = null;
    private final int imageSize;
    private boolean show_delete_icon = true;
    private boolean show_add_icon = true;

    public MultiShowIvAdapter(List<Photo> mPhotos, Context mContext) {
        this.mPhotos = mPhotos;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;

        imageSize = widthPixels / 3;
    }

    public MultiShowIvAdapter(Context mContext) {
        this.mPhotos = new ArrayList<>();
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;

        imageSize = widthPixels / 3;
    }

    public void setShow_delete_icon(boolean show_delete_icon) {
        this.show_delete_icon = show_delete_icon;
    }

    public void setShow_add_icon(boolean show_add_icon) {
        this.show_add_icon = show_add_icon;
    }

    public boolean isShow_add_icon() {
        return show_add_icon;
    }

    public boolean isShow_delete_icon() {
        return show_delete_icon;
    }

    public void setPhotoClickListener(onPhotoClickListener photoClickListener) {
        this.photoClickListener = photoClickListener;
    }

    @Override
    public MultiShowviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycleview_item, parent, false);
        MultiShowviewHolder holder = new MultiShowviewHolder(itemView, mContext);
        if (viewType == ITEM_TYPE_ADD || !show_delete_icon) {
            holder.ivDelete.setVisibility(View.GONE);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final MultiShowviewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {
            Photo photo = mPhotos.get(position);
            if (photo.isLocal()) {
                Glide.with(mContext)
                        .load(new File(photo.getPath()))
                        .centerCrop()
                        .dontAnimate()
                        .thumbnail(0.5f)
//                    .override(imageSize, imageSize)
                        .placeholder(me.iwf.photopicker.R.drawable.ic_photo_black_48dp)
                        .error(me.iwf.photopicker.R.drawable.ic_broken_image_black_48dp)
                        .into(holder.ivPhoto);

            } else {
                Glide.with(mContext)
                        .load(photo.getPath())
                        .centerCrop()
                        .dontAnimate()
                        .thumbnail(0.5f)
//                    .override(imageSize, imageSize)
                        .placeholder(me.iwf.photopicker.R.drawable.ic_photo_black_48dp)
                        .error(me.iwf.photopicker.R.drawable.ic_broken_image_black_48dp)
                        .into(holder.ivPhoto);

            }
        } else if (getItemViewType(position) == ITEM_TYPE_ADD) {
            Glide.with(mContext)
                    .load(R.drawable.page_add)
//                    .override(imageSize, imageSize)
                    .into(holder.ivPhoto);
//            holder.ivPhoto.setImageResource(R.drawable.page_add);
        }
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (photoClickListener != null) {
                    photoClickListener.onPhotoClick(v, position);
                }
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotos.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position == mPhotos.size() ? ITEM_TYPE_ADD : ITEM_TYPE_PHOTO;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setmPhotos(List<Photo> mPhotos) {
        this.mPhotos = mPhotos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return show_add_icon ? mPhotos.size() + 1 : mPhotos.size();
    }

    public static class MultiShowviewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private BadgeView ivDelete;

        public MultiShowviewHolder(View itemView, Context context) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.content_imageview);
            ivDelete = new BadgeView(context);
            ivDelete.setTargetView(ivPhoto);
            ivDelete.setText("X");
            ivDelete.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
            ivDelete.setBackgroundColor(context.getResources().getColor(R.color.navi_color));
        }

    }

    public interface onPhotoClickListener {
        void onPhotoClick(View v, int position);
    }

    public List<String> getCurrentPhotoPaths() {
        List<String> photoPaths = new ArrayList<>();
        if (mPhotos == null) {
            return photoPaths;
        }
        for (Photo photo : mPhotos) {
            photoPaths.add(photo.getPath());
        }
        return photoPaths;
    }

}

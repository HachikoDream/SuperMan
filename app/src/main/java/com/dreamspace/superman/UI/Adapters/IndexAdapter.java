package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.api.LessonInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class IndexAdapter extends BasisAdapter<LessonInfo, IndexAdapter.viewHolder> {


    public IndexAdapter(Context context){
        super(context,new ArrayList<LessonInfo>(),viewHolder.class);
    }
    public IndexAdapter(List<LessonInfo> mLessonList, Context context) {
        super(context, mLessonList, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(final viewHolder holder, LessonInfo entity) {
        holder.lessonNameTv.setText(entity.getLess_name());
        holder.smNameTv.setText(entity.getName());
        holder.wantCountTv.setText(String.valueOf(entity.getCollection_count()));
        holder.successCountTv.setText(String.valueOf(entity.getSuccess_count()));
        holder.priceTv.setText(String.valueOf(entity.getPrice()));
        holder.descTv.setText(entity.getDescription());
        Glide.with(getmContext())
                .load(entity.getImage())
                .placeholder(R.drawable.login_pho)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder.avaterIv.setImageDrawable(resource);
                    }
                });
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.lessonNameTv = (TextView) convertView.findViewById(R.id.course_name_tv);
        holder.smNameTv= (TextView) convertView.findViewById(R.id.smname_tv);
        holder.wantCountTv= (TextView) convertView.findViewById(R.id.want_meet_num_tv);
        holder.successCountTv= (TextView) convertView.findViewById(R.id.success_meet_num_tv);
        holder.priceTv= (TextView) convertView.findViewById(R.id.price_tv);
        holder.avaterIv= (CircleImageView) convertView.findViewById(R.id.profile_image);
        holder.descTv= (TextView) convertView.findViewById(R.id.desc_tv);
    }

    @Override
    public int getItemLayout() {
        return R.layout.index_list_item;
    }

    public static class viewHolder {

        public TextView lessonNameTv;
        public TextView smNameTv;
        public TextView wantCountTv;
        public TextView successCountTv;
        public TextView priceTv;
        public CircleImageView avaterIv;
        public TextView descTv;

    }

}


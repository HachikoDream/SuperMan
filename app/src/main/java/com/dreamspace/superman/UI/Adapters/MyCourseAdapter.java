package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Lesson;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/10 0010.
 */
public class MyCourseAdapter extends BasisAdapter<Lesson,MyCourseAdapter.viewHolder> {

    public MyCourseAdapter(Context mContext){
        this(mContext,new ArrayList<Lesson>());
    }
    public MyCourseAdapter(Context mContext, List<Lesson> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, Lesson entity) {
       holder.courseNameTv.setText(entity.getCourseName());
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.avaterIv= (CircleImageView) convertView.findViewById(R.id.profile_image);
        holder.courseNameTv= (TextView) convertView.findViewById(R.id.course_name_tv);
        holder.priceTv= (TextView) convertView.findViewById(R.id.course_price);
        holder.supermanTv=(TextView)convertView.findViewById(R.id.username_tv);
        holder.timeTv=(TextView)convertView.findViewById(R.id.time_tv);
    }

    @Override
    public int getItemLayout() {
        return R.layout.my_course_list_item;
    }

    public static class viewHolder{
        public TextView courseNameTv;
        public TextView supermanTv;
        public TextView priceTv;
        public TextView timeTv;
        public CircleImageView avaterIv;

    }
}

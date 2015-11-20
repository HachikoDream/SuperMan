package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Lesson;
import com.dreamspace.superman.model.api.LessonInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/10 0010.
 */
public class MyCourseAdapter extends BasisAdapter<LessonInfo,MyCourseAdapter.viewHolder> {

    public MyCourseAdapter(Context mContext){
        this(mContext,new ArrayList<LessonInfo>());
    }
    public MyCourseAdapter(Context mContext, List<LessonInfo> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, LessonInfo entity) {
        Tools.showImageWithGlide(getmContext(),holder.avaterIv,entity.getImage());
        holder.courseNameTv.setText(entity.getLess_name());
        holder.priceTv.setText(CommonUtils.getPriceWithInfo(entity.getPrice()));
        holder.timeTv.setText(entity.getKeeptime());
        if(entity.getState().equals("off")){
            holder.courseBg.setBackgroundColor(getmContext().getResources().getColor(R.color.grey_line));
        }else{
            holder.courseBg.setBackgroundColor(getmContext().getResources().getColor(R.color.near_white));
        }
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.avaterIv= (CircleImageView) convertView.findViewById(R.id.profile_image);
        holder.courseNameTv= (TextView) convertView.findViewById(R.id.course_name_tv);
        holder.priceTv= (TextView) convertView.findViewById(R.id.course_price);
        holder.timeTv=(TextView)convertView.findViewById(R.id.time_tv);
        holder.courseBg=(LinearLayout)convertView.findViewById(R.id.course_bg);
    }

    @Override
    public int getItemLayout() {
        return R.layout.my_course_list_item;
    }

    public static class viewHolder{
        public TextView courseNameTv;
        public TextView priceTv;
        public TextView timeTv;
        public CircleImageView avaterIv;
        public LinearLayout courseBg;

    }
}

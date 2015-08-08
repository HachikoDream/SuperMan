package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/1 0001.
 */
public class CommentAdapter extends BasisAdapter<Comment,CommentAdapter.viewHolder> {

    public CommentAdapter(Context mContext, List<Comment> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, Comment entity) {
        holder.contentTv.setText(entity.getComment());
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.contentTv= (TextView) convertView.findViewById(R.id.content_tv);
        holder.userNameTv= (TextView) convertView.findViewById(R.id.username_tv);
        holder.timeTv= (TextView) convertView.findViewById(R.id.time_tv);
        holder.relatedCourseTv=(TextView)convertView.findViewById(R.id.course_name_tv);
        holder.userAvaterIv=(CircleImageView)convertView.findViewById(R.id.profile_image);
    }

    @Override
    public int getItemLayout() {
        return R.layout.student_comment_list_item;
    }

    public static class viewHolder{
        public TextView contentTv;
        public TextView userNameTv;
        public TextView timeTv;
        public TextView relatedCourseTv;
        public CircleImageView userAvaterIv;
    }
}


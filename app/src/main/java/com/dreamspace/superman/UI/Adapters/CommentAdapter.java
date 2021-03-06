package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.Common.DataUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Comment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/1 0001.
 */
public class CommentAdapter extends BasisAdapter<Comment,CommentAdapter.viewHolder> {

    public CommentAdapter(Context mContext){
        this(mContext,new ArrayList<Comment>());
    }
    public CommentAdapter(Context mContext, List<Comment> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, Comment entity) {
        holder.contentTv.setText(entity.getContent());
        holder.timeTv.setText(DataUtils.fetchDateDeleteTime(entity.getTime()));
        Tools.showImageWithGlide(getmContext(),holder.userAvaterIv,entity.getImage());
        holder.userNameTv.setText(entity.getNickname());
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.contentTv= (TextView) convertView.findViewById(R.id.content_tv);
        holder.userNameTv= (TextView) convertView.findViewById(R.id.username_tv);
        holder.timeTv= (TextView) convertView.findViewById(R.id.time_tv);
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
        public CircleImageView userAvaterIv;
    }
}


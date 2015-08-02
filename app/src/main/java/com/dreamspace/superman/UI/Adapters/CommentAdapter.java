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
public class CommentAdapter extends BaseAdapter {
    private List<Comment> mCommentList;
    private Context mContext;

    public CommentAdapter(List<Comment> mCommentList, Context mContext) {
        this.mCommentList = mCommentList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.student_comment_list_item,parent,false);
            holder=new viewHolder();
            holder.contentTv= (TextView) convertView.findViewById(R.id.content_tv);
            holder.userNameTv= (TextView) convertView.findViewById(R.id.username_tv);
            holder.timeTv= (TextView) convertView.findViewById(R.id.time_tv);
            holder.relatedCourseTv=(TextView)convertView.findViewById(R.id.course_name_tv);
            holder.userAvaterIv=(CircleImageView)convertView.findViewById(R.id.profile_image);
            convertView.setTag(holder);
        }else{
            holder= (viewHolder) convertView.getTag();
        }
         Comment comment=mCommentList.get(position);
         holder.contentTv.setText(comment.getComment());
        return convertView;
    }
    public class viewHolder{
        public TextView contentTv;
        public TextView userNameTv;
        public TextView timeTv;
        public TextView relatedCourseTv;
        public CircleImageView userAvaterIv;
    }
}


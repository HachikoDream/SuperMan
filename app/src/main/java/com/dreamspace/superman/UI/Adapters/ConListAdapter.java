package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.ConList;
import com.ds.greendao.Conversation;
import com.ds.greendao.ConversationDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/17 0017.
 */
public class ConListAdapter extends CursorAdapter {


    public ConListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.avaterIv = (CircleImageView) convertView.findViewById(R.id.profile_image);
        holder.latestContentTv = (TextView) convertView.findViewById(R.id.content_tv);
        holder.nameTv= (TextView) convertView.findViewById(R.id.username_tv);
        holder.timeTv= (TextView) convertView.findViewById(R.id.time_tv);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        viewHolder holder=new viewHolder();
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.view_con_list_item,parent,false);
        initViewHolder(view,holder);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewHolder holder= (viewHolder) view.getTag();
        String memeberName=cursor.getString(cursor.getColumnIndex(ConversationDao.Properties.MemberName.columnName));
        String avaterUrl=cursor.getString(cursor.getColumnIndex(ConversationDao.Properties.MemberAvater.columnName));
        int memberId=cursor.getInt(cursor.getColumnIndex(ConversationDao.Properties.MemberId.columnName));
        boolean isRead=cursor.getInt(cursor.getColumnIndex(ConversationDao.Properties.IsRead.columnName))>0;
        String chatTime=cursor.getString(cursor.getColumnIndex(ConversationDao.Properties.ChatTime.columnName));
        String latestContent=cursor.getString(cursor.getColumnIndex(ConversationDao.Properties.LastContent.columnName));
        Tools.showImageWithGlide(context,holder.avaterIv,avaterUrl);
        holder.latestContentTv.setText(latestContent);
        holder.nameTv.setText(memeberName);
        holder.timeTv.setText(chatTime);
    }

    //    return R.layout.view_con_list_item;
    public static class viewHolder {
        public TextView nameTv;
        public TextView latestContentTv;
        public TextView timeTv;
        public CircleImageView avaterIv;

    }
}

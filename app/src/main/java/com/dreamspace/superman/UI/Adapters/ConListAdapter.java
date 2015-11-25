package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamspace.superman.Common.DataUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.ds.greendao.Conversation;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/17 0017.
 */
public class ConListAdapter extends BasisAdapter<Conversation, ConListAdapter.viewHolder> {

    public ConListAdapter(Context mContext) {
        this(mContext, new ArrayList<Conversation>());
    }

    public ConListAdapter(Context mContext, List<Conversation> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, Conversation entity) {
        holder.timeTv.setText(DataUtils.getTag(entity.getChatTime()));
        holder.nameTv.setText(entity.getMemberName());
        holder.latestContentTv.setText(entity.getLastContent());
        Tools.showImageWithGlide(getmContext(),holder.avaterIv,entity.getMemberAvater());
        boolean isRead=entity.getIsRead();
        if(isRead){
            holder.contentBg.setBackgroundColor(getmContext().getResources().getColor(R.color.main_bg));
        }else{
            holder.contentBg.setBackgroundColor(getmContext().getResources().getColor(R.color.grey_line));
        }
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.avaterIv = (CircleImageView) convertView.findViewById(R.id.user_avater_iv);
        holder.latestContentTv = (TextView) convertView.findViewById(R.id.content_tv);
        holder.nameTv= (TextView) convertView.findViewById(R.id.username_tv);
        holder.timeTv= (TextView) convertView.findViewById(R.id.time_tv);
        holder.contentBg=(LinearLayout)convertView.findViewById(R.id.con_bg);
    }
    public void refreshDate(List<Conversation> mCon){
        setmEntities(mCon);
        notifyDataSetChanged();
    }
    @Override
    public int getItemLayout() {
        return R.layout.view_con_list_item;
    }

    public static class viewHolder {
        public TextView nameTv;
        public TextView latestContentTv;
        public TextView timeTv;
        public CircleImageView avaterIv;
        public LinearLayout contentBg;
    }
}

package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.ConList;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/17 0017.
 */
public class ConListAdapter extends BasisAdapter<ConList, ConListAdapter.viewHolder> {

    public ConListAdapter(Context mContext) {
        this(mContext, new ArrayList<ConList>());
    }

    public ConListAdapter(Context mContext, List<ConList> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, ConList entity) {
//        holder.timeTv.setText(entity.getTime());
//        holder.nameTv.setText(entity.getName());
        holder.latestContentTv.setText(entity.getLatestContent());
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.avaterIv = (CircleImageView) convertView.findViewById(R.id.profile_image);
        holder.latestContentTv = (TextView) convertView.findViewById(R.id.content_tv);
        holder.nameTv= (TextView) convertView.findViewById(R.id.username_tv);
        holder.timeTv= (TextView) convertView.findViewById(R.id.time_tv);
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

    }
}

package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.api.SmInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Wells on 2016/2/24.
 */
public class SmListAdapter extends BasisAdapter<SmInfo, SmListAdapter.ViewHolder> {

    public static final String WANT_POST_STRING = "人想见";

    public SmListAdapter(Context mContext, List<SmInfo> mEntities) {
        super(mContext, mEntities, ViewHolder.class);
    }

    public SmListAdapter(Context mContext) {
        super(mContext, new ArrayList<SmInfo>(), ViewHolder.class);
    }

    public static class ViewHolder {
        public TextView smNameTv;
        public TextView smTagTv;
        public TextView smWantTv;
        public CircleImageView smIv;
    }


    @Override
    protected void setDataIntoView(ViewHolder holder, SmInfo entity) {
        Tools.showImageWithGlide(getmContext(), holder.smIv, entity.getImage());
        holder.smWantTv.setText(entity.getCollection_count() + WANT_POST_STRING);
        holder.smNameTv.setText(entity.getName());
        holder.smTagTv.setText(entity.getTags());
    }

    @Override
    protected void initViewHolder(View convertView, ViewHolder holder) {
        holder.smIv = (CircleImageView) convertView.findViewById(R.id.profile_image);
        holder.smNameTv = (TextView) convertView.findViewById(R.id.superman_name_tv);
        holder.smTagTv = (TextView) convertView.findViewById(R.id.superman_tag_tv);
        holder.smWantTv = (TextView) convertView.findViewById(R.id.want_meet_num_tv);
    }

    @Override
    public int getItemLayout() {
        return R.layout.superman_list_item;
    }
}

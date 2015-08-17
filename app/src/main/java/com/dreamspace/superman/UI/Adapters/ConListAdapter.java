package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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

    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {

    }

    @Override
    public int getItemLayout() {
        return 0;
    }

    public static class viewHolder {
        public TextView nameTv;
        public TextView latestContentTv;
        public TextView timeTv;
        public CircleImageView avaterIv;

    }
}

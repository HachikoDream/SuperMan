package com.dreamspace.superman.UI.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.superman.R;
import com.dreamspace.superman.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/16 0016.
 */
public class SmOrderAdapter extends BasisAdapter<Order,SmOrderAdapter.viewHolder> {
    public SmOrderAdapter(Context mContext) {
        super(mContext, new ArrayList<Order>(), viewHolder.class);
    }
    public SmOrderAdapter(Context mContext, List<Order> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, Order order) {
        holder.courseNameTv.setText(order.getCoursename());
        holder.priceTv.setText(order.getPrice() + "");
        holder.timeTv.setText(order.getTime());
        holder.statusTv.setText(order.getStatus());
        holder.supermanNameTv.setText(order.getSupermanname());
    }

    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.courseNameTv = (TextView) convertView.findViewById(R.id.course_name_tv);
        holder.supermanNameTv = (TextView) convertView.findViewById(R.id.superman_name_tv);
        holder.statusTv = (TextView) convertView.findViewById(R.id.status_tv);
        holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
        holder.priceTv = (TextView) convertView.findViewById(R.id.price_tv);
        holder.mButton=(Button)convertView.findViewById(R.id.mButton);
    }

    @Override
    public int getItemLayout() {
        return  R.layout.sm_orderlist_item_layout;
    }

    public static class viewHolder{
        public TextView timeTv;
        public TextView courseNameTv;
        public TextView supermanNameTv;
        public TextView priceTv;
        public TextView statusTv;
        public Button mButton;
    }
}

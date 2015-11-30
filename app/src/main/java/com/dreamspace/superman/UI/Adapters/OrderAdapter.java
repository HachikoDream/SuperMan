package com.dreamspace.superman.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.QRCodeShowActivity;
import com.dreamspace.superman.UI.Activity.Main.QRReaderActivity;
import com.dreamspace.superman.UI.Activity.Person.OrderDetailActivity;
import com.dreamspace.superman.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class OrderAdapter extends BasisAdapter<Order, OrderAdapter.viewHolder> {

    public final static int QRREADER_REQUEST_CODE = 156;

    public OrderAdapter(Context mContext) {
        super(mContext, new ArrayList<Order>(), viewHolder.class);
    }

    public OrderAdapter(Context mContext, List<Order> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, final Order order) {
        holder.courseNameTv.setText(order.getLess_name());
        holder.priceTv.setText(CommonUtils.getPriceWithInfo(order.getLess_price()));
        holder.timeTv.setText(order.getTime());
        holder.statusTv.setText(CommonUtils.getStatusByCode(order.getState(),order.isCommented()));
        holder.supermanNameTv.setText(order.getName());
        switch (order.getState()) {
            case Constant.ORDER_RELATED.PRE_COST:
                holder.mButton.setVisibility(View.VISIBLE);
                holder.mButton.setText("支付");
                holder.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b=new Bundle();
                        b.putInt(OrderDetailActivity.ORDER_ID, order.getId());
                        b.putInt(OrderDetailActivity.STATE, Constant.ORDER_RELATED.tranverse(order.getState()));
                        b.putString(OrderDetailActivity.COMMON_PRICE, CommonUtils.getPriceWithInfo(order.getLess_price()));
                        b.putInt(OrderDetailActivity.COME_SOURCE_KEY,OrderDetailActivity.COME_FROM_PAY);
                        readyGo(OrderDetailActivity.class,b);
                    }
                });
                break;
            case Constant.ORDER_RELATED.PRE_MEET:
                holder.mButton.setVisibility(View.VISIBLE);
                holder.mButton.setText("确认见面");
                holder.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b=new Bundle();
                        b.putInt(OrderDetailActivity.ORDER_ID, order.getId());
                        b.putInt(OrderDetailActivity.STATE, Constant.ORDER_RELATED.tranverse(order.getState()));
                        b.putString(OrderDetailActivity.COMMON_PRICE, CommonUtils.getPriceWithInfo(order.getLess_price()));
                        b.putInt(OrderDetailActivity.COME_SOURCE_KEY,OrderDetailActivity.COME_FROM_QR);
                        readyGo(OrderDetailActivity.class,b);
                    }
                });
                break;
            case Constant.ORDER_RELATED.FINISH:
                if (!order.isCommented()) {
                    holder.mButton.setText("评价");
                    holder.mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b=new Bundle();
                            b.putInt(OrderDetailActivity.ORDER_ID, order.getId());
                            b.putInt(OrderDetailActivity.STATE, Constant.ORDER_RELATED.tranverse(order.getState()));
                            b.putString(OrderDetailActivity.COMMON_PRICE, CommonUtils.getPriceWithInfo(order.getLess_price()));
                            b.putInt(OrderDetailActivity.COME_SOURCE_KEY,OrderDetailActivity.COME_FROM_COMMENT);
                            readyGo(OrderDetailActivity.class,b);
                        }
                    });
                } else
                    holder.mButton.setVisibility(View.GONE);
                break;
            case Constant.ORDER_RELATED.CANCEL:
            case Constant.ORDER_RELATED.BACK_COST:
            case Constant.ORDER_RELATED.SUSCRIBE:
                holder.mButton.setVisibility(View.GONE);
                break;

        }

    }


    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getmContext(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        Activity activity = (Activity) getmContext();
        activity.startActivityForResult(intent, QRREADER_REQUEST_CODE);
    }
    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getmContext(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        getmContext().startActivity(intent);
    }
    @Override
    protected void initViewHolder(View convertView, viewHolder holder) {
        holder.courseNameTv = (TextView) convertView.findViewById(R.id.course_name_tv);
        holder.supermanNameTv = (TextView) convertView.findViewById(R.id.superman_name_tv);
        holder.statusTv = (TextView) convertView.findViewById(R.id.status_tv);
        holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
        holder.priceTv = (TextView) convertView.findViewById(R.id.price_tv);
        holder.mButton = (Button) convertView.findViewById(R.id.mButton);
    }

    @Override
    public int getItemLayout() {
        return R.layout.order_list_item;
    }

    public static class viewHolder {
        public TextView timeTv;
        public TextView courseNameTv;
        public TextView supermanNameTv;
        public TextView priceTv;
        public TextView statusTv;
        public Button mButton;
    }
}

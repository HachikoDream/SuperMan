package com.dreamspace.superman.UI.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dreamspace.superman.API.ApiManager;
import com.dreamspace.superman.Common.CommonUtils;
import com.dreamspace.superman.Common.Constant;
import com.dreamspace.superman.Common.NetUtils;
import com.dreamspace.superman.Common.Tools;
import com.dreamspace.superman.R;
import com.dreamspace.superman.UI.Activity.Main.QRCodeShowActivity;
import com.dreamspace.superman.UI.Activity.Main.QRReaderActivity;
import com.dreamspace.superman.UI.Fragment.OnRefreshListener;
import com.dreamspace.superman.model.Order;
import com.dreamspace.superman.model.api.OperatorReq;
import com.dreamspace.superman.model.api.OrderlistRes;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2015/8/16 0016.
 */
public class SmOrderAdapter extends BasisAdapter<Order, SmOrderAdapter.viewHolder> {
    private ProgressDialog pd;

    public SmOrderAdapter(Context mContext) {
        super(mContext, new ArrayList<Order>(), viewHolder.class);
    }

    public SmOrderAdapter(Context mContext, List<Order> mEntities) {
        super(mContext, mEntities, viewHolder.class);
    }

    @Override
    protected void setDataIntoView(viewHolder holder, final Order order) {
        holder.courseNameTv.setText(order.getLess_name());
        holder.priceTv.setText(CommonUtils.getPriceWithInfo(order.getLess_price()));
        holder.timeTv.setText(order.getTime());
        holder.statusTv.setText(CommonUtils.getStatusByCode(order.getState()));
        holder.supermanNameTv.setText(new StringBuilder().append("下单人: ").append(order.getName()));
        switch (order.getState()) {
            case Constant.ORDER_RELATED.SUSCRIBE:
                holder.mButton.setVisibility(View.VISIBLE);
                holder.mButton.setText("同意");
                holder.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agreeOrder(order.getId());
                    }
                });
                break;
            case Constant.ORDER_RELATED.PRE_MEET:
                holder.mButton.setVisibility(View.VISIBLE);
                holder.mButton.setText("确认见面");
                holder.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle b = new Bundle();
                        b.putInt(QRCodeShowActivity.ORD_ID, order.getId());
                        readyGo(QRCodeShowActivity.class, b);

                    }
                });
                break;
            case Constant.ORDER_RELATED.FINISH:
            case Constant.ORDER_RELATED.CANCEL:
            case Constant.ORDER_RELATED.BACK_COST:
            case Constant.ORDER_RELATED.PRE_COST:
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
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getmContext(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        getmContext().startActivity(intent);
    }
    private void showPd(String msg) {
        if (pd == null) {
            if (msg == null) {
                msg = "正在提交您的请求,请稍后";
            }
            pd = ProgressDialog.show(getmContext(), "", msg, true, false);
        } else {
            pd.show();
        }
    }

    private void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
   //达人在预约列表中点击同意按钮时触发的函数
    private void agreeOrder(int ord_id) {
        if (NetUtils.isNetworkConnected(getmContext())) {
            showPd(null);
            OperatorReq req = new OperatorReq();
            req.setOpeator(Constant.ORDER_RELATED.SM_OPERATION.CONFIRM);
            ApiManager.getService(getmContext().getApplicationContext()).operateOrderBySm(ord_id, req, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    dismissPd();
                    showAlertDialog("操作成功,需等待学员付款，您现在可以在待付款中查看该订单的进展", "我知道啦", null, new OnFinish() {
                        @Override
                        public void finish(boolean isOk) {
                            getRefreshDataByState(Constant.ORDER_RELATED.SUSCRIBE, new OnRefreshListener<Order>() {
                                @Override
                                public void onFinish(List<Order> mEntities) {
                                    refreshData(mEntities);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showAlertDialog(Tools.getInnerErrorInfo(error), "确定", null, null);
                }
            });
        } else {
            showNetWorkErrorDialog();
        }
    }

    private void showNetWorkErrorDialog() {
        showAlertDialog("无法连接网络，请稍后再试", "确定", null, null);
    }

    private void getRefreshDataByState(int state, final OnRefreshListener<Order> finishListener) {
        if (NetUtils.isNetworkConnected(getmContext())) {
            showPd("请稍后,正在刷新数据");
            ApiManager.getService(getmContext().getApplicationContext()).getSmOrderListByState(state, 1, new Callback<OrderlistRes>() {
                @Override
                public void success(OrderlistRes orderlistRes, Response response) {
                    dismissPd();
                    if (orderlistRes != null) {
                        finishListener.onFinish(orderlistRes.getOrders());
                    } else {
                        finishListener.onError();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissPd();
                    showAlertDialog(Tools.getInnerErrorInfo(error), "确定", null, new OnFinish() {
                        @Override
                        public void finish(boolean isOk) {
                            finishListener.onError();
                        }
                    });
                }
            });
        } else {
            finishListener.onError();
            showNetWorkErrorDialog();
        }
    }

    private void refreshData(List<Order> orders) {
        setmEntities(orders);
        notifyDataSetChanged();
    }

    private void showAlertDialog(String msg, String positiveMsg, String negativeMsg, final OnFinish finish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getmContext())
                .setTitle("提示")
                .setMessage(msg)
                .setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish != null) {
                            finish.finish(true);
                        }
                    }
                });
        if (!CommonUtils.isEmpty(negativeMsg)) {
            builder.setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (finish != null) {
                        finish.finish(false);
                    }
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
        return R.layout.sm_orderlist_item_layout;
    }

    public static class viewHolder {
        public TextView timeTv;
        public TextView courseNameTv;
        public TextView supermanNameTv;
        public TextView priceTv;
        public TextView statusTv;
        public Button mButton;
    }

    interface OnFinish {
        void finish(boolean isOk);

    }
}

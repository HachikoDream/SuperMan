package com.dreamspace.superman.UI.Fragment.Orders.Superman;

import com.dreamspace.superman.UI.Adapters.OrderAdapter;
import com.dreamspace.superman.UI.Adapters.SmOrderAdapter;
import com.dreamspace.superman.UI.Fragment.Base.BaseListFragment;
import com.dreamspace.superman.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class SmNopaymentFragment extends BaseListFragment<Order> {
    private static final String TAG="待付款";
    public SmNopaymentFragment() {
        super(SmOrderAdapter.class);
        setTAG(TAG);
    }

    @Override
    public void onPullUp() {

    }

    @Override
    public void onPullDown() {

    }

    @Override
    public void getInitData() {
        refreshDate(getTestDate());
    }
    private List<Order> getTestDate(){
        List<Order> mOrders=new ArrayList<>();
        Order order;
        for (int i=0;i<10;i++){
            order=new Order();
            order.setCoursename("钢琴" + i + "级教程");
            order.setPrice(i+0.5f);
            order.setStatus("待XX");
            order.setSupermanname("XXXXXX");
            order.setTime("10-11 12:33");
            mOrders.add(order);
        }
        return mOrders;
    }
}

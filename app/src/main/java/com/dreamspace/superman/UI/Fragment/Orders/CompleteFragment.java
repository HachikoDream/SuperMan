package com.dreamspace.superman.UI.Fragment.Orders;

import com.dreamspace.superman.UI.Fragment.Base.BaseIndexListFragment;
import com.dreamspace.superman.UI.Fragment.Base.BaseOrderListFragment;
import com.dreamspace.superman.model.Course;
import com.dreamspace.superman.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class CompleteFragment extends BaseOrderListFragment {
    private final static String TAG="已完成";

    public CompleteFragment() {
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

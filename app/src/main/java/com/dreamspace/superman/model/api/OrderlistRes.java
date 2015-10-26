package com.dreamspace.superman.model.api;

import com.dreamspace.superman.model.Order;

import java.util.List;

/**
 * Created by Wells on 2015/10/24.
 */
public class OrderlistRes {
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/11/12.
 */
public class payAccountRes {
    private String payaccount;
    private int arrived_balance;
    private int not_arrived_balance;

    public int getArrived_balance() {
        return arrived_balance;
    }

    public void setArrived_balance(int arrived_balance) {
        this.arrived_balance = arrived_balance;
    }

    public int getNot_arrived_balance() {
        return not_arrived_balance;
    }

    public void setNot_arrived_balance(int not_arrived_balance) {
        this.not_arrived_balance = not_arrived_balance;
    }

    public String getPayaccount() {
        return payaccount;
    }

    public void setPayaccount(String payaccount) {
        this.payaccount = payaccount;
    }
}

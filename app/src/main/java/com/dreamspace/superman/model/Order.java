package com.dreamspace.superman.model;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class Order {
    private int id;
    private String time;
    private int state;
    private int less_price;
    private String less_name;
    private String name;
    private boolean commented;

    public boolean isCommented() {
        return commented;
    }

    public void setCommented(boolean commented) {
        this.commented = commented;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLess_price() {
        return less_price;
    }

    public void setLess_price(int less_price) {
        this.less_price = less_price;
    }

    public String getLess_name() {
        return less_name;
    }

    public void setLess_name(String less_name) {
        this.less_name = less_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

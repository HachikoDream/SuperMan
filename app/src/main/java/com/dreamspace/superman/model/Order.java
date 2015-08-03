package com.dreamspace.superman.model;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class Order {
    private String time;
    private String coursename;
    private String status;
    private int type;
    private String supermanname;
    private float price;

    public void setTime(String time) {
        this.time = time;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSupermanname(String supermanname) {
        this.supermanname = supermanname;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {

        return time;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public String getSupermanname() {
        return supermanname;
    }

    public float getPrice() {
        return price;
    }
}

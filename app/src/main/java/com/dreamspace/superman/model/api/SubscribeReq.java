package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class SubscribeReq {
    private String less_id;
    private String name;
    private String phone;
    private String time;
    private String site;
    private String remark;

    public String getLess_id() {
        return less_id;
    }

    public void setLess_id(String less_id) {
        this.less_id = less_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

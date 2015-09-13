package com.dreamspace.superman.model.api;

/**
 * Created by Administrator on 2015/8/24 0024.
 */
public class RegistertokenReq {
    private String phone;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

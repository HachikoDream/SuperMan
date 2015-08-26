package com.dreamspace.superman.model.api;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public class LoginReq {
    private String phone;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class ModifyReq {
    private String register_token;
    private String password;

    public String getRegister_token() {
        return register_token;
    }

    public void setRegister_token(String register_token) {
        this.register_token = register_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

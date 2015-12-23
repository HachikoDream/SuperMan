package com.dreamspace.superman.model.api;

/**
 * Created by Administrator on 2015/8/24 0024.
 */
public class RegistertokenRes {
    private String register_token;
    private boolean registered;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getRegister_token() {
        return register_token;
    }

    public void setRegister_token(String register_token) {
        this.register_token = register_token;
    }
}

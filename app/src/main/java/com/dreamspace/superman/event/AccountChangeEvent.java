package com.dreamspace.superman.event;

/**
 * Created by Wells on 2015/11/17.
 * 代表账号登录或者注销事件
 */
public class AccountChangeEvent {
    public String type=LOGIN;
    public static final String LOGIN="login";
    public static final String MAST_STATE_CHANGE="mast";
}

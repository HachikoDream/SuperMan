package com.dreamspace.superman.event;

/**
 * Created by Wells on 2016/2/15.
 */
public class RefreshEvent {

    public static final int INDEX = 0;
    public static final int SUPERMAN = 1;
    public int type;
    public static RefreshEvent newInstance() {
        return new RefreshEvent();
    }
}

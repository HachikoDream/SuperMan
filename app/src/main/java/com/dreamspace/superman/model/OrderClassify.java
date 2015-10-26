package com.dreamspace.superman.model;

/**
 * Created by Wells on 2015/10/24.
 */
public class OrderClassify {
    private int state;
    private String name;

    public OrderClassify(int state, String name) {
        this.state = state;
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

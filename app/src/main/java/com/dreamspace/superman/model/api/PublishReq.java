package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class PublishReq {
    private String name;
    private String keeptime;
    private int price;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeeptime() {
        return keeptime;
    }

    public void setKeeptime(String keeptime) {
        this.keeptime = keeptime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}

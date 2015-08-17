package com.dreamspace.superman.model;

/**
 * Created by Administrator on 2015/8/17 0017.
 */
public class ConList {
    private String time;
    private String name;
    private String avaterUrl;
    private String latestContent;

    public String getAvaterUrl() {
        return avaterUrl;
    }

    public String getLatestContent() {
        return latestContent;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }

    public void setLatestContent(String latestContent) {
        this.latestContent = latestContent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

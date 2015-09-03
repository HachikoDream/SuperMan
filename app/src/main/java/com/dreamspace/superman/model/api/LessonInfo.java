package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class LessonInfo {
    private String less_id;
    private String name;
    private String image;
    private String[] tags;
    private String less_name;
    private int collection_count;
    private int success_count;
    private int price;
    private int state;
    private String keeptime;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getLess_name() {
        return less_name;
    }

    public void setLess_name(String less_name) {
        this.less_name = less_name;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(int collection_count) {
        this.collection_count = collection_count;
    }

    public int getSuccess_count() {
        return success_count;
    }

    public void setSuccess_count(int success_count) {
        this.success_count = success_count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getKeeptime() {
        return keeptime;
    }

    public void setKeeptime(String keeptime) {
        this.keeptime = keeptime;
    }
}

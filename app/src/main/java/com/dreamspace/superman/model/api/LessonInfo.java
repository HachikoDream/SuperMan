package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class LessonInfo {
    private String id;
    private String name;
    private String image;
    private String less_name;
    private int collection_count;
    private int success_count;
    private int price;
    private String state;
    private String keeptime;
    private String mast_id;
    private String description;


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


    public String getKeeptime() {
        return keeptime;
    }

    public void setKeeptime(String keeptime) {
        this.keeptime = keeptime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMast_id() {
        return mast_id;
    }

    public void setMast_id(String mast_id) {
        this.mast_id = mast_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

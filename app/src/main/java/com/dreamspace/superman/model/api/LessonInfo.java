package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 * {
 * "mast_id": 2,
 * "name": "duzhixia",
 * "keeptime": "2",
 * "tags": "tag1,tag2,tag3,tag4,tag5",
 * "state": "on",
 * "image": "http://7xl53f.com1.z0.glb.clouddn.com/5607bc8090c49013abc2234b?imageView2/1/w/138/h/138/q/100",
 * "description": "jieshao",
 * "id": 2,
 * "less_name": "mycourse1",
 * "collection_count": 0,
 * "success_count": 0,
 * "price": 666
 * }
 */
public class LessonInfo {
    private int id;
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
    private String tags;
    private boolean is_collected;

    public boolean is_collected() {
        return is_collected;
    }

    public void setIs_collected(boolean is_collected) {
        this.is_collected = is_collected;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
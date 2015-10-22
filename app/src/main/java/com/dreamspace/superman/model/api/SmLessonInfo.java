package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/10/22.
 * {"lessons":
 * [
 * {
 * "state": "on",
 * "keeptime": "12",
 * "less_id": 7,
 * "collection_count": 0,
 * "image": "http://7xl53f.com1.z0.glb.clouddn.com/560aad1090c4902ac0c277f6?imageView2/1/w/138/h/138/q/100",
 * "less_name": "wddtest",
 * "tags": "sdsd",
 * "success_count": 0,
 * "price": 1234,
 * "name": "wangdd"
 * },
 * {"state": "on", "keeptime": "12", "less_id": 8, "collection_count": 0, "image": "http://7xl53f.com1.z0.glb.clouddn.com/560aad1090c4902ac0c277f6?imageView2/1/w/138/h/138/q/100", "less_name": "wddtest2", "tags": "sdsd", "success_count": 0, "price": 1232, "name": "wangdd"}]}
 */
public class SmLessonInfo {
    private String state;
    private String keeptime;
    private int less_id;
    private int collection_count;
    private String image;
    private String less_name;
    private String tags;
    private int success_count;
    private int price;
    private String name;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKeeptime() {
        return keeptime;
    }

    public void setKeeptime(String keeptime) {
        this.keeptime = keeptime;
    }

    public int getLess_id() {
        return less_id;
    }

    public void setLess_id(int less_id) {
        this.less_id = less_id;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(int collection_count) {
        this.collection_count = collection_count;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

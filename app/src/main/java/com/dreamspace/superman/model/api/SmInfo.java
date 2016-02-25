package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class SmInfo {
    private String id;
    private String image;
    private String sex;
    private String name;
    //所获荣誉
    private String glory;
    //个人评价
    private String resume;
    //个人标签
    private String tags;
    private String[] certificates;
    //达人收藏数
    private int collection_count;
    //日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public void setCollection_count(int collection_count) {
        this.collection_count = collection_count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    private String date;
    //达人热度
    private int heat;

    public String[] getCertificates() {
        return certificates;
    }

    public void setCertificates(String[] certificates) {
        this.certificates = certificates;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGlory() {
        return glory;
    }

    public void setGlory(String glory) {
        this.glory = glory;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}

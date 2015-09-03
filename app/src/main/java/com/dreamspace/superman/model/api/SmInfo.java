package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class SmInfo {
    private String image;
    private String sex;
    private String name;
    //所获荣誉
    private String glory;
    //个人评价
    private String resume;
    //????
    private String[] tags;

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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}

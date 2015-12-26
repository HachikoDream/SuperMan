package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 * {"resume": "jianjie",
 * "id": 2,
 * "image": "http://7xl53f.com1.z0.glb.clouddn.com/5607bc8090c49013abc2234b?imageView2/1/w/138/h/138/q/100",
 * "user_id": 7,
 * "tags": "tag1,tag2,tag3,tag4,tag5",
 * "glory": "rongyu",
 * "name": "duzhixia"}
 */
public class SmInfo {
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

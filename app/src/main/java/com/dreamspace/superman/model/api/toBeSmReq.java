package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/29.
 */
public class ToBeSmReq {
    private String image;
    private String sex;
    private String name;
    private String phone;
    private String want_cata;
    private String tags;
    private String glory;
    private String resume;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWant_cata() {
        return want_cata;
    }

    public void setWant_cata(String want_cata) {
        this.want_cata = want_cata;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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
}

package com.dreamspace.superman.model;

/**
 * Created by Wells on 2015/9/4.
 */
public class UserInfo {
    private String id;
    private String sex;
    private String name;
    private String nickname;
    private String image;
    private String phone;
    private String mas_id;
    private String mast_state;

    public String getMast_state() {
        return mast_state;
    }

    public void setMast_state(String mast_state) {
        this.mast_state = mast_state;
    }

    public String getMas_id() {
        return mas_id;
    }

    public void setMas_id(String mas_id) {
        this.mas_id = mas_id;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", image='" + image + '\'' +
                ", phone='" + phone + '\'' +
                ", mas_id='" + mas_id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

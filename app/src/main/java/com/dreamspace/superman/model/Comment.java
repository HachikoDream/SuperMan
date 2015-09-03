package com.dreamspace.superman.model;

/**
 * Created by Administrator on 2015/8/1 0001.
 */
public class Comment {
    private String time;
    private String Image;
    private String content;
    private String less_name;
    private String nickname;

    public void setTime(String time) {
        this.time = time;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLess_name(String less_name) {
        this.less_name = less_name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTime() {

        return time;
    }

    public String getImage() {
        return Image;
    }

    public String getContent() {
        return content;
    }

    public String getLess_name() {
        return less_name;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", Image='" + Image + '\'' +
                ", less_name='" + less_name + '\'' +
                ", userName='" + nickname + '\'' +
                '}';
    }
}

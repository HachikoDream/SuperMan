package com.dreamspace.superman.model;

/**
 * Created by Administrator on 2015/8/1 0001.
 * {
 * "comments": [
 * {
 * "nickname": "f*****",
 * "content": "东西不错",
 * "id": 3,
 * "time": "2015-09-27 14:33:26",
 * "image": "asd"
 * }
 * ]
 * }
 */
public class Comment {
    private String time;
    private String Image;
    private String content;
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

    public String getNickname() {
        return nickname;
    }
}

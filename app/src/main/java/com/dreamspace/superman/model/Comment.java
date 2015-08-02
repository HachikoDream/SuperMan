package com.dreamspace.superman.model;

/**
 * Created by Administrator on 2015/8/1 0001.
 */
public class Comment {
    private String time;
    private String ImageUrL;
    private String comment;
    private String courseName;
    private String userName;

    public void setTime(String time) {
        this.time = time;
    }

    public void setImageUrL(String imageUrL) {
        ImageUrL = imageUrL;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {

        return time;
    }

    public String getImageUrL() {
        return ImageUrL;
    }

    public String getComment() {
        return comment;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getUserName() {
        return userName;
    }
}

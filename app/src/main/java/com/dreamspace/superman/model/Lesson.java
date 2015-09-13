package com.dreamspace.superman.model;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class Lesson {
    @Override
    public String toString() {
        return "Lesson{" +
                "courseName='" + courseName + '\'' +
                '}';
    }

    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}

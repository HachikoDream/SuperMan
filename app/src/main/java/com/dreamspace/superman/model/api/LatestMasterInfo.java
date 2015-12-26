package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/12/26.
 */
public class LatestMasterInfo {
    private String tags;
    private String glory;
    private String resume;
    private String[] certificates;
    private String state;
    public static String PENDING = "pending";
    public static String SUCCESS = "success";

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

    public String[] getCertificates() {
        return certificates;
    }

    public void setCertificates(String[] certificates) {
        this.certificates = certificates;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

package com.dreamspace.superman.event;

/**
 * Created by Wells on 2015/12/3.
 */
public class AvaterChangeEvent {
    private String photoPath;
    public AvaterChangeEvent(String photoPath) {
        this.photoPath=photoPath;
    }
    public String getPhotoPath(){
        return photoPath;
    }


}

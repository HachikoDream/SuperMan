package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class CommentReq {
  private String content;
  private boolean anonymous;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}

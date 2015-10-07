package com.dreamspace.superman.model.api;

import com.dreamspace.superman.model.Comment;

import java.util.List;

/**
 * Created by Wells on 2015/10/5.
 */
public class CommentList {
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

package dev.linhnv.fbee.model;

import android.graphics.Bitmap;

/**
 * Created by DevLinhnv on 1/11/2017.
 */

public class Comment_forum {
    private String commentId;
    private String title;
    private String description;
    private String dateCreated;
    private String parent;
    private String userId;
    private String topicId;
    public Comment_forum(){

    }

    public Comment_forum(String commentId, String title, String description, String dateCreated, String parent, String userId, String topicId) {
        this.commentId = commentId;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.parent = parent;
        this.userId = userId;
        this.topicId = topicId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}

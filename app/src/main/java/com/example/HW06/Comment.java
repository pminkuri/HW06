package com.example.HW06;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {

    String commentText,commentedBy,forumID,commentID;
    Date commentedOn;

    public String getForumID() {
        return forumID;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public void setForumID(String forumID) {
        this.forumID = forumID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    public Comment(String commentText, String commentedBy, String forumID, Date commentedOn) {
        this.commentText = commentText;
        this.commentedBy = commentedBy;
        this.forumID = forumID;
        this.commentedOn = commentedOn;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        this.commentID = format.format(commentedOn);
    }

    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentText='" + commentText + '\'' +
                ", commentedBy='" + commentedBy + '\'' +
                ", forumID='" + forumID + '\'' +
                ", commentID='" + commentID + '\'' +
                ", commentedOn=" + commentedOn +
                '}';
    }
}

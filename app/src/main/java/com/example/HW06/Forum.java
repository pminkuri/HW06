package com.example.HW06;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;


public class Forum implements Serializable {

    String title, creatorName, description;
    private HashMap<String,Object> likedBy=new HashMap<>();
    String forumID;
    Date createdOn;

    ArrayList<Comment> comments = new ArrayList<>();

    public String getForumID() {
        return forumID;
    }

    public void setForumID(String forumID) {
        this.forumID = forumID;
    }

    public HashMap<String, Object> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(HashMap<String, Object> likedBy) {
        this.likedBy = likedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

//    public Forum(String title, String creatorName, String description, HashMap<String, Object> likedBy, Date createdOn) {
//        this.title = title;
//        this.creatorName = creatorName;
//        this.description = description;
//        this.likedBy = likedBy;
//        this.createdOn = createdOn;
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        this.forumID = formatter.format(createdOn);
//
//    }

    public Forum(String title, String creatorName, String description, HashMap<String, Object> likedBy,Date createdOn, ArrayList<Comment> comments) {
        this.title = title;
        this.creatorName = creatorName;
        this.description = description;
        this.likedBy = likedBy;
        this.forumID = forumID;
        this.createdOn = createdOn;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        this.forumID = formatter.format(createdOn);
        this.comments = comments;
    }

    public Forum() {
    }

    @Override
    public String toString() {
        return "Forum{" +
                "title='" + title + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", description='" + description + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }




}




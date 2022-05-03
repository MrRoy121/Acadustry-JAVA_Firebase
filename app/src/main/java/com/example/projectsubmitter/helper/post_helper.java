package com.example.projectsubmitter.helper;


import java.util.List;

public class post_helper {

    String username, userID, title, details;
    List<String> Tags;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tag) {
        Tags = tag;
    }

    public post_helper(String username, String userID, String title, String details, List<String> tag) {
        this.username = username;
        this.userID = userID;
        this.title = title;
        this.details = details;
        Tags = tag;
    }

    public post_helper() {
    }
}
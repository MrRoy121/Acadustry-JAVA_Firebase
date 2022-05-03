package com.example.projectsubmitter.submission;

public class my_submission_list {
    public my_submission_list(String code, String title, String date, String time, String id) {
        this.code = code;
        this.title = title;
        this.date = date;
        this.time = time;
        this.id = id;
    }

    String code, title, date, time, id;

    public my_submission_list() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.example.projectsubmitter.helper;

public class teach_job_list {
    String title, userName;

    public teach_job_list(String title, String userName) {
        this.title = title;
        this.userName = userName;
    }

    public teach_job_list() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getuserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }
}

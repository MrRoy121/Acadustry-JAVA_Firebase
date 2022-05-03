package com.example.projectsubmitter.helper;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.StringTokenizer;

public class submit_list {
    String title;
    String details;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRan() {
        return ran;
    }

    public void setRan(String ran) {
        this.ran = ran;
    }

    String ran;

    public submit_list() {
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

        StringTokenizer st = new StringTokenizer(details, ".");
        String community = st.nextToken() + " .....";
        this.details = community;
    }

    public submit_list(String title, String details, String ran, String date) {
        this.title = title;
        this.details = details;
        this.ran = ran;
        this.date = date;
    }
}

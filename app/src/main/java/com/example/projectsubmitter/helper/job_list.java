package com.example.projectsubmitter.helper;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.StringTokenizer;

public class job_list {
    String title;
    String details;
    String ran;
    boolean is;

    public boolean isIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public String getRan() {
        return ran;
    }

    public void setRan(String ran) {
        this.ran = ran;
    }

    public job_list() {
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

    public job_list(String title, String details, String ran, boolean is) {
        this.title = title;
        this.details = details;
        this.ran = ran;
        this.is = is;
    }
}

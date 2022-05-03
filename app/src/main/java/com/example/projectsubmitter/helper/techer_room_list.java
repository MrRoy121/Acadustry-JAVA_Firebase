package com.example.projectsubmitter.helper;

public class techer_room_list {

    String title;
    String section;
    String teacherName;
    String subject;

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    String random;

    public techer_room_list() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getteacherName() {
        return teacherName;
    }

    public void setteacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public techer_room_list(String title, String section, String teacherName, String subject, String random) {
        this.title = title;
        this.section = section;
        this.teacherName = teacherName;
        this.subject = subject;
        this.random = random;
    }
}

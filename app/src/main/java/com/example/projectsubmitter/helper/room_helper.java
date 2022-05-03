package com.example.projectsubmitter.helper;

public class room_helper {
    String rand, title, tname, temail, room, dept, section;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject() {
        return dept;
    }

    public void setSubject(String Dept) {
        this.dept = Dept;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public room_helper() {
    }

    public String getRandom() {
        return rand;
    }

    public void setRandom(String rand) {
        this.rand = rand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherName() {
        return tname;
    }

    public void setTeacherName(String tname) {
        this.tname = tname;
    }

    public String getTeacherEmail() {
        return temail;
    }

    public void setTeacherEmail(String temail) {
        this.temail = temail;
    }

    public room_helper(String rand, String title, String room, String dept, String section, String tname, String temail) {
        this.rand = rand;
        this.title = title;
        this.room = room;
        this.dept = dept;
        this.section = section;
        this.tname = tname;
        this.temail = temail;
    }
}

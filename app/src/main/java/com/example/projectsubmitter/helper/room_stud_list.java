package com.example.projectsubmitter.helper;

public class room_stud_list {
    String name, id;

    public room_stud_list() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public room_stud_list(String name, String id) {
        this.name = name;
        this.id = id;
    }
}

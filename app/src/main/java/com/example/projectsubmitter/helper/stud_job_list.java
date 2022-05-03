package com.example.projectsubmitter.helper;

public class stud_job_list {
    String code, name, id;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public stud_job_list() {
    }

    public stud_job_list(String code, String name, String id) {
        this.code = code;
        this.name = name;
        this.id = id;
    }
}

package com.example.sreya_2207033_cvbuilder.model;

public class Education {
    private int id;
    private int userId;
    private String details;

    public Education() {}
    public Education(int userId, String details) {
        this.userId = userId;
        this.details = details;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}

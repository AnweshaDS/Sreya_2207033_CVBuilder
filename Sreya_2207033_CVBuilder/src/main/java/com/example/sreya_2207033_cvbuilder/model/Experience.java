package com.example.sreya_2207033_cvbuilder.model;

public class Experience {
    private int id;
    private int userId;
    private String description;

    public Experience() {}

    public Experience(int userId, String description) {
        this.userId = userId;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "id=" + id +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                '}';
    }
}

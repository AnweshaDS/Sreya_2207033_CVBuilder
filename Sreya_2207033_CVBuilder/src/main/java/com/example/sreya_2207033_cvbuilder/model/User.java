package com.example.sreya_2207033_cvbuilder.model;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String address; // optional/notes

    public User() {}

    public User(String fullName, String email, String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSummary() { return address; }
    public void setSummary(String summary) { this.address = summary; }

    @Override
    public String toString() {
        return id + ": " + fullName;
    }
}

package com.example.sreya_2207033_cvbuilder;

import javafx.scene.image.Image;
import java.util.List;

public class CVData {
    private String fullName;
    private String email;
    private String phone;
    private String address;

    private String skills;
    private String experience;
    private String projects;

    private List<String> educationList;
    private Image photo;

    public CVData() {}

    public CVData(String fullName, String email, String phone, String address,
                  String skills, String experience, String projects,
                  List<String> educationList, Image photo) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.skills = skills;
        this.experience = experience;
        this.projects = projects;
        this.educationList = educationList;
        this.photo = photo;
    }

    // ---------- Getters & Setters ----------

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getProjects() { return projects; }
    public void setProjects(String projects) { this.projects = projects; }

    public List<String> getEducationList() { return educationList; }
    public void setEducationList(List<String> educationList) { this.educationList = educationList; }

    public Image getPhoto() { return photo; }
    public void setPhoto(Image photo) { this.photo = photo; }
}

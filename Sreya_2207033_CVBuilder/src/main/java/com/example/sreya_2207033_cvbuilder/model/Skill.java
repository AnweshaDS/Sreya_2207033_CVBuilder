package com.example.sreya_2207033_cvbuilder.model;

public class Skill {
    private int id;
    private int userId;
    private String skillName;

    public Skill() {}
    public Skill(int userId, String skillName) {
        this.userId = userId;
        this.skillName = skillName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
}

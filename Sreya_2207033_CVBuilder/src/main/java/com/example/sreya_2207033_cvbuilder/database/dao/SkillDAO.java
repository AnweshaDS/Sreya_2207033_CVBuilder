package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkillDAO {

    public void insertSkill(int userId, String skillName) throws SQLException {
        String sql = "INSERT INTO skills(user_id, skill_name) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, skillName);
            ps.executeUpdate();
        }
    }

    public List<Skill> getByUserId(int userId) throws SQLException {
        List<Skill> list = new ArrayList<>();
        String sql = "SELECT id, user_id, skill_name FROM skills WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Skill s = new Skill();
                    s.setId(rs.getInt("id"));
                    s.setUserId(rs.getInt("user_id"));
                    s.setSkillName(rs.getString("skill_name"));
                    list.add(s);
                }
            }
        }
        return list;
    }

    public void deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM skills WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}

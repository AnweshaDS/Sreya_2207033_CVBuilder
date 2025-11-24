package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.Education;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EducationDAO {

    public void insertEducation(int userId, String details) throws SQLException {
        String sql = "INSERT INTO education(user_id, details) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, details);
            ps.executeUpdate();
        }
    }

    public List<Education> getByUserId(int userId) throws SQLException {
        List<Education> list = new ArrayList<>();
        String sql = "SELECT id, user_id, details FROM education WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Education e = new Education();
                    e.setId(rs.getInt("id"));
                    e.setUserId(rs.getInt("user_id"));
                    e.setDetails(rs.getString("details"));
                    list.add(e);
                }
            }
        }
        return list;
    }

    public void deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM education WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}

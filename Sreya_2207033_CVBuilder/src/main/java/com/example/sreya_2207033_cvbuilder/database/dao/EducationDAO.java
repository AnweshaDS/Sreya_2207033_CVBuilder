package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.Education;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EducationDAO {

    public void insert(int userId, String details) throws SQLException {
        String sql = "INSERT INTO education(user_id, details) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, details);
            ps.executeUpdate();
        }
    }

    public List<Education> getByUser(int userId) throws SQLException {
        List<Education> out = new ArrayList<>();
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
                    out.add(e);
                }
            }
        }
        return out;
    }

    public void deleteByUser(int userId) throws SQLException {
        String sql = "DELETE FROM education WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}

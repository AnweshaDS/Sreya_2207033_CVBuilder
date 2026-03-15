package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.Experience;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExperienceDAO {

    public void insert(int userId, String desc) throws SQLException {
        String sql = "INSERT INTO experience(user_id, description) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, desc);
            ps.executeUpdate();
        }
    }

    public List<Experience> getByUser(int userId) throws SQLException {
        List<Experience> out = new ArrayList<>();
        String sql = "SELECT id, user_id, description FROM experience WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Experience ex = new Experience();
                    ex.setId(rs.getInt("id"));
                    ex.setUserId(rs.getInt("user_id"));
                    ex.setDescription(rs.getString("description"));
                    out.add(ex);
                }
            }
        }
        return out;
    }

    public void deleteByUser(int userId) throws SQLException {
        String sql = "DELETE FROM experience WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}

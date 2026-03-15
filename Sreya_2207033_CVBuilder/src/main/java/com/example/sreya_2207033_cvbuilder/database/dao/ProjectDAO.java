package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public void insert(int userId, String desc) throws SQLException {
        String sql = "INSERT INTO projects(user_id, description) VALUES(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, desc);
            ps.executeUpdate();
        }
    }

    public List<Project> getByUser(int userId) throws SQLException {
        List<Project> out = new ArrayList<>();
        String sql = "SELECT id, user_id, description FROM projects WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Project p = new Project();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setDescription(rs.getString("description"));
                    out.add(p);
                }
            }
        }
        return out;
    }

    public void deleteByUser(int userId) throws SQLException {
        String sql = "DELETE FROM projects WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}

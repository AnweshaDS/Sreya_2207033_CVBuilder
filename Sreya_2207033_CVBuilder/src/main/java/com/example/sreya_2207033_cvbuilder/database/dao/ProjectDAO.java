package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    // INSERT using userId + description (matches controller)
    public void insert(int userId, String description) throws SQLException {
        String sql = "INSERT INTO project (user_id, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, description);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // FETCH all projects for a user
    public List<Project> getByUserId(int userId) {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM project WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setUserId(rs.getInt("user_id"));
                project.setDescription(rs.getString("description"));
                list.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // DELETE all projects for a user
    public void deleteByUserId(int userId) {
        String sql = "DELETE FROM project WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

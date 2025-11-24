package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {


    public void insert(Project project) {
        String sql = "INSERT INTO project (userId, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, project.getUserId());
            stmt.setString(2, project.getDescription());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                project.setId(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Project> getByUserId(int userId) {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM project WHERE userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Project pr = new Project();
                pr.setId(rs.getInt("id"));
                pr.setUserId(rs.getInt("userId"));
                pr.setDescription(rs.getString("description"));
                list.add(pr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public void deleteByUserId(int userId) {
        String sql = "DELETE FROM project WHERE userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

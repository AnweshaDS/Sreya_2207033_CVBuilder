package com.example.sreya_2207033_cvbuilder.database.dao;

import com.example.sreya_2207033_cvbuilder.database.DatabaseConnection;
import com.example.sreya_2207033_cvbuilder.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public int insert(User u, String photoPath) throws SQLException {
        String sql = "INSERT INTO users(full_name, email, phone, address, photo_path) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getAddress());
            ps.setString(5, photoPath);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    u.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public List<User> getAll() throws SQLException {
        List<User> out = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setAddress(rs.getString("address"));
                u.setPhotoPath(rs.getString("photo_path"));
                out.add(u);
            }
        }
        return out;
    }

    public User getById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setAddress(rs.getString("address"));
                    u.setPhotoPath(rs.getString("photo_path"));
                    return u;
                }
            }
        }
        return null;
    }

    public void update(User u, String photoPath) throws SQLException {
        String sql = "UPDATE users SET full_name=?, email=?, phone=?, address=?, photo_path=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getAddress());
            ps.setString(5, photoPath);
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // children use foreign key cascade; if not enforceable, delete manually.
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement("DELETE FROM education WHERE user_id=?");
                 PreparedStatement p2 = conn.prepareStatement("DELETE FROM skills WHERE user_id=?");
                 PreparedStatement p3 = conn.prepareStatement("DELETE FROM experience WHERE user_id=?");
                 PreparedStatement p4 = conn.prepareStatement("DELETE FROM projects WHERE user_id=?");
                 PreparedStatement p5 = conn.prepareStatement("DELETE FROM users WHERE id=?")) {

                p1.setInt(1, id); p1.executeUpdate();
                p2.setInt(1, id); p2.executeUpdate();
                p3.setInt(1, id); p3.executeUpdate();
                p4.setInt(1, id); p4.executeUpdate();
                p5.setInt(1, id); p5.executeUpdate();

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}

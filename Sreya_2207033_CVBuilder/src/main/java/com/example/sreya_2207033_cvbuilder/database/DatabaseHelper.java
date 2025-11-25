package com.example.sreya_2207033_cvbuilder.database;

import com.example.sreya_2207033_cvbuilder.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // -----------------------------
    // PHOTO COPY FUNCTION (FIXED)
    // -----------------------------
    public static String copyPhotoToApp(File src) throws IOException {
        File folder = new File("photos");
        if (!folder.exists()) folder.mkdirs();

        File dest = new File(folder, System.currentTimeMillis() + "_" + src.getName());
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return dest.getAbsolutePath();
    }

    // -----------------------------
    // INSERT USER WITH PHOTO PATH
    // -----------------------------
    public static int insertUser(User user, String photoPath) {
        String sql = """
            INSERT INTO users (full_name, email, phone, address, photo_path)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setString(5, photoPath);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // -----------------------------
    // GET ALL USERS
    // -----------------------------
    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

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
                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // -----------------------------
    // UPDATE USER
    // -----------------------------
    public static boolean updateUser(User user, String photoPath) {
        String sql = """
            UPDATE users SET 
            full_name = ?, email = ?, phone = ?, address = ?, photo_path = ?
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getAddress());
            ps.setString(5, photoPath);
            ps.setInt(6, user.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    public static boolean deleteUser(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

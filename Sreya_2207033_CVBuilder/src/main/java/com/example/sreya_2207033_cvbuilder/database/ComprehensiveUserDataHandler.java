package com.example.sreya_2207033_cvbuilder.database;

import com.example.sreya_2207033_cvbuilder.model.Education;
import com.example.sreya_2207033_cvbuilder.model.Experience;
import com.example.sreya_2207033_cvbuilder.model.Project;
import com.example.sreya_2207033_cvbuilder.model.Skill;
import com.example.sreya_2207033_cvbuilder.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles comprehensive user data operations including personal info, education,
 * experience, skills, projects, and photo in single transactions.
 */
public class ComprehensiveUserDataHandler {

    /**
     * Inserts a complete user profile with all related information in a single transaction.
     *
     * @param user       the user's personal information (including photo_path)
     * @param education  list of education details
     * @param experience list of experience descriptions
     * @param skills     list of skill names
     * @param projects   list of project descriptions
     * @return the generated user ID, or -1 on failure
     */
    public static int insertCompleteUser(User user,
                                         List<String> education,
                                         List<String> experience,
                                         List<String> skills,
                                         List<String> projects) {
        String insertUser = "INSERT INTO users(full_name, email, phone, address, summary, photo_path) VALUES(?,?,?,?,?,?)";
        String insertEdu = "INSERT INTO education(user_id, details) VALUES(?,?)";
        String insertExp = "INSERT INTO experience(user_id, description) VALUES(?,?)";
        String insertSkill = "INSERT INTO skills(user_id, skill_name) VALUES(?,?)";
        String insertProj = "INSERT INTO projects(user_id, description) VALUES(?,?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int userId;
                try (PreparedStatement ps = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, user.getFullName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPhone());
                    ps.setString(4, user.getAddress());
                    ps.setString(5, user.getSummary());
                    ps.setString(6, user.getPhotoPath());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            userId = keys.getInt(1);
                            user.setId(userId);
                        } else {
                            conn.rollback();
                            return -1;
                        }
                    }
                }

                insertRelated(conn, insertEdu, userId, education);
                insertRelated(conn, insertExp, userId, experience);
                insertRelated(conn, insertSkill, userId, skills);
                insertRelated(conn, insertProj, userId, projects);

                conn.commit();
                return userId;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves a complete user profile including all related education, experience,
     * skills, projects, and photo information.
     *
     * @param userId the user's ID
     * @return a map containing the User object and lists of related data, or null if not found
     */
    public static Map<String, Object> getCompleteUser(int userId) {
        String selectUser = "SELECT id, full_name, email, phone, address, summary, photo_path FROM users WHERE id=?";
        String selectEdu = "SELECT details FROM education WHERE user_id=? ORDER BY id";
        String selectExp = "SELECT description FROM experience WHERE user_id=? ORDER BY id";
        String selectSkill = "SELECT skill_name FROM skills WHERE user_id=? ORDER BY id";
        String selectProj = "SELECT description FROM projects WHERE user_id=? ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection()) {
            User user = null;
            try (PreparedStatement ps = conn.prepareStatement(selectUser)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setFullName(rs.getString("full_name"));
                        user.setEmail(rs.getString("email"));
                        user.setPhone(rs.getString("phone"));
                        user.setAddress(rs.getString("address"));
                        user.setSummary(rs.getString("summary"));
                        user.setPhotoPath(rs.getString("photo_path"));
                    }
                }
            }

            if (user == null) return null;

            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("education", fetchStringList(conn, selectEdu, userId, "details"));
            result.put("experience", fetchStringList(conn, selectExp, userId, "description"));
            result.put("skills", fetchStringList(conn, selectSkill, userId, "skill_name"));
            result.put("projects", fetchStringList(conn, selectProj, userId, "description"));

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all users with their complete profile information.
     *
     * @return list of maps, each containing a User and their related data
     */
    public static List<Map<String, Object>> getAllCompleteUsers() {
        List<Map<String, Object>> results = new ArrayList<>();
        String selectAllUsers = "SELECT id FROM users ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(selectAllUsers)) {

            while (rs.next()) {
                Map<String, Object> userProfile = getCompleteUser(rs.getInt("id"));
                if (userProfile != null) {
                    results.add(userProfile);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Updates an entire user profile atomically, replacing all related data.
     *
     * @param user       the updated user personal information
     * @param education  updated list of education details
     * @param experience updated list of experience descriptions
     * @param skills     updated list of skill names
     * @param projects   updated list of project descriptions
     * @return true if the update succeeded, false otherwise
     */
    public static boolean updateCompleteUser(User user,
                                              List<String> education,
                                              List<String> experience,
                                              List<String> skills,
                                              List<String> projects) {
        String updateUser = "UPDATE users SET full_name=?, email=?, phone=?, address=?, summary=?, photo_path=? WHERE id=?";
        String deleteEdu = "DELETE FROM education WHERE user_id=?";
        String deleteExp = "DELETE FROM experience WHERE user_id=?";
        String deleteSkill = "DELETE FROM skills WHERE user_id=?";
        String deleteProj = "DELETE FROM projects WHERE user_id=?";
        String insertEdu = "INSERT INTO education(user_id, details) VALUES(?,?)";
        String insertExp = "INSERT INTO experience(user_id, description) VALUES(?,?)";
        String insertSkill = "INSERT INTO skills(user_id, skill_name) VALUES(?,?)";
        String insertProj = "INSERT INTO projects(user_id, description) VALUES(?,?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(updateUser)) {
                    ps.setString(1, user.getFullName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPhone());
                    ps.setString(4, user.getAddress());
                    ps.setString(5, user.getSummary());
                    ps.setString(6, user.getPhotoPath());
                    ps.setInt(7, user.getId());
                    ps.executeUpdate();
                }

                deleteRelated(conn, deleteEdu, user.getId());
                deleteRelated(conn, deleteExp, user.getId());
                deleteRelated(conn, deleteSkill, user.getId());
                deleteRelated(conn, deleteProj, user.getId());

                insertRelated(conn, insertEdu, user.getId(), education);
                insertRelated(conn, insertExp, user.getId(), experience);
                insertRelated(conn, insertSkill, user.getId(), skills);
                insertRelated(conn, insertProj, user.getId(), projects);

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user and all related data (education, experience, skills, projects).
     *
     * @param userId the user's ID
     * @return true if deletion succeeded, false otherwise
     */
    public static boolean deleteCompleteUser(int userId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                deleteRelated(conn, "DELETE FROM education WHERE user_id=?", userId);
                deleteRelated(conn, "DELETE FROM experience WHERE user_id=?", userId);
                deleteRelated(conn, "DELETE FROM skills WHERE user_id=?", userId);
                deleteRelated(conn, "DELETE FROM projects WHERE user_id=?", userId);

                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Private helpers ---

    private static void insertRelated(Connection conn, String sql, int userId, List<String> items) throws SQLException {
        if (items == null || items.isEmpty()) return;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String item : items) {
                ps.setInt(1, userId);
                ps.setString(2, item);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void deleteRelated(Connection conn, String sql, int userId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private static List<String> fetchStringList(Connection conn, String sql, int userId, String column) throws SQLException {
        List<String> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString(column));
                }
            }
        }
        return list;
    }
}

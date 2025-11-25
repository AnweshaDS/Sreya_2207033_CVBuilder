package com.example.sreya_2207033_cvbuilder.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            if (conn != null) {
                System.out.println(" DATABASE CONNECTED SUCCESSFULLY!");
            } else {
                System.out.println(" DATABASE CONNECTION FAILED!");
                return;
            }

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT,
                    email TEXT,
                    phone TEXT,
                    address TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS education (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    details TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS skills (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    skill_name TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS experience (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    description TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS projects (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    description TEXT
                );
            """);

            System.out.println(" All tables created or already exist.");
            System.out.println(" Database initialization complete.");

        } catch (SQLException e) {
            System.out.println(" DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.example.sreya_2207033_cvbuilder.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT,
                    email TEXT,
                    phone TEXT,
                    summary TEXT
                );
            """);

            // store each education row as a single TEXT 'details' (keeps UI -> DB simple)
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

            System.out.println("Database initialized (tables ensured).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

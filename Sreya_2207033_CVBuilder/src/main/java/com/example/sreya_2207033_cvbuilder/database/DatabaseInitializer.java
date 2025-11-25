package com.example.sreya_2207033_cvbuilder.database;

import java.sql.Connection;
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
                    address TEXT,
                    photo_path TEXT
                );
            """);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

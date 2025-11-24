package com.example.sreya_2207033_cvbuilder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:app.db"; // local file database

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

}

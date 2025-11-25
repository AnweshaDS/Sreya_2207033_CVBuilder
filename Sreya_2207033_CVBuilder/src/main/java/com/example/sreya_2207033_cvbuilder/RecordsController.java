package com.example.sreya_2207033_cvbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.sql.*;

public class RecordsController {

    @FXML
    private ListView<String> recordsList;

    @FXML
    public void initialize() {
        loadRecords();
    }

    private void loadRecords() {
        String sql = "SELECT id, full_name, email FROM users";

        try (Connection conn = com.example.sreya_2207033_cvbuilder.database.DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String row = "#" + rs.getInt("id") + ": " +
                        rs.getString("full_name") +
                        " (" + rs.getString("email") + ")";
                recordsList.getItems().add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
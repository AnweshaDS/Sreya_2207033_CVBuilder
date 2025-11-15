package com.example.sreya_2207033_cvbuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
public class CoverController {
    @FXML
    private Button createNewCvBtn;

    @FXML
    private void handleCreateNewCv() {
        try {
            // Load the next page (NextScreen.fxml must be in the same package)
            Parent root = FXMLLoader.load(getClass().getResource("NextScreen.fxml"));

            // Get current window (stage)
            Stage stage = (Stage) createNewCvBtn.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Optional: keep window size consistent
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the next page");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        // LATER, REPLACE WITH:
        // Parent root = FXMLLoader.load(getClass().getResource("NextScreen.fxml"));
        // Stage stage = (Stage) createNewCvBtn.getScene().getWindow();
        // stage.setScene(new Scene(root));
        // stage.show();
    }
}


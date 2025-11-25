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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NextScreen.fxml"));
            Parent root = loader.load();
            NextScreenController controller = loader.getController();

            Stage stage = (Stage) createNewCvBtn.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();



            stage.show();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the next page");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
}

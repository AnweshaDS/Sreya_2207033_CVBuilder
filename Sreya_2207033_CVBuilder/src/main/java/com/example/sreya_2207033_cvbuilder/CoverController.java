package com.example.sreya_2207033_cvbuilder;

import javafx.application.Platform;
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

    private void navigateTo(String fxmlFile, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) createNewCvBtn.getScene().getWindow();

        boolean wasMaximized = stage.isMaximized();
        double width = stage.getWidth();
        double height = stage.getHeight();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);

        if (wasMaximized) {
            stage.setMaximized(true);
        } else {
            stage.setWidth(width);
            stage.setHeight(height);
            stage.centerOnScreen();
        }

        stage.show();
    }

    @FXML
    private void handleCreateNewCv() {
        try {
            navigateTo("NextScreen.fxml", "Create New CV");

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the next page");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    private void handleSeeRecords() {
        try {
            navigateTo("RecordsView.fxml", "Saved Records");
        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load records page");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();

    }
}

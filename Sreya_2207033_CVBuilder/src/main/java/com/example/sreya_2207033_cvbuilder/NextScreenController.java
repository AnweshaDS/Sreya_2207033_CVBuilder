package com.example.sreya_2207033_cvbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NextScreenController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private TextArea educationField;
    @FXML private TextArea skillsField;
    @FXML private TextArea experienceField;
    @FXML private TextArea projectsField;

    @FXML
    private void handleGenerateCV() {

        if (fullNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() ||
                phoneField.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Information");
            alert.setHeaderText("Please fill all required fields.");
            alert.setContentText("Full Name, Email, and Phone are mandatory.");
            alert.showAndWait();
            return;
        }

        // TEMPORARY: Show a confirmation message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CV Generated!");
        alert.setHeaderText("Your CV has been generated.");
        alert.setContentText("Next step: export as PDF/Word (to be added soon).");
        alert.showAndWait();
    }
}

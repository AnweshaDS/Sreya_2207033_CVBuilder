package com.example.sreya_2207033_cvbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class PreviewCVController {

    @FXML private ImageView previewPhoto;

    @FXML private Label previewName;
    @FXML private Label previewEmail;
    @FXML private Label previewPhone;
    @FXML private Label previewAddress;

    @FXML private Label previewSkills;
    @FXML private Label previewExperience;
    @FXML private Label previewProjects;

    @FXML private VBox previewEducationList;

    //  METHOD CALLED FROM NextScreenController
    public void setData(CVData data) {

        previewName.setText(data.fullName);
        previewEmail.setText("Email: " + data.email);
        previewPhone.setText("Phone: " + data.phone);
        previewAddress.setText("Address: " + data.address);

        previewSkills.setText(data.skills);
        previewExperience.setText(data.experience);
        previewProjects.setText(data.projects);

        if (data.photo != null) {
            previewPhoto.setImage(data.photo);
        }

        // Add education entries dynamically
        for (String edu : data.educationList) {
            Label label = new Label(edu);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFDAB9;");
            previewEducationList.getChildren().add(label);
        }

    }

    @FXML
    private void handleDone() {
        System.exit(0);
    }
}

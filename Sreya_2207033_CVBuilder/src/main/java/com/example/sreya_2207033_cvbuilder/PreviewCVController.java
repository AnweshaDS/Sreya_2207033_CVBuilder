package com.example.sreya_2207033_cvbuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    public void setData(CVData data) {

        // Basic info
        previewName.setText(data.getFullName());
        previewEmail.setText("Email: " + data.getEmail());
        previewPhone.setText("Phone: " + data.getPhone());
        previewAddress.setText("Address: " + data.getAddress());

        // Text sections
        previewSkills.setText(data.getSkills());
        previewExperience.setText(data.getExperience());
        previewProjects.setText(data.getProjects());

        // Photo
        if (data.getPhoto() != null) {
            previewPhoto.setImage(data.getPhoto());
        }

        // Education list
        previewEducationList.getChildren().clear();
        if (data.getEducationList() != null) {
            for (String edu : data.getEducationList()) {
                Label label = new Label(edu);
                label.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFDAB9;");
                previewEducationList.getChildren().add(label);
            }
        }
    }

    private void openRecordsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordsView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Saved Records");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleDone() {
        openRecordsWindow();
    }
}

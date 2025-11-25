package com.example.sreya_2207033_cvbuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
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

        for (String edu : data.educationList) {
            Label label = new Label(edu);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFDAB9;");
            previewEducationList.getChildren().add(label);
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
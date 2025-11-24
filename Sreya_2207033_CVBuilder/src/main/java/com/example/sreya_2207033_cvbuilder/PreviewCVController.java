package com.example.sreya_2207033_cvbuilder;

import com.example.sreya_2207033_cvbuilder.database.dao.EducationDAO;
import com.example.sreya_2207033_cvbuilder.database.dao.SkillDAO;
import com.example.sreya_2207033_cvbuilder.database.dao.UserDAO;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    private CVData cvData; // store passed data

    public void setData(CVData data) {
        this.cvData = data;

        previewName.setText(data.getFullName());
        previewEmail.setText("Email: " + data.getEmail());
        previewPhone.setText("Phone: " + data.getPhone());
        previewAddress.setText("Address: " + data.getAddress());

        previewSkills.setText(data.getSkills());
        previewExperience.setText(data.getExperience());
        previewProjects.setText(data.getProjects());

        if (data.getPhoto() != null) {
            previewPhoto.setImage(data.getPhoto());
        }

        previewEducationList.getChildren().clear();
        for (String edu : data.getEducationList()) {
            Label label = new Label(edu);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFDAB9;");
            previewEducationList.getChildren().add(label);
        }
    }

    @FXML
    private void handleDone() {
        Stage stage = (Stage) previewName.getScene().getWindow();
        stage.close();
    }

}

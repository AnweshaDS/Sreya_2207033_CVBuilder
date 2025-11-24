package com.example.sreya_2207033_cvbuilder;

import com.example.sreya_2207033_cvbuilder.database.dao.EducationDAO;
import com.example.sreya_2207033_cvbuilder.database.dao.SkillDAO;
import com.example.sreya_2207033_cvbuilder.database.dao.UserDAO;
import com.example.sreya_2207033_cvbuilder.model.User;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

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

    private CVData cvData; // store the data passed in

    public void setData(CVData data) {
        this.cvData = data;

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

        previewEducationList.getChildren().clear();
        for (String edu : data.educationList) {
            Label label = new Label(edu);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: #FFDAB9;");
            previewEducationList.getChildren().add(label);
        }
    }

    // Called by the Done button — now saves the CV to DB before closing
    @FXML
    private void handleDone() {
        if (cvData == null) {
            // nothing to save, just exit
            System.exit(0);
            return;
        }

        // Run DB save in background so UI doesn't freeze
        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                UserDAO userDAO = new UserDAO();
                EducationDAO eduDAO = new EducationDAO();
                SkillDAO skillDAO = new SkillDAO();

                // 1) insert user
                com.example.sreya_2207033_cvbuilder.model.User user =
                        new com.example.sreya_2207033_cvbuilder.model.User(cvData.fullName, cvData.email, cvData.phone, null);
                int userId = userDAO.insertUser(user);

                // 2) insert education rows (each entry is a formatted string)
                for (String edu : cvData.educationList) {
                    eduDAO.insertEducation(userId, edu);
                }

                // 3) insert skills
                // split skills by newline or comma
                if (cvData.skills != null && !cvData.skills.isEmpty()) {
                    String[] parts = cvData.skills.split("\\r?\\n|,");
                    for (String s : parts) {
                        String skill = s.trim();
                        if (!skill.isEmpty()) {
                            skillDAO.insertSkill(userId, skill);
                        }
                    }
                }

                return null;
            }
        };

        saveTask.setOnSucceeded(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saved");
            alert.setHeaderText("CV saved");
            alert.setContentText("Your CV was saved to the local database.");
            alert.showAndWait();

            // close the preview stage
            Stage stage = (Stage) previewName.getScene().getWindow();
            stage.close();
        });

        saveTask.setOnFailed(e -> {
            Throwable ex = saveTask.getException();
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save failed");
            alert.setHeaderText("Failed to save CV");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        });

        new Thread(saveTask).start();
    }
}

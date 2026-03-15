package com.example.sreya_2207033_cvbuilder;

import com.example.sreya_2207033_cvbuilder.database.DatabaseHelper;
import com.example.sreya_2207033_cvbuilder.database.dao.*;
import com.example.sreya_2207033_cvbuilder.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NextScreenController {

    private File chosenProfileFile = null;

    @FXML private VBox educationList;

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cover.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(new Scene(root));
            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
                stage.centerOnScreen();
            }
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML private ImageView profileImageView;

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private TextArea skillsField;
    @FXML private TextArea experienceField;
    @FXML private TextArea projectsField;

    private static class EducationEntry {
        TextField degree;
        TextField institute;
        TextField cgpa;
        TextField year;
    }
    private final List<EducationEntry> educationEntries = new ArrayList<>();

    @FXML
    private void handleAddEducation() {
        HBox row = new HBox(10);
        row.setStyle("-fx-padding:5;");
        EducationEntry e = new EducationEntry();
        e.degree = new TextField(); e.degree.setPromptText("Degree"); e.degree.setPrefWidth(200);
        e.institute = new TextField(); e.institute.setPromptText("Institute"); e.institute.setPrefWidth(250);
        e.cgpa = new TextField(); e.cgpa.setPromptText("CGPA/GPA"); e.cgpa.setPrefWidth(120);
        e.year = new TextField(); e.year.setPromptText("Year"); e.year.setPrefWidth(100);
        row.getChildren().addAll(e.degree, e.institute, e.cgpa, e.year);
        educationEntries.add(e);
        educationList.getChildren().add(row);
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        chosenProfileFile = chooser.showOpenDialog(null);
        if (chosenProfileFile != null) {
            Image im = new Image(chosenProfileFile.toURI().toString());
            profileImageView.setImage(im);
        }
    }

    @FXML
    private void handleSeeRecords() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordsView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(new Scene(root));
            stage.setTitle("Saved Records");
            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
                stage.centerOnScreen();
            }
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open records: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private void handleGenerateCV() {
        if (fullNameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Full name, email and phone are required").showAndWait();
            return;
        }

        CVData data = new CVData();
        data.setFullName(fullNameField.getText());
        data.setEmail(emailField.getText());
        data.setPhone(phoneField.getText());
        data.setAddress(addressField.getText());
        data.setSkills(skillsField.getText());
        data.setExperience(experienceField.getText());
        data.setProjects(projectsField.getText());
        data.setPhoto(profileImageView.getImage());

        List<String> eduList = new ArrayList<>();
        for (EducationEntry e : educationEntries) {
            eduList.add(e.degree.getText() + " - " + e.institute.getText()
                    + " | CGPA: " + e.cgpa.getText() + " | Year: " + e.year.getText());
        }
        data.setEducationList(eduList);

        // --- save to DB ---
        try {
            String savedPhotoPath = null;
            if (chosenProfileFile != null) {
                savedPhotoPath = DatabaseHelper.copyPhotoToApp(chosenProfileFile);
            }

            UserDAO userDAO = new UserDAO();
            EducationDAO eduDAO = new EducationDAO();
            SkillDAO skillDAO = new SkillDAO();
            ExperienceDAO expDAO = new ExperienceDAO();
            ProjectDAO projDAO = new ProjectDAO();

            User user = new User(data.getFullName(), data.getEmail(), data.getPhone(), data.getAddress(), savedPhotoPath);
            int userId = userDAO.insert(user, savedPhotoPath);

            // education
            for (String ed : data.getEducationList()) {
                eduDAO.insert(userId, ed);
            }
            // skills: split by comma or newline
            if (data.getSkills() != null && !data.getSkills().isEmpty()) {
                String[] parts = data.getSkills().split("\\r?\\n|,");
                for (String p : parts) {
                    String s = p.trim();
                    if (!s.isEmpty()) skillDAO.insert(userId, s);
                }
            }
            // experience: split by newline or comma
            if (data.getExperience() != null && !data.getExperience().isEmpty()) {
                String[] parts = data.getExperience().split("\\r?\\n|,");
                for (String p : parts) {
                    String item = p.trim();
                    if (!item.isEmpty()) expDAO.insert(userId, item);
                }
            }
            // projects: split by newline or comma
            if (data.getProjects() != null && !data.getProjects().isEmpty()) {
                String[] parts = data.getProjects().split("\\r?\\n|,");
                for (String p : parts) {
                    String item = p.trim();
                    if (!item.isEmpty()) projDAO.insert(userId, item);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save to database: " + ex.getMessage()).showAndWait();
            return;
        }

        // open preview in the same stage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PreviewCV.fxml"));
            Parent root = loader.load();
            PreviewCVController controller = loader.getController();
            controller.setData(data);

            Stage stage = (Stage) fullNameField.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(new Scene(root));
            stage.setTitle("CV Preview");
            if (wasMaximized) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
                stage.centerOnScreen();
            }
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open preview: " + e.getMessage()).showAndWait();
        }
    }
}

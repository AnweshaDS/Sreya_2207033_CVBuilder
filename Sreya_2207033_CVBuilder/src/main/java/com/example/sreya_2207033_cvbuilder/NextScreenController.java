package com.example.sreya_2207033_cvbuilder;

import com.example.sreya_2207033_cvbuilder.database.dao.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NextScreenController {

    @FXML private VBox educationList;
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
        row.setStyle("-fx-padding: 5;");

        EducationEntry entry = new EducationEntry();
        entry.degree = new TextField();
        entry.institute = new TextField();
        entry.cgpa = new TextField();
        entry.year = new TextField();

        entry.degree.setPromptText("Degree");
        entry.institute.setPromptText("Institute");
        entry.cgpa.setPromptText("CGPA/GPA");
        entry.year.setPromptText("Year");

        row.getChildren().addAll(
                entry.degree,
                entry.institute,
                entry.cgpa,
                entry.year
        );

        educationEntries.add(entry);
        educationList.getChildren().add(row);
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            profileImageView.setImage(image);
        }
    }

    @FXML
    private void handleGenerateCV() throws SQLException {
        System.out.println("BUTTON CLICKED");

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

        // ===== Collect CV Data =====
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
            eduList.add(
                    e.degree.getText() + " - " +
                            e.institute.getText() + " | CGPA: " +
                            e.cgpa.getText() + " | Year: " +
                            e.year.getText()
            );
        }
        data.setEducationList(eduList);


        // ===== STORE TO DATABASE =====
        UserDAO userDAO = new UserDAO();
        int userId = userDAO.insert(
                new com.example.sreya_2207033_cvbuilder.model.User(
                        data.getFullName(),
                        data.getEmail(),
                        data.getPhone(),
                        data.getAddress()
                )
        );

        EducationDAO eduDAO = new EducationDAO();
        for (String eduText : data.getEducationList()) {
            eduDAO.insertEducation(userId, eduText);
        }

        SkillDAO skillDAO = new SkillDAO();
        for (String skill : data.getSkills().split(",")) {
            skillDAO.insertSkill(userId, skill.trim());
        }

        ExperienceDAO expDAO = new ExperienceDAO();
        expDAO.insert(userId, data.getExperience());

        ProjectDAO projDAO = new ProjectDAO();
        projDAO.insert(userId, data.getProjects());


        // ===== OPEN PREVIEW SCREEN =====
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sreya_2207033_cvbuilder/PreviewCV.fxml"));
            Parent root = loader.load();

            PreviewCVController controller = loader.getController();
            controller.setData(data);

            Stage stage = new Stage();
            stage.setTitle("CV Preview");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CV Generated!");
        alert.setHeaderText("Your CV has been generated.");
        alert.setContentText("Preview window opened.");
        alert.showAndWait();
    }
}

package com.example.sreya_2207033_cvbuilder;

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
import java.util.ArrayList;
import java.util.List;
import com.example.sreya_2207033_cvbuilder.model.User;
import com.example.sreya_2207033_cvbuilder.model.Education;
import com.example.sreya_2207033_cvbuilder.model.Skill;
import com.example.sreya_2207033_cvbuilder.model.Experience;
import com.example.sreya_2207033_cvbuilder.model.Project;


public class NextScreenController {

    // ========= FXML FIELDS =========
    @FXML private VBox educationList;
    @FXML private ImageView profileImageView;

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private TextArea skillsField;
    @FXML private TextArea experienceField;
    @FXML private TextArea projectsField;

    // ========= EDUCATION ENTRY CLASS =========
    private static class EducationEntry {
        TextField degree;
        TextField institute;
        TextField cgpa;
        TextField year;
    }

    private final List<EducationEntry> educationEntries = new ArrayList<>();


    // ========= ADD EDUCATION ROW =========
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
        entry.degree.setPrefWidth(200);

        entry.institute.setPromptText("Institute");
        entry.institute.setPrefWidth(250);

        entry.cgpa.setPromptText("CGPA/GPA");
        entry.cgpa.setPrefWidth(120);

        entry.year.setPromptText("Year");
        entry.year.setPrefWidth(100);

        row.getChildren().addAll(
                entry.degree,
                entry.institute,
                entry.cgpa,
                entry.year
        );

        educationEntries.add(entry);
        educationList.getChildren().add(row);
    }


    // ========= UPLOAD PROFILE PHOTO =========
    @FXML
    private void handleUploadPhoto() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Photo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Images", "*.png", "*.jpg", "*.jpeg"
                )
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            profileImageView.setImage(image);
        }
    }


    // ========= GENERATE CV =========
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
        // ===== Collect Data =====
        CVData data = new CVData();
        data.fullName = fullNameField.getText();
        data.email = emailField.getText();
        data.phone = phoneField.getText();
        data.address = addressField.getText();

        data.skills = skillsField.getText();
        data.experience = experienceField.getText();
        data.projects = projectsField.getText();

        data.photo = profileImageView.getImage();

// Collect education rows
        List<String> list = new ArrayList<>();
        for (EducationEntry e : educationEntries) {
            String formatted = e.degree.getText() + " - " +
                    e.institute.getText() + " | CGPA: " +
                    e.cgpa.getText() + " | Year: " +
                    e.year.getText();

            list.add(formatted);
        }
        data.educationList = list;
        // SAVE TO DB
        User user = new User();
        user.fullName = data.fullName;
        user.email = data.email;
        user.phone = data.phone;
        user.address = data.address;

        UserDAO userDAO = new UserDAO();
        int userId = userDAO.insert(user);

// save education list
        EducationDAO eduDAO = new EducationDAO();
        for (String edu : data.educationList) {
            String[] parts = edu.split("\\|");
            Education e = new Education();
            e.userId = userId;
            // parse degree, institution, cgpa, year properly
            eduDAO.insert(e);
        }

// save skills
        SkillDAO skillDAO = new SkillDAO();
        for (String s : data.skills.split(",")) {
            Skill sk = new Skill();
            sk.userId = userId;
            sk.skill = s.trim();
            skillDAO.insert(sk);
        }

// experience
        ExperienceDAO expDAO = new ExperienceDAO();
        Experience exp = new Experience();
        exp.userId = userId;
        exp.description = data.experience;
        expDAO.insert(exp);

// projects
        ProjectDAO projDAO = new ProjectDAO();
        Project pr = new Project();
        pr.userId = userId;
        pr.description = data.projects;
        projDAO.insert(pr);


// ===== Load PreviewCV.fxml =====
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PreviewCV.fxml"));
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
        alert.setContentText("Next step: show formatted preview screen.");
        alert.showAndWait();
    }
}

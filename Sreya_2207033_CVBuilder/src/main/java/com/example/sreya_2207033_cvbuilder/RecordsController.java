package com.example.sreya_2207033_cvbuilder;

import com.example.sreya_2207033_cvbuilder.database.dao.*;
import com.example.sreya_2207033_cvbuilder.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class RecordsController {

    @FXML private VBox recordsContainer;
    @FXML private Button backBtn;

    @FXML
    private void initialize() {
        loadAllRecords();
    }

    private void loadAllRecords() {
        recordsContainer.getChildren().clear();
        try {
            UserDAO userDAO = new UserDAO();
            EducationDAO eduDAO = new EducationDAO();
            SkillDAO skillDAO = new SkillDAO();
            ExperienceDAO expDAO = new ExperienceDAO();
            ProjectDAO projDAO = new ProjectDAO();

            List<User> users = userDAO.getAll();

            if (users.isEmpty()) {
                Label empty = new Label("No records found.");
                empty.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 16px;");
                recordsContainer.getChildren().add(empty);
                return;
            }

            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                List<Education> eduList = eduDAO.getByUser(u.getId());
                List<Skill> skillList = skillDAO.getByUser(u.getId());
                List<Experience> expList = expDAO.getByUser(u.getId());
                List<Project> projList = projDAO.getByUser(u.getId());

                VBox card = buildCard(i + 1, u, eduList, skillList, expList, projList);
                recordsContainer.getChildren().add(card);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Label err = new Label("Failed to load records: " + ex.getMessage());
            err.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 14px;");
            recordsContainer.getChildren().add(err);
        }
    }

    private VBox buildCard(int index, User u, List<Education> eduList,
                           List<Skill> skillList, List<Experience> expList,
                           List<Project> projList) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: #16213e; -fx-border-color: #0f3460;"
                + " -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        card.setPadding(new Insets(16));

        // --- Top row: photo + name/contact ---
        HBox topRow = new HBox(20);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Profile photo
        ImageView photo = new ImageView();
        photo.setFitWidth(100);
        photo.setFitHeight(100);
        photo.setPreserveRatio(true);
        photo.setStyle("-fx-border-color: #0f3460; -fx-border-width: 2;");
        if (u.getPhotoPath() != null) {
            File f = new File(u.getPhotoPath());
            if (f.exists()) {
                photo.setImage(new Image(f.toURI().toString()));
            }
        }

        // Name + contact stacked beside the photo
        VBox nameBlock = new VBox(5);
        Label header = new Label("#" + index + "  " + u.getFullName());
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        header.setStyle("-fx-text-fill: #e94560;");
        nameBlock.getChildren().add(header);
        nameBlock.getChildren().add(infoLabel("Email", u.getEmail()));
        nameBlock.getChildren().add(infoLabel("Phone", u.getPhone()));
        nameBlock.getChildren().add(infoLabel("Address", u.getAddress()));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 6 14 6 14;");
        deleteBtn.setOnAction(e -> handleDeleteRecord(u));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(photo, nameBlock, spacer, deleteBtn);
        card.getChildren().add(topRow);

        card.getChildren().add(new Separator());

        // --- Education ---
        if (!eduList.isEmpty()) {
            card.getChildren().add(sectionLabel("Education"));
            for (Education e : eduList) {
                card.getChildren().add(bulletLabel(e.getDetails()));
            }
        }

        // --- Skills ---
        if (!skillList.isEmpty()) {
            card.getChildren().add(sectionLabel("Skills"));
            StringBuilder sb = new StringBuilder();
            for (Skill s : skillList) {
                if (sb.length() > 0) sb.append(",  ");
                sb.append(s.getSkillName());
            }
            card.getChildren().add(bulletLabel(sb.toString()));
        }

        // --- Experience ---
        if (!expList.isEmpty()) {
            card.getChildren().add(sectionLabel("Experience"));
            for (Experience e : expList) {
                addBulletedLines(card, e.getDescription());
            }
        }

        // --- Projects ---
        if (!projList.isEmpty()) {
            card.getChildren().add(sectionLabel("Projects"));
            for (Project p : projList) {
                addBulletedLines(card, p.getDescription());
            }
        }

        return card;
    }

    private Label infoLabel(String field, String value) {
        Label lbl = new Label(field + ":  " + (value != null ? value : ""));
        lbl.setStyle("-fx-text-fill: #c8d6e5; -fx-font-size: 14px;");
        lbl.setWrapText(true);
        return lbl;
    }

    private Label sectionLabel(String title) {
        Label lbl = new Label(title);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-text-fill: #FFF8DC; -fx-padding: 6 0 2 0;");
        return lbl;
    }

    private Label bulletLabel(String text) {
        Label lbl = new Label("  •  " + (text != null ? text : ""));
        lbl.setStyle("-fx-text-fill: #a8d8ea; -fx-font-size: 13px;");
        lbl.setWrapText(true);
        return lbl;
    }

    private void addBulletedLines(VBox container, String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return;
        }
        String[] lines = raw.split("\\r?\\n|,");
        for (String line : lines) {
            String item = line.trim();
            if (!item.isEmpty()) {
                container.getChildren().add(bulletLabel(item));
            }
        }
    }

    private void handleDeleteRecord(User user) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete CV");
        confirm.setHeaderText("Delete this CV record?");
        confirm.setContentText("This action cannot be undone for " + user.getFullName() + ".");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.delete(user.getId());
                loadAllRecords();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Delete Failed");
                error.setHeaderText("Could not delete CV record");
                error.setContentText(ex.getMessage());
                error.showAndWait();
            }
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cover.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(new Scene(root));
            stage.setTitle("CVBuilder!");
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
}

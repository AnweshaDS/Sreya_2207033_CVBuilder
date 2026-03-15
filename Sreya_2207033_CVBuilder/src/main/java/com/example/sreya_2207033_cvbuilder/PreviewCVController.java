package com.example.sreya_2207033_cvbuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PreviewCVController {

    @FXML private ImageView previewPhoto;
    @FXML private Label previewName, previewEmail, previewPhone, previewAddress;
    @FXML private Label previewSkills, previewExperience, previewProjects;
    @FXML private VBox previewEducationList;

    public void setData(CVData data) {
        previewName.setText(data.getFullName());
        previewEmail.setText("Email: " + data.getEmail());
        previewPhone.setText("Phone: " + data.getPhone());
        previewAddress.setText("Address: " + data.getAddress());
        previewSkills.setText(data.getSkills());
        previewExperience.setText(formatBulletedText(data.getExperience()));
        previewProjects.setText(formatBulletedText(data.getProjects()));
        if (data.getPhoto() != null) previewPhoto.setImage(data.getPhoto());
        previewEducationList.getChildren().clear();
        if (data.getEducationList() != null) {
            for (String e : data.getEducationList()) {
                Label lbl = new Label(e);
                lbl.setStyle("-fx-font-size:14px; -fx-text-fill:#FFDAB9;");
                previewEducationList.getChildren().add(lbl);
            }
        }
    }

    private String formatBulletedText(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        String[] lines = raw.split("\\r?\\n");
        for (String line : lines) {
            String item = line.trim();
            if (!item.isEmpty()) {
                if (out.length() > 0) {
                    out.append("\n");
                }
                out.append("• ").append(item);
            }
        }
        return out.toString();
    }

    private void navigateTo(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) previewName.getScene().getWindow();
            boolean wasMaximized = stage.isMaximized();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
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

    @FXML
    private void handleDone() {
        new Alert(Alert.AlertType.INFORMATION, "Saved successfully.").showAndWait();
    }

    @FXML
    private void handleSeeRecords() {
        navigateTo("RecordsView.fxml", "Saved Records");
    }

    @FXML
    private void handleBackToCover() {
        navigateTo("Cover.fxml", "CVBuilder!");
    }
}

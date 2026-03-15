package com.example.sreya_2207033_cvbuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        com.example.sreya_2207033_cvbuilder.database.DatabaseInitializer.initialize();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Cover.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CVBuilder!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (!isNowMaximized) {
                Platform.runLater(() -> {
                    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                    double w = bounds.getWidth() * 0.75;
                    double h = bounds.getHeight() * 0.75;
                    stage.setWidth(w);
                    stage.setHeight(h);
                    stage.centerOnScreen();
                });
            }
        });
        stage.show();
    }
}

package com.example.sreya_2207033_cvbuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        stage.setFullScreen(true);
        stage.show();
    }
}

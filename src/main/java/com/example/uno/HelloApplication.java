package com.example.uno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/uno/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 800);
        stage.setTitle("UNO!");
        // 🔒 Bloquear redimensionamiento
        stage.setResizable(false);
        // 🔒 Ocultar botón de maximizar
        stage.initStyle(javafx.stage.StageStyle.DECORATED); // ya viene por defecto, solo para claridad
        stage.centerOnScreen(); //centra la ventana
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
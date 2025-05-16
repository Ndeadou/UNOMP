package com.example.uno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Esta clase es el punto de entrada de la aplicación JavaFX.
 * Se encarga de cargar el FXML (hello-view.fxml), levantar la escena y mostrar la ventana principal.
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class HelloApplication extends Application {

    /**
     * Este método se encarga de cargar la escena que guarda el fxml (interfaz gráfica)
     * @param stage
     * @throws IOException
     */
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

    /**
     * Este método Inicializa el entorno de JavaFX
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

}
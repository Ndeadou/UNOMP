package com.example.uno.Model;


import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.concurrent.atomic.AtomicReference;


public class VentanaEmergente {


    public static int mostrarDialogoSeleccionColor() {
        Dialog<Integer> dialogo = new Dialog<>();
        dialogo.setTitle("Selecciona un color");
        dialogo.setHeaderText("Elige un color para continuar");
        dialogo.setContentText("Haz clic en un botón de color:");


        ButtonType azulBotonTipo = new ButtonType("Azul");
        ButtonType verdeBotonTipo = new ButtonType("Verde");
        ButtonType rojoBotonTipo = new ButtonType("Rojo");
        ButtonType amarilloBotonTipo = new ButtonType("Amarillo");


        dialogo.getDialogPane().getButtonTypes().addAll(azulBotonTipo, verdeBotonTipo, rojoBotonTipo, amarilloBotonTipo);


        // Evitar que el usuario cierre con la "X"
        Stage stage = (Stage) dialogo.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(event -> {
            event.consume(); // Consume el evento de cierre, impidiendo que la ventana se cierre
        });


        dialogo.setResultConverter(button -> {
            if (button == azulBotonTipo) {
                return 1;
            } else if (button == verdeBotonTipo) {
                return 2;
            } else if (button == rojoBotonTipo) {
                return 3;
            } else if (button == amarilloBotonTipo) {
                return 4;
            }
            return null; // Esto no debería ocurrir si los botones están bien definidos
        });


        dialogo.showAndWait(); // Bloquea hasta que se cierre el diálogo


        return dialogo.getResult(); // Devuelve el entero seleccionado
    }
}

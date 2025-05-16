package com.example.uno.Controller;

import com.example.uno.Model.MesaDeJuego;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Esta clase es el controlador de la vista FXML.
 * Inyecta los nodos (Pane del mazo, Button de UNO, ImageView de la pila, Label de color, etc.).
 * Inicializa el motor de juego (lógica) (MesaDeJuego) pasándole esos nodos.
 * Actúa como puente entre los eventos de UI (clics, pulsaciones) y el modelo de juego.
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class HelloController {

    @FXML
    private Label lblColorActual;

    @FXML
    private Pane idMazo1;

    @FXML
    private  Pane idMazo2;

    @FXML
    private  ImageView pila;

    @FXML
    private Button unoButton;

    private MesaDeJuego mesa;

    /**
     * Este método conecta la lógica del juego con los componentes gráficos
     * y arranca la partida en cuanto la ventana aparece
     */
    @FXML
    public void initialize() {
        mesa = new MesaDeJuego( idMazo1, idMazo2, unoButton, pila, lblColorActual);
        mesa.jugar();
    }

    /**
     * Este método se encarga de mostrar una ventana emergente
     * con las reglas e indicaciones del juego
     */
    @FXML
    public void mostrarInstrucciones() {
        // Crear texto de instrucciones
        Label instrucciones = new Label("""
        Instrucciones del juego UNO:

        1. Coloca cartas del mismo color, número o simbolo en caso del bloqueo.
        2. Usa cartas especiales: +2, +4, bloqueo, cambio de color.
        3. Pulsa 'UNO' cuando te quede una sola carta.
        4. Si en tu turno no tienes cartas validas, la baraja te
        repartirá una carta automáticamente
        
        Funciones de comodines:
        
        Cambio de color: Dicta de que color debe ser la proxima carta a jugar
        
        +4, +2: Penaliza al oponente comiendo 2 o 4 cartas respectivamente; además perderá su turno
        
        Bloqueo: El oponente perderá su próximo turno
        
        ¡Buena suerte!
        
        Juego desarrollado por: 
        Erick Obando
        Miguel Ángel
        """);
        instrucciones.setWrapText(true);
        instrucciones.setStyle("-fx-font-size: 13px;");

        // Contenedor de los elementos
        VBox layout = new VBox(20, instrucciones);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: white;");
        layout.setAlignment(Pos.CENTER);

        // Crear la nueva ventana
        Scene scene = new Scene(layout, 400, 600);
        Stage ventana = new Stage();
        ventana.setTitle("Instrucciones del Juego");
        ventana.setScene(scene);
        ventana.setResizable(false);
        ventana.show();
    }
}
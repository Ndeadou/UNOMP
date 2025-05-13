package com.example.uno.Controller;

import com.example.uno.Model.Cartas;
import com.example.uno.Model.MesaDeJuego;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Button idBaraja;

    @FXML
    private Label lblColorActual;

    @FXML
    private Button btnInstrucciones;

    @FXML
    private Pane idMazo1;

    @FXML
    private  Pane idMazo2;

    @FXML
    private  ImageView pila;

    //btn
    @FXML
    private Button unoButton;
    //btn

    private MesaDeJuego mesa = new MesaDeJuego(HelloController.this);




    @FXML
    public void initialize() {
        System.out.println("Tamaño de la baraja después de crear: " + mesa.barajaSiz());
        mesa.jugar(idMazo1, idMazo2);
    }

    @FXML
    public void darClickBaraja(){
        mesa.repPlayer( idMazo1);// Pasar idMazo1 como argumento
        System.out.println("Tamaño de la baraja después de repartir: " + mesa.barajaSiz());
    }

    //btn
    /**
     * 3) Método invocado al hacer clic en el botón “UNO”.
     *    Marca la pulsación en la lógica y deshabilita el botón.
     */
    @FXML
    public void handleUnoButton(ActionEvent event) {
        mesa.onHumanPressUno();
        unoButton.setDisable(true);
    }
    //btn

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


    public Pane getMazoPlayer() {
        return idMazo1;
    }
    public Pane getMazoCpu() {
        return idMazo2;
    }

    //btn
    /**
     * 4) Getter para que MesaDeJuego pueda habilitar/deshabilitar el botón.
     */
    public Button getUnoButton() {
        return unoButton;
        /*EJEMPLO: Si no existiera este getter, MesaDeJuego no podría acceder al botón y no funcionaría la activación
         cuando alguien llegue a ultima carta.*/
    }
    //btn


    /**La siguiente funcion recibe el evento clicked definida en mesa de Juego, para que funcionara se hicieron modificaciones
     * en la clase MesaDeJuego, importamos la clase controladora para instanciar un controlador y añadirlo como atributo
     * al constructor de la mesa de juego(revisar la linea 15 y 17 en MesaDeJuego y 29 en HelloController).
     * Todo esto con el fin de poder usar la funcion de abajo dentro de la clase MesaDeJuego.
     * En resumen se instancio el controlador original en mesa de juego para usar la funcion de abajo.

     */
    public void manejarClicCarta(MouseEvent event) {
        Button cartaButtonClickeada = (Button) event.getSource();
        Cartas cartaAsociada = (Cartas) cartaButtonClickeada.getUserData();
        if (cartaAsociada != null) {
            String rutaImagen = cartaAsociada.getRutaImagen();
            Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));
            pila.setImage(imagen);
        } else {
            System.out.println("No se encontró información de la carta en el botón.");
        }
    }
    public void leerNuevaPila(Cartas carta) {
        Image imagen = new Image(getClass().getResourceAsStream(carta.getRutaImagen()));
        pila.setImage(imagen);
    }

    public void primCarta(String cartaRuta) {
        Image imagen = new Image(getClass().getResourceAsStream(cartaRuta));
        pila.setImage(imagen);
    }

    public void mostrarColorActual(int color) {
        String texto = switch (color) {
            case 1 -> "Azul";
            case 2 -> "Verde";
            case 3 -> "Rojo";
            case 4 -> "Amarillo";
            default -> "Color inválido";
        };
        lblColorActual.setText("Color elegido: " + texto);
    }
    public void resetearColorActual(){
        lblColorActual.setText("");
    }

}
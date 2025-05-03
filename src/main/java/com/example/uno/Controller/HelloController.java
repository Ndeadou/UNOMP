package com.example.uno.Controller;

import com.example.uno.Model.MesaDeJuego;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
//Los siguientes imports son para leer la imagen del boton clickeado gracias al evento asignado en mesa de juego
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import com.example.uno.Model.Cartas;

import javax.swing.*;

public class HelloController {
    @FXML
    private Button idBaraja;

    @FXML
    private  HBox idMazo1;

    @FXML
    private  HBox idMazo2;

    @FXML
    private  ImageView pila;

    private MesaDeJuego mesa = new MesaDeJuego(HelloController.this);

    @FXML
    public void initialize() {
        mesa.crearCartas();
        mesa.mezclarBaraja();
        for(int i =0; i<5;i++){
            darClickBaraja();
        }
        System.out.println("Tamaño de la baraja después de crear: " + mesa.barajaSiz());
    }

    @FXML
    public void darClickBaraja(){
        mesa.repCartas(mesa.jugadorH, idMazo1);// Pasar idMazo1 como argumento
        System.out.println("Tamaño de la baraja después de repartir: " + mesa.barajaSiz());
    }


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


}
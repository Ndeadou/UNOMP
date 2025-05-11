package com.example.uno.Controller;

import com.example.uno.Model.Cartas;
import com.example.uno.Model.MesaDeJuego;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

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
        System.out.println("Tamaño de la baraja después de crear: " + mesa.barajaSiz());
        mesa.jugar(idMazo1, idMazo2);
    }

    @FXML
    public void darClickBaraja(){
        mesa.repPlayer( idMazo1);// Pasar idMazo1 como argumento
        System.out.println("Tamaño de la baraja después de repartir: " + mesa.barajaSiz());
    }

    public HBox getMazoPlayer() {
        return idMazo1;
    }
    public HBox getMazoCpu() {
        return idMazo2;
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
    public void leerNuevaPila(Cartas carta) {
        Image imagen = new Image(getClass().getResourceAsStream(carta.getRutaImagen()));
        pila.setImage(imagen);
    }

    public void primCarta(String cartaRuta) {
        Image imagen = new Image(getClass().getResourceAsStream(cartaRuta));
        pila.setImage(imagen);
    }

}
package com.example.uno.Model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// Las animaciones de abajo hay que importarlas en otra clase
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class MesaDeJuego {

    Baraja baraja = new Baraja();
    JugadorH jugadorh = new JugadorH();
    JugadorCPU jugadorCPU = new JugadorCPU();

    //Hay que comenzar a crear funciones con el scene builder abierto
    public void crearCartas() {
        baraja.crearCartas();
    }

    public void repartirCartas(int num) {
        //idMazo1.getChildren().clear(); copiar esto cuando se implemente en el controller
        for (int i = baraja.size() - 1; i > baraja.size() - num; i--){
            Cartas carta = baraja.getcarta(i);
            Button cartaButton = crearBotonCarta(carta, i);
        }
    } // RECUERDA CREAR UN CLASE QUE EVALÚE LAS JUGADAS PARA AHORRAR CÒDIGO Y NO PONERLO AQUÍ

    public Button crearBotonCarta(Cartas carta, int indice ) {
        Button cartaButton = new Button();
        ImageView cartaImageView = new ImageView( new Image(getClass().getResourceAsStream(carta.getRutaImagen()))); // Preguntar al profe
        cartaImageView.setFitWidth(70);
        cartaImageView.setFitHeight(88);
        cartaButton.setGraphic( cartaImageView );
        cartaButton.setUserData(carta);

        // Todo el codigo en lo que queda de funcion va en otra clase enfocada en animaciones
        // Efecto de elevación (opcional)
        cartaButton.setOnMouseEntered(event -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), cartaButton);
            transition.setToY(-10);
            transition.play();
        });

        cartaButton.setOnMouseExited(event -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), cartaButton);
            transition.setToY(0);
            transition.play();
        });

        // Establecer la posición horizontal (efecto acordeón)
        cartaButton.setTranslateX(-20 * indice); // Ajusta el valor según necesites

        return cartaButton;

    }
}

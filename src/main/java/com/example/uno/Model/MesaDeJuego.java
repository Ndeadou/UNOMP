package com.example.uno.Model;

import com.example.uno.Controller.HelloController;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// Las animaciones de abajo hay que importarlas en otra clase
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.layout.HBox;


public class MesaDeJuego {

    private HelloController controlador;

    private Cartas cartaPila;

    private int turno;

    private boolean clickeo;

    public MesaDeJuego(HelloController controlador) {
        this.controlador = controlador;
    }

    Baraja baraja = new Baraja();
    public Jugador jugadorH = new Jugador();
    public Jugador jugadorCPU = new Jugador();



    public void crearCartas() {
        baraja.crearCartas();
    }

    public void mezclarBaraja(){
        baraja.mezclarBaraja();
    }

    /**
     * Las siguientes 2 funciones fueron separadas ya que necesitaba usar leerMazo() de forma aislada dentro de la funcion
     * crear boton carta especificamente en setOnMouseClicked(linea 68) por esa misma razon es que se pasan los parametros HBox
     * y Jugador tantas veces de funcion en funcion.
     * Observa que ahora usamos leer mazo para mostrar en pantalla el mazo actualizado,sea de la CPU o del jugador
     * (Se emplea en repCartas y crearBotonCarta)
     *
     */
    public void repCartas(Jugador jugador, HBox mazoPlayer) {
            jugador.addCarta(baraja.getCarta(baraja.size() - 1));
            baraja.eliminarCarta(baraja.getCarta(baraja.size() - 1));
            leerMazo(jugador, mazoPlayer);

    }
    public void leerMazo(Jugador jugador, HBox mazoPlayer) {
        mazoPlayer.getChildren().clear();//Para entender esta linea silenciala y corre el juego dandole a baraja
        for(int i = 0; i < jugador.mazoSize(); i++) {
            Cartas carta = jugador.getCarta(i);
            Button cartaButton = crearBotonCarta(carta, i, jugador, mazoPlayer);//El "i" se usa para la animacion de acordeon
            mazoPlayer.getChildren().add(cartaButton);
        }
    }


    // RECUERDA CREAR UN CLASE QUE EVALÚE LAS JUGADAS PARA AHORRAR CÒDIGO Y NO PONERLO AQUÍ

    public Button crearBotonCarta(Cartas carta, int indice, Jugador jugador, HBox mazoPlayer) {
        Button cartaButton = new Button();
        ImageView cartaImageView = new ImageView( new Image(getClass().getResourceAsStream(carta.getRutaImagen()))); // Preguntar al profe
        cartaImageView.setFitWidth(70);
        cartaImageView.setFitHeight(88);
        cartaButton.setGraphic( cartaImageView );
        cartaButton.setUserData(carta);

        //esta funcion es la clave para el click de las cartas.(elimina del mazo y añade a la pila)
        cartaButton.setOnMouseClicked(event -> {
            controlador.manejarClicCarta(event);
            jugadorH.removeCarta(carta);
            leerMazo(jugador, mazoPlayer);
            click = true;
            cartaClick

            //cambiar la ubicacion del condicional del hilo
            if (jugadorH.mazoSize() == 1) { //Este condicional evalua si el tamaño de las cartas que tiene jugadorH es igual a 1
                // y si es así llama a la funcion que está en hello controller que contiene el hilo
                controlador.activarTemporizadorUNO(); // este método debe estar en HelloController
            }

            //comenzar a escribir la logica del juego.
            //crear una forma de almacenar la ultima carta de la pila y su data
            //crear una funcion que evalue si la carta clickeada se puede poner en la pila o no dependiendo de la data
            //esa funcion sera reutilizada tambien para la CPU con un ciclo que itere su mazo
            //
        });

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

        //Logica condicional para habilitar y deshabilitar el boton
        if (turno == 1){
            cartaButton.setDisable(false);
        } else if (turno == 2) {
            cartaButton.setDisable(true);
        } else {
            cartaButton.setDisable(false);
        }

        // Condicional para bloquear cartas
        if (evaluar(carta)){
            cartaButton.setDisable(false);
        } else {
            cartaButton.setDisable(true);
        }



        // Establecer la posición horizontal (efecto acordeón)
        //cartaButton.setTranslateX(-1 * indice); // Ajusta el valor según necesites

        return cartaButton;

    }
    public int barajaSiz(){
        return baraja.size();
    }


    public boolean evaluar(Cartas carta) {
        boolean pedo = false;
        //Aquí vamos a transformar la carta de la pila al tipo que sea necesario para compararla con la carta del parametro
        CartasN pilaN = new CartasN(0, null, 0, null);
        Comodines pilaC;
        if (cartaPila instanceof CartasN){
            pilaN = (CartasN) cartaPila;
        }

        //Aquí vamos a comparar la carta de la pila ya transformada con la pila del parametro

        if (carta instanceof CartasN) {
            CartasN cartaN = (CartasN) carta;
            if (carta.getColor() == cartaPila.getColor() || cartaN.getNumero() == pilaN.getNumero()) {
                pedo = true;
            } else {
                pedo = false;
            }
        }else if (carta.getTipodecarta() == "comodin") {
            if (carta.getColor() == cartaPila.getColor() || carta.getColor() == 0) {
                pedo = true;
            }else{
                pedo = false;}
        } return pedo;
    }











    //Logica

    public void jugar (HBox mazoPlayer) {

        boolean hayGanador = false;

        while(!hayGanador){
            hayGanador = verificarWinner(); //No ha sido creada hasta el momento
            if (turno == 1){
                turnoHumano(mazoPlayer);
            }else if(turno == 2){
                turnoCpu(); // No creada hasta el momento
            }else{
                System.out.println("No se puede jugar");
            }
        } //Aquí se coloca el ganador y la vuelta entera para finalizar
    }



    public void turnoHumano(HBox mazoPlayer) {

        if (mazoVacio()){ // No ha sido creada
            repCartas(jugadorH, mazoPlayer);
            turno =2;
        }
    }

    private boolean click = false;
    private Cartas cartaClick;

    public void cartaClicked(Cartas carta, HBox mazoPlayer) {
        if (carta instanceof Comodines){
            Comodines cartaC = (Comodines) carta;
            if (cartaC.getSimbolo() == "+2" ) {
                for (int i = 0; i <2; i++){
                    //repCartas(jugadorCPU, mazoPlayer); Hay que crear una funcion que reparta par al cpu porque dont exist
                    turno = 1;
                }
            } else if (cartaC.getSimbolo() == "+4"){
                for (int i = 0; i < 4; i++){
                    //repCartas(jugadorCPU, mazoPlayer); Hay que crear una funcion que reparta par al cpu porque dont exist
                    turno = 1;
                }
            } else if (cartaC.getSimbolo() == "block"){
                turno = 1;
            }
        } else {
            turno = 2;
        }
        cartaPila = carta;
    }
}


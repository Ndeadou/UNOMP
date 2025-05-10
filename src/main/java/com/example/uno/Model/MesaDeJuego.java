package com.example.uno.Model;

import com.example.uno.Controller.HelloController;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Random;


public class MesaDeJuego {

    private HelloController controlador;


    public MesaDeJuego(HelloController controlador) {
        this.controlador = controlador;
    }

    Baraja baraja = new Baraja();
    public Jugador jugadorH = new Jugador();
    public Jugador jugadorCPU = new Jugador();
    private int turno = 1;
    Cartas cartaPila;
    String ganador;
    boolean cartasRepartidas = false;

    //La siguiente funcion es la que le dará fin al juego.Verá si algun jugador ganó cambiando el bool "hayGanador"
    // ademas es la encargada de mostrar en la interfaz las felicitaciones etc..
    public boolean hayGanador(){
        boolean ganaron  = false;
        if(jugadorH.mazoSize() == 0){
            ganaron = true;
            ganador = "El humano";
        }else if(jugadorCPU.mazoSize() == 0){
            ganaron = true;
            ganador = "El cpu";
        }
        return ganaron;
        //si nada de lo anterior sucede se sigue jugando normal.
    }

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

    /**
     * LAS SIGUIENTES DOS FUNCIONES SON HERRAMIENTAS PARA EL PLAYER HUMANO
     *
     */
    public void repPlayer(HBox mazoPlayer) {
            jugadorH.addCarta(baraja.getCarta(baraja.size() - 1));
            baraja.eliminarCarta(baraja.getCarta(baraja.size() - 1));
            leerMazo(mazoPlayer);

    }
    public void leerMazo(HBox mazoPlayer) {
        mazoPlayer.getChildren().clear();//Para entender esta linea silenciala y corre el juego dandole a baraja
        for(int i = 0; i < jugadorH.mazoSize(); i++) {
            Cartas carta = jugadorH.getCarta(i);
            Button cartaButton = crearBotonCarta(carta, i, mazoPlayer);//El "i" se usa para la animacion de acordeon
            mazoPlayer.getChildren().add(cartaButton);
            if(turno != 1){
                cartaButton.setDisable(true);
            }
        }

        if(turno == 1 && mazoVacio(jugadorH) && cartasRepartidas){
            pausaJugador(mazoPlayer);
            /*
            turno = 2;
            repPlayer(mazoPlayer);
            System.out.println("Jugador COME");
            ejecutarTurnoCpu(controlador.getMazoCpu());*/
        }
    }

    public void pausaJugador(HBox mazoPlayer) {
        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
        pausa.setOnFinished(e -> {
            turno = 2;
            repPlayer(mazoPlayer);
            System.out.println("Jugador COME");
            ejecutarTurnoCpu(controlador.getMazoCpu());
        });
        pausa.play();
    }

    /**
     * LAS SIGUIENTES DOS FUNCIONES SON HERRAMIENTAS PARA LA CPU.
     */


    public void repCpu(HBox mazoCpu) {
        jugadorCPU.addCarta(baraja.getCarta(baraja.size() - 1));
        baraja.eliminarCarta(baraja.getCarta(baraja.size() - 1));
        leerMazoCpu(mazoCpu);

    }
    public void leerMazoCpu(HBox mazoCpu) {
        mazoCpu.getChildren().clear();
        for(int i = 0; i < jugadorCPU.mazoSize(); i++) {
            ImageView cartaImageView = new ImageView(new Image(getClass().getResourceAsStream("/Cartas/card_uno.png")));
            cartaImageView.setFitWidth(70);
            cartaImageView.setFitHeight(88);
            cartaImageView.setPreserveRatio(true); // Opcional: mantener la proporción
            mazoCpu.getChildren().add(cartaImageView);

        }
    }
    // RECUERDA CREAR UN CLASE QUE EVALÚE LAS JUGADAS PARA AHORRAR CÒDIGO Y NO PONERLO AQUÍ

    public Button crearBotonCarta(Cartas carta, int indice, HBox mazoPlayer) {
        Button cartaButton = new Button();
        ImageView cartaImageView = new ImageView( new Image(getClass().getResourceAsStream(carta.getRutaImagen()))); // Preguntar al profe
        cartaImageView.setFitWidth(70);
        cartaImageView.setFitHeight(88);
        cartaButton.setGraphic( cartaImageView );
        cartaButton.setUserData(carta);

        //Ahora esta mejor escrita la bloqueada de cartas.
        if(turno == 1 ){

            if (evaluar(carta)){
                cartaButton.setDisable(false);
            }else {
                cartaButton.setDisable(true);
            }
        }else{
            cartaButton.setDisable(true);

        }

        //esta funcion es la clave para el click de las cartas.(elimina del mazo y añade a la pila)
        cartaButton.setOnMouseClicked(event -> {
            controlador.manejarClicCarta(event);
            jugadorH.removeCarta(carta);
            cartaPila = carta;
            cartaClicked(carta,controlador.getMazoCpu());


            //Move o arregla la funcion directamente q esta basurota es la q no deja q funcione el code
            if (jugadorH.mazoSize() == 1) { //Este condicional evalua si el tamaño de las cartas que tiene jugadorH es igual a 1
                // y si es así llama a la funcion que está en hello controller que contiene el hilo
                controlador.activarTemporizadorUNO(); // este método debe estar en HelloController
            }

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



        // Establecer la posición horizontal (efecto acordeón)
        //cartaButton.setTranslateX(-1 * indice); // Ajusta el valor según necesites

        return cartaButton;

    }
    public int barajaSiz(){
        return baraja.size();
    }


    //LOGICA """"

    public void jugar(HBox mazoPlayer, HBox mazoCpu) {
        cartaPila = baraja.getCarta(0);
        baraja.eliminarCarta(cartaPila);
        controlador.primCarta(cartaPila.getRutaImagen());

        Comodines mas4 = new Comodines(0, "comodin", "+4", "/Cartas/4_wild_draw.png");
        jugadorH.addCarta(mas4);
        for(int i = 0; i < 5; i++) {
            repPlayer(mazoPlayer);
            repCpu(mazoCpu);
        }cartasRepartidas = true;
        leerMazo(mazoPlayer);

    }

    public boolean mazoVacio(Jugador cualquierJugador) {
        boolean mazoVacio = true;//cuando decimos mazoVacio "true" significa que no tiene opciones validas.
        for (int i = 0; i < cualquierJugador.mazoSize(); i++) {
            if(evaluar(cualquierJugador.getCarta(i))){
                mazoVacio = false;
                break;
            }
        }return mazoVacio;
    }

    public boolean evaluar(Cartas carta) {
        boolean pedo = false;

        /*Aquí vamos a transformar la carta de la pila al tipo que sea necesario para compararla con la carta del parametro
        Tambien vamos a comparar la carta de la pila ya transformada con la carta del parametro*/

        if (cartaPila instanceof CartasN) {
            //solo se crea pilaN para hacer la comparacion numerica.
            CartasN pilaN = (CartasN) cartaPila;
            if(carta instanceof CartasN){
                CartasN cartaN = (CartasN) carta;
                if(cartaN.getNumero() == pilaN.getNumero() || cartaN.getColor() == pilaN.getColor()){
                    pedo = true;
                }
            }else if(carta instanceof Comodines){
                if(carta.getColor() == cartaPila.getColor() || carta.getColor() == 0){
                    pedo = true;
                }
            }
        }else if(cartaPila instanceof Comodines){
            Comodines pilaC = (Comodines) cartaPila;
            if(carta instanceof Comodines){
                Comodines cartaC = (Comodines) carta;
                if (carta.getColor() == cartaPila.getColor()
                    || carta.getColor() == 0
                    || Objects.equals(cartaC.getSimbolo(), pilaC.getSimbolo()))
                {
                    pedo = true;
                }
            }else if(carta instanceof CartasN){
                if(carta.getColor() == cartaPila.getColor()){ pedo = true;}
            }
        } return pedo;
    }



    public void cartaClicked(Cartas carta, HBox mazoCpu) {
        if (carta instanceof Comodines){
            Comodines cartaC = (Comodines) carta;
            if (cartaC.getSimbolo() == "+2" ) {
                for (int i = 0; i <2; i++){
                    repCpu(mazoCpu);
                }
                System.out.println("Jugador tiro comodin +2");
            } else if (cartaC.getSimbolo() == "+4"){
                for (int i = 0; i < 4; i++){
                    repCpu(mazoCpu);
                }System.out.println("Jugador tiro comodin +4");
                carta.setColor(VentanaEmergente.mostrarDialogoSeleccionColor());
            } else if (cartaC.getSimbolo() == "block"){
                System.out.println("Jugador tiro comodin BLOQUEO");
                //No pasa nada, se supone que el turno sigue siendo el mismo
            }else if(cartaC.getSimbolo() == "camb"){
                carta.setColor(VentanaEmergente.mostrarDialogoSeleccionColor());
                turno = 2;
            }
            cartaPila = carta;
            controlador.leerNuevaPila(cartaPila);

            leerMazo(controlador.getMazoPlayer());
            if (!hayGanador() && turno == 2) {
                ejecutarTurnoCpu(controlador.getMazoCpu());
            }else if(!hayGanador() && turno == 1){
                leerMazoCpu(mazoCpu);
            }else if(hayGanador()){
                System.out.println("Hay Ganador y es : "+ganador);
                turno = 0;
                controlador.getMazoPlayer().getChildren().clear();
            }
        } else if (carta instanceof CartasN) {
            //Aqui definimos lo que pasa si la carta es la de seleccionar color, ya que la de retorno ha sido eliminada
            //Usa gemini para crear una interfaz funcional dentro de model para la ventana q selecciona el color
            cartaPila = carta;
            controlador.leerNuevaPila(cartaPila);
            //texto pa ver q pasa
            CartasN cartaN = (CartasN) carta;
            System.out.println("Jugador tiro NUMERO: "+cartaN.getNumero());

            turno = 2;
            leerMazo(controlador.getMazoPlayer());
            if (!hayGanador()) {
                ejecutarTurnoCpu(controlador.getMazoCpu());
            }else{
                System.out.println("Hay Ganador y es : "+ ganador);
                turno = 0;
            }
        }
    }
    Random random = new Random();
    public void cartaSelected(Cartas carta, HBox mazoPlayer) {
        if (carta instanceof Comodines){
            Comodines cartaC = (Comodines) carta;
            if (cartaC.getSimbolo() == "+2" ) {
                for (int i = 0; i <2; i++){
                    repPlayer(mazoPlayer);
                }
            } else if (cartaC.getSimbolo() == "+4"){
                for (int i = 0; i < 4; i++){
                    repPlayer(mazoPlayer);
                }carta.setColor(random.nextInt(4) + 1);
            } else if (cartaC.getSimbolo() == "block"){
                //No pasa nada, se supone que el turno sigue siendo el mismo
            } else if (cartaC.getSimbolo() == "camb") {
                turno = 1;
                carta.setColor(random.nextInt(4) + 1);
            }

            cartaPila = carta;
            controlador.leerNuevaPila(cartaPila);
            jugadorCPU.removeCarta(carta);
            leerMazoCpu(controlador.getMazoCpu());

            if(hayGanador()){
                System.out.println("Hay Ganador y es : "+ganador);
                turno = 0;
            }
            leerMazo(mazoPlayer);

            if(turno == 2){
                ejecutarTurnoCpu(controlador.getMazoCpu());
            }


        } else if(carta instanceof CartasN){
            cartaPila = carta;
            controlador.leerNuevaPila(cartaPila);
            jugadorCPU.removeCarta(carta);
            leerMazoCpu(controlador.getMazoCpu());
            //leerMazo(mazoPlayer);
            turno = 1;
            if(hayGanador()){
                System.out.println("Hay Ganador y es : "+ganador);
                turno = 0;
            }
            leerMazo(mazoPlayer);

        }
    }

    public void ejecutarTurnoCpu(HBox cpu) {
        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
        pausa.setOnFinished(e -> {
            turnoCpu(cpu);

        });
        pausa.play();
    }
    public void turnoCpu(HBox cpu) {
        //Aqui ejecutar el darTiempo()

        System.out.println("TURNO DE CPU");
        if(mazoVacio(jugadorCPU)){
            System.out.println("CPU comio \n");
            repCpu(cpu);
            //leerMazo(controlador.getMazoPlayer());
            turno = 1;
            leerMazo(controlador.getMazoPlayer());
            /*
            AQUI Y EN DONDE SE CAMBIE EL TURNO A 1, HAY QUE HAYAR LA MANERA DE DESBLOQUEAR LOS BOTONES NUEVAMENTE
            SIN LLAMAR A LA FUNCION CREAR BOTON, YA QUE AHI ESTA ESE CONDICIONAL(103 Line) Y POR ESO FALLA EL JUEGO
            ADEMAS POR ESO AVECES NO SE LEE LA ULTIMA CARTA DE LA PILA
            SOLUCIONAR QUE EL MAZO DEL JUGADOR SE ACTUALIZA MAL(Esto creo q se soluciona si solucionamos el error que mencione arriba)
            */

        }else{
            for (int i = 0; i < jugadorCPU.mazoSize(); i++) {
                Cartas carta = jugadorCPU.getCarta(i);
                if(evaluar(carta)){
                    System.out.println("CPU tiro la carta del tipo:"+carta.getTipodecarta());

                    cartaSelected(carta,controlador.getMazoPlayer());

                    //pa ver q monda tira
                    if(carta instanceof Comodines){
                        System.out.println("Es un :"+((Comodines) carta).getSimbolo()+"de color "+((Comodines) carta).getColor());
                    }else{
                        System.out.println("Es un :"+((CartasN) carta).getNumero()+"de color "+((CartasN) carta).getColor()+"\n");
                    }

                    break;//como se encontro la carta se detiene el bucle
                }
            }
        }
    }


}




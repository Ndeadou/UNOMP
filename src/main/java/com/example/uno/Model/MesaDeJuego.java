package com.example.uno.Model;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;

import java.util.Objects;
import java.util.Random;

/**
 * Esta clase es la lógica del UNO:
 * Crea y mezcla la baraja, reparte las cartas a humano y CPU.
 * Controla los turnos, los efectos de  las cartas,
 * çel temporizador de “UNO” (hilos y transiciones).
 * Actualiza los Pane, Button, ImageView
 * y Label inyectados para reflejar el estado actual del juego.
 * Gestiona las penalizaciones y detecta el ganador.
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class MesaDeJuego {

    private final ImageView vistaPila;
    private final Pane mazoPlayer;
    private final Pane mazoCpu;
    private final Button unoButton;
    private final Label lblColorActual;

    private Thread humanUnoThread;

    private PauseTransition cpuUnoTransition;

    /**
     * Parametros del constructor MesaDeJuego
     * @param mazoPlayer
     * @param mazoCpu
     * @param unoButton
     * @param vistaPila
     * @param lblColorActual
     *
     */
    public MesaDeJuego( Pane mazoPlayer, Pane mazoCpu, Button unoButton, ImageView vistaPila, Label lblColorActual) {

        this.lblColorActual = lblColorActual;
        this.mazoPlayer  = mazoPlayer;
        this.mazoCpu     = mazoCpu;
        this.unoButton = unoButton;
        this.vistaPila = vistaPila;

        this.unoButton.setOnAction(evt -> {
            if (humanRaceStarted && humanUnoThread != null && humanUnoThread.isAlive()) {
                humanUnoPressed = true;
                humanUnoThread.interrupt();
                return;
            }
            if (cpuRaceStarted && cpuUnoTransition != null) {
                cpuUnoPressed = true;
                cpuUnoTransition.stop();
                resolveUno("C");
                cpuRaceStarted = false;
                unoButton.setDisable(true);
            }
        });
    }

    Baraja baraja = new Baraja();
    public Jugador jugadorH = new Jugador();
    public Jugador jugadorCPU = new Jugador();
    private int turno = 1;
    Cartas cartaPila;
    String ganador;
    boolean cartasRepartidas = false;

    private volatile boolean humanUnoPressed = false;
    private volatile boolean cpuUnoPressed   = false;

    private Random random = new Random();

    private boolean humanRaceStarted = false;
    private boolean cpuRaceStarted   = false;

    /**
     * Este metodo comprueba si alguno de los jugadores ganó la partida
     * @see Jugador
     * @return booleano
     */
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
    }

    /**
     * Este metodo reparte una carta al jugador humano
     * eliminando de la baraja la carta repartida
     * @see #leerMazo
     */
    public void repPlayer() {
            jugadorH.addCarta(baraja.getCarta(baraja.size() - 1));
            baraja.eliminarCarta(baraja.getCarta(baraja.size() - 1));
            leerMazo();
    }

    /**
     * Este metodo actualiza el mazo (el nodo Pane) del jugador humano
     * Se usa varias veces en la clase
     * @see #crearBotonCarta
     */
    public void leerMazo() {
        Platform.runLater(() -> {
            mazoPlayer.getChildren().clear();//Para entender esta linea silenciala y corre el juego dandole a baraja
            int totalCartas = jugadorH.mazoSize();
            double anchoDisponible = mazoPlayer.getWidth();
            double anchoCarta = 75;
            double espacioEntreCartas = anchoCarta;

            if (totalCartas > 1) {
                double maxEspacio = (anchoDisponible - anchoCarta) / (totalCartas - 1);
                espacioEntreCartas = Math.min(anchoCarta, maxEspacio);
            }
            for(int i = 0; i < jugadorH.mazoSize(); i++) {

                Cartas carta = jugadorH.getCarta(i);
                Button cartaButton = crearBotonCarta(carta);//El "i" se usa para la animacion de acordeon
                cartaButton.setLayoutX(i * espacioEntreCartas);
                cartaButton.setLayoutY(0); // Opcional si quieres alinearlo al borde superior
                mazoPlayer.getChildren().add(cartaButton);
                if(turno != 1){
                    cartaButton.setDisable(true);
                }
            }

        });


        if(turno == 1 && mazoVacio(jugadorH) && cartasRepartidas){
            pausaJugador();
        }
    }

    /**
     * Este metodo hace una pausa para dar naturalidad al juego,
     * penaliza al jugador humano con una carta y ejecuta el turno de la cpu
     * @see #repPlayer
     * @see #ejecutarTurnoCpu
     */
    public void pausaJugador() {
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            turno = 2;
            repPlayer();
            System.out.println("Jugador COME");
            ejecutarTurnoCpu();
        });
        pausa.play();
    }

    /**
     * Este metodo reparte una carta a la cpu
     * eliminando de la baraja la carta repartida
     * @see #leerMazoCpu()
     * @see Baraja
     * @see Jugador
     */
    public void repCpu() {
        jugadorCPU.addCarta(baraja.getCarta(baraja.size() - 1));
        baraja.eliminarCarta(baraja.getCarta(baraja.size() - 1));
        leerMazoCpu();

    }

    /**
     * Este metodo actualiza el mazo (el nodo Pane) de la cpu
     * Se usa varias veces en la clase, además implementa una
     * lógica para el desborde de cartas
     * @see Jugador
     * */
    public void leerMazoCpu() {
        mazoCpu.getChildren().clear();
        Platform.runLater(() -> {
            int totalCartas = jugadorCPU.mazoSize();
            double anchoDisponible = mazoCpu.getWidth();
            double anchoCarta = 74;
            double espacioEntreCartas = anchoCarta;
            if (totalCartas > 1) {
                double maxEspacio = (anchoDisponible - anchoCarta) / (totalCartas - 1);
                espacioEntreCartas = Math.min(anchoCarta, maxEspacio);
            }
            for(int i = 0; i < jugadorCPU.mazoSize(); i++) {
                ImageView cartaImageView = new ImageView(new Image(getClass().getResourceAsStream("/Cartas/card_uno.png")));
                cartaImageView.setFitWidth(68);
                cartaImageView.setFitHeight(88);
                cartaImageView.setLayoutX(i * espacioEntreCartas);
                cartaImageView.setLayoutY(0);
                mazoCpu.getChildren().add(cartaImageView);
            }});

        if (cartasRepartidas && jugadorCPU.mazoSize() == 1) {
            startUnoRace("C");
        }
    }

    /**
     * Este método crea el boton de la carta iterada en leer mazo
     * @param carta
     * @return cartaButton
     */
    public Button crearBotonCarta(Cartas carta) {
        Button cartaButton = new Button();
        cartaButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ImageView cartaImageView = new ImageView( new Image(getClass().getResourceAsStream(carta.getRutaImagen()))); // Preguntar al profe
        cartaImageView.setFitWidth(70);
        cartaImageView.setFitHeight(88);
        cartaButton.setGraphic( cartaImageView );
        cartaButton.setUserData(carta);
        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-0.75);
        grayscale.setBrightness(-0.76);
        grayscale.setContrast(0.25);

        //lógica de bloqueo de botones
        if(turno == 1 ){

            if (evaluar(carta)){
                cartaButton.setDisable(false);
                cartaImageView.setEffect(null);
            }else {
                cartaButton.setDisable(true);
                cartaButton.setOpacity(1.0);
                cartaImageView.setEffect(grayscale);
            }
        }else{
            cartaButton.setDisable(true);
            cartaButton.setOpacity(1.0);
            cartaImageView.setEffect(grayscale);
        }
        cartaButton.setOnMouseClicked(event -> {
            manejarClicCarta(carta);
            cartaPila = carta;
            cartaClicked(carta);
        });

        // Efecto de elevación
        cartaButton.setOnMouseEntered(event -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), cartaButton);
            transition.setToY(-25);
            transition.play();
        });

        cartaButton.setOnMouseExited(event -> {
            TranslateTransition transition = new TranslateTransition(Duration.millis(200), cartaButton);
            transition.setToY(0);
            transition.play();
        });

        return cartaButton;
    }

    //LOGICA """"

    /**
     * Este método se encarga de iniciar el juego, creando cartas, mezclándolas
     * y repartiendolas, posteriormente se le da el turno al jugador humano para empezar
     * @see Baraja
     */
    public void jugar() {
        baraja.crearCartas();
        baraja.mezclarBaraja();
        cartaPila = baraja.getCarta(0);
        baraja.eliminarCarta(cartaPila);
        manejarClicCarta(cartaPila);
        //Comodines +4
        Comodines mas4 = new Comodines(0, "comodin", "+4", "/Cartas/4_wild_draw.png");
        baraja.añadirCarta(mas4);

        //Comodines cambiodecolor
        Comodines cambioColor = new Comodines(0, "comodin", "camb", "/Cartas/wild.png");
        baraja.añadirCarta(cambioColor);
        baraja.mezclarBaraja();

        for(int i = 0; i < 5; i++) {
            repPlayer();
            repCpu();
        }cartasRepartidas = true;
        leerMazo();
    }

    /**
     * Este metodo verifica si un mazo no tiene opciones válidas
     * @param cualquierJugador
     * @return mazoVacio
     */
    public boolean mazoVacio(Jugador cualquierJugador) {
        boolean mazoVacio = true;
        for (int i = 0; i < cualquierJugador.mazoSize(); i++) {
            if (evaluar(cualquierJugador.getCarta(i))) {
                mazoVacio = false;
                break;
            }
        }
        return mazoVacio;
    }

    /**
     * Este metodo verifica si una carta es jugable
     * de acuerdo a la información de la pila
     * @param carta
     * @return booleano
     * @see Cartas
     * @see CartasN
     * @see Comodines
     */
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

    /**
     * Este método maneja la lógica de los comodines y la
     * tirada de la carta clickeada por el jugador humano
     * Se usan metodos propios de la clase como: resetearColorActual,
     * mostrarColorActual, ejecutarTurnoCpu, startUnoRace, HayGanador,
     * leerNuevaPila
     * @param carta
     * @see Comodines
     * @see Jugador
     * @see Cartas
     * @see VentanaEmergente
     */
    public void cartaClicked(Cartas carta) {
        jugadorH.removeCarta(carta);
        resetearColorActual();

        if (carta instanceof Comodines) {
            Comodines cartaC = (Comodines) carta;
            int colorElegido = 0;
            if (cartaC.getSimbolo() == "+2") {
                for (int i = 0; i < 2; i++) {
                    repCpu();
                }
                System.out.println("Jugador tiro comodin +2");
            } else if (cartaC.getSimbolo() == "+4") {
                for (int i = 0; i < 4; i++) {
                    repCpu();
                }
                System.out.println("Jugador tiro comodin +4");
                colorElegido = VentanaEmergente.mostrarDialogoSeleccionColor();
                carta.setColor(colorElegido);
                mostrarColorActual(colorElegido); //
            } else if (cartaC.getSimbolo() == "block") {
                System.out.println("Jugador tiro comodin BLOQUEO");
            } else if (cartaC.getSimbolo() == "camb") {
                colorElegido = VentanaEmergente.mostrarDialogoSeleccionColor();
                carta.setColor(colorElegido);
                mostrarColorActual(colorElegido);
                turno = 2;
            }
            cartaPila = carta;
            leerNuevaPila(cartaPila);

            if (hayGanador()) {
                System.out.println("Hay Ganador y es : " + ganador);
                turno = 0;
                mazoPlayer.getChildren().clear();
            } else {
                if (cartasRepartidas && jugadorH.mazoSize() == 1) {
                    startUnoRace("H");
                }
                leerMazo();
                if (turno == 2) {
                    ejecutarTurnoCpu();
                }
            }
        } else if (carta instanceof CartasN) {
            //Aqui definimos lo que pasa si la carta es la de seleccionar color, ya que la de retorno ha sido eliminada
            cartaPila = carta;
            leerNuevaPila(cartaPila);
            CartasN cartaN = (CartasN) carta;
            System.out.println("Jugador tiro NUMERO: "+cartaN.getNumero());

            turno = 2;
            if (cartasRepartidas && jugadorH.mazoSize() == 1) {
                startUnoRace("H");
            }
            leerMazo();
            if (!hayGanador()) {
                ejecutarTurnoCpu();
            }else{
                System.out.println("Hay Ganador y es : "+ ganador);
                turno = 0;
            }
        }
    }

    /**
     * Este método maneja la lógica de los comodines y la
     * tirada de la carta clickeada por la cpu
     * Se usan metodos propios de la clase como: resetearColorActual,
     * mostrarColorActual, ejecutarTurnoCpu, HayGanador,
     * leerNuevaPila, leerMazoCpu, leerMazo
     * @param carta
     * @see Cartas
     * @see Comodines
     */
    public void cartaSelected(Cartas carta) {
        resetearColorActual();
        if (carta instanceof Comodines){
            int colorElegido = 0;
            Comodines cartaC = (Comodines) carta;
            if (cartaC.getSimbolo() == "+2" ) {
                for (int i = 0; i <2; i++){
                    repPlayer();
                }
            } else if (cartaC.getSimbolo() == "+4"){
                for (int i = 0; i < 4; i++){
                    repPlayer();
                }colorElegido = random.nextInt(4) + 1;
                mostrarColorActual(colorElegido);
                carta.setColor(colorElegido);
            } else if (cartaC.getSimbolo() == "block"){
                //No pasa nada, se supone que el turno sigue siendo el mismo
            } else if (cartaC.getSimbolo() == "camb") {
                turno = 1;
                colorElegido = random.nextInt(4) + 1;
                mostrarColorActual(colorElegido);
                carta.setColor(colorElegido);
            }

            cartaPila = carta;
            leerNuevaPila(cartaPila);
            jugadorCPU.removeCarta(carta);
            leerMazoCpu();

            if(hayGanador()){
                System.out.println("Hay Ganador y es : "+ganador);
                turno = 0;
            }
            leerMazo();

            if(turno == 2){
                ejecutarTurnoCpu();
            }
        } else if(carta instanceof CartasN){
            cartaPila = carta;
            leerNuevaPila(cartaPila);
            jugadorCPU.removeCarta(carta);
            leerMazoCpu();
            turno = 1;
            if(hayGanador()){
                System.out.println("Hay Ganador y es : "+ganador);
                turno = 0;
            }
            leerMazo();
        }
    }

    /**
     * Este método es similiar a pausaJugador, sirve para dar naturalidad al juego
     * y ejecuta el turno de la cpu
     * Se usan metodos propios de la clase como turnoCpu
     */
    public void ejecutarTurnoCpu() {
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            turnoCpu();
        });
        pausa.play();
    }

    /**
     * Este metodo maneja la lógica del turno de la cpu
     * Se usan metodos propios de la clase como:
     * leerMazo, repCpu, cartaSelected
     * @see Comodines
     * @see CartasN
     * @see Jugador
     * @see Cartas
     */
    public void turnoCpu() {
        System.out.println("TURNO DE CPU");
        if(mazoVacio(jugadorCPU)){
            System.out.println("CPU comio \n");
            repCpu();
            turno = 1;
            leerMazo();
        }else{
            for (int i = 0; i < jugadorCPU.mazoSize(); i++) {
                Cartas carta = jugadorCPU.getCarta(i);
                if(evaluar(carta)){
                    System.out.println("CPU tiro la carta del tipo:"+carta.getTipodecarta());
                    cartaSelected(carta);
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

    /**
     * Este método inicia la “carrera” de UNO tanto para el jugador humano como para la CPU,
     * controlando quién debe declarar UNO y en qué tiempo.
     * Usa metodos propios de la clase como: hayGanador, resolveUno
     * @param side
     */
    private void startUnoRace(String side) {
        // 3.1) Prevenir duplicados
        if ("H".equals(side)) {
            if (humanRaceStarted) return;
            humanRaceStarted = true;
        } else {
            if (cpuRaceStarted) return;
            cpuRaceStarted = true;
        }
        // 3.2) Resetear flags
        humanUnoPressed = false;
        cpuUnoPressed   = false;
        // 3.3) Mostrar botón UNO
        unoButton.setDisable(false);
        // 3.4) Arrancar el retraso
        if ("H".equals(side)) {
            humanUnoThread = new Thread(() -> {
                try {
                    long delay = 2000 + random.nextInt(2000);
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    // ¡lo despertó el usuario pulsando UNO!
                }
                // Aquí sabemos si pulso UNO o no
                Platform.runLater(() -> {
                    if (!hayGanador()) {
                        if (humanUnoPressed) {
                            // —— ÉXITO: declaró UNO a tiempo ——
                            System.out.println("UNO declarado a tiempo. Sin penalización.");
                        } else {
                            // —— FALLO: no declaró UNO a tiempo ——
                            resolveUno("H");      // esto aplica la penalización
                        }
                        humanRaceStarted = false;
                        unoButton.setDisable(true);
                    }
                });
            });
            humanUnoThread.setDaemon(true);
            humanUnoThread.start();
        } else {
            // Calcular un retraso aleatorio 1–4s
            double delaySec = 1 + random.nextDouble() * 3.0;
            // Guardamos la transición para poder cancelarla
            cpuUnoTransition = new PauseTransition(Duration.seconds(delaySec));
            cpuUnoTransition.setOnFinished(ev -> {
                if (!hayGanador()) {
                    resolveUno("C");        // la CPU cantó UNO a tiempo
                    cpuRaceStarted = false;
                    unoButton.setDisable(true);
                }
            });
            cpuUnoTransition.play();
        }
    }

    /**
     *  Este metodo es llamado por HelloController cuando el humano pulsa el botón:
     *  marca la bandera correspondiente.
     * @see Jugador
     */
    public void onHumanPressUno() {
        if (jugadorH.mazoSize() == 1) {
            humanUnoPressed = true;
            System.out.println("Jugador cantó UNO (propio).");
        } else if (jugadorCPU.mazoSize() == 1) {
            cpuUnoPressed = true;
            System.out.println("Jugador cantó UNO por la CPU.");
        }
    }

    /**
     * Este método resuelve la carrera cuando expira el temporizador:
     * comprueba quién pulsó y aplica penalización o no.
     * Usa métodos propios de la clase cómo: repPlayer, repCpu
     * @param side
     */
    private void resolveUno(String side) {
        if ("H".equals(side)) {
            if (!humanUnoPressed) {
                System.out.println("Jugador NO cantó UNO a tiempo, penalización.");
                repPlayer();
            } else {
                System.out.println("Jugador cantó UNO a tiempo.");
            }
        } else { // CPU
            if (!cpuUnoPressed) {
                System.out.println("CPU canta UNO automáticamente.");
                cpuUnoPressed = true;
            } else {
                System.out.println("CPU NO cantó UNO a tiempo, penalización.");
                repCpu();
            }
        }
        unoButton.setDisable(true);
    }

    /**
     * Este método añade la imagen de la carta clickeada al ImageView correspondiente
     * a la pila
     * @param carta
     * @see Cartas
     */
    public void manejarClicCarta(Cartas carta) {
        if (carta != null) {
            String rutaImagen = carta.getRutaImagen();
            Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));
            vistaPila.setImage(imagen);
        } else {
            System.out.println("No se encontró información de la carta en el botón.");
        }
    }

    /**
     * Este método se encarga de limpiar la etiqueta que muestra el color
     * actualmente seleccionado, dejándola vacía.
     */
    public void resetearColorActual(){
        lblColorActual.setText("");
    }

    /**
     * Este método se encarga de mostrar en pantalla el nombre del color que el jugador
     * (o la CPU) ha elegido, traduciendo un código numérico a texto legible y actualizando el Label
     * @param color
     */
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

    /**
     * Este método se encarga de actualizar la imagen de la pila de descarte
     * con la carta que se le pase
     * @param carta
     * @see Cartas
     */
    public void leerNuevaPila(Cartas carta) {
        Image imagen = new Image(getClass().getResourceAsStream(carta.getRutaImagen()));
        vistaPila.setImage(imagen);
    }
}




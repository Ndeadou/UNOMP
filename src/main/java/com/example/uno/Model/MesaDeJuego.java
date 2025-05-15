package com.example.uno.Model;

import com.example.uno.Controller.HelloController;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;

import java.util.Objects;
import java.util.Random;


public class MesaDeJuego {

    private ImageView vistaPila;
    private Pane mazoPlayer;
    private Pane mazoCpu;
    private Button unoButton;
    private Label lblColorActual;



    public MesaDeJuego( Pane mazoPlayer, Pane mazoCpu, Button unoButton, ImageView vistaPila, Label lblColorActual) {

        this.lblColorActual = lblColorActual;
        this.mazoPlayer  = mazoPlayer;
        this.mazoCpu     = mazoCpu;
        this.unoButton = unoButton;
        this.vistaPila = vistaPila;
    }

    Baraja baraja = new Baraja();
    public Jugador jugadorH = new Jugador();
    public Jugador jugadorCPU = new Jugador();
    private int turno = 1;
    Cartas cartaPila;
    String ganador;
    boolean cartasRepartidas = false;

    //btn
    /**
     * Flags que indican si cada lado pulsó “UNO”.
     * volatile: asegura que los cambios se vean entre hilos.
     */
    private volatile boolean humanUnoPressed = false;
    private volatile boolean cpuUnoPressed   = false;
    //btn

    private Random random = new Random();
    /* EJEMPLO: Si creáramos un Random local en cada llamada,
    podríamos terminar con patrones menos aleatorios y más objetos en memoria.
    Evitar carreras duplicadas*/

    //btn
    /**
     * Evita lanzar dos carreras UNO simultáneas para el mismo lado.
     */
    private boolean humanRaceStarted = false;
    private boolean cpuRaceStarted   = false;
    //btn

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
    public void repPlayer() {
            jugadorH.addCarta(baraja.getCarta(baraja.size() - 1));
            baraja.eliminarCarta(baraja.getCarta(baraja.size() - 1));
            leerMazo();

    }
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
                Button cartaButton = crearBotonCarta(carta, i);//El "i" se usa para la animacion de acordeon
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
            /*
            turno = 2;
            repPlayer(mazoPlayer);
            System.out.println("Jugador COME");
            ejecutarTurnoCpu(controlador.getMazoCpu());*/
        }
        //btn
        /**
         * 1) Detecta si el humano quedó con 1 carta y
         *    lanza la carrera UNO para el humano.
         */
        /*if (cartasRepartidas && jugadorH.mazoSize() == 1) {
            startUnoRace("H");
        }*/
        //btn
    }

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
     * LAS SIGUIENTES DOS FUNCIONES SON HERRAMIENTAS PARA LA CPU.
     */


    public void repCpu() {
        jugadorCPU.addCarta(baraja.getCarta(baraja.size() - 1));
        baraja.eliminarCarta(baraja.getCarta(baraja.size() - 1));
        leerMazoCpu();

    }
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
                //cartaImageView.setPreserveRatio(true); // Opcional: mantener la proporción
                cartaImageView.setLayoutX(i * espacioEntreCartas);
                cartaImageView.setLayoutY(0); // opcional
                mazoCpu.getChildren().add(cartaImageView);
            }});



        //btn
        /**
         * 2) Igual para la CPU: cuando baje a 1 carta,
         *    lanza la carrera UNO para la CPU.
         */
        if (cartasRepartidas && jugadorCPU.mazoSize() == 1) {
            startUnoRace("C");
        }
        //btn
    }
    // RECUERDA CREAR UN CLASE QUE EVALÚE LAS JUGADAS PARA AHORRAR CÒDIGO Y NO PONERLO AQUÍ



    public Button crearBotonCarta(Cartas carta, int indice) {
        Button cartaButton = new Button();
        cartaButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        ImageView cartaImageView = new ImageView( new Image(getClass().getResourceAsStream(carta.getRutaImagen()))); // Preguntar al profe
        cartaImageView.setFitWidth(70);
        cartaImageView.setFitHeight(88);
        cartaButton.setGraphic( cartaImageView );
        cartaButton.setUserData(carta);
        ColorAdjust grayscale = new ColorAdjust();
        grayscale.setSaturation(-0.75);     // escala de grises
        grayscale.setBrightness(-0.76);   // más oscuro
        grayscale.setContrast(0.25);      // más definido


        //Ahora esta mejor escrita la bloqueada de cartas.
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


        //esta funcion es la clave para el click de las cartas.(elimina del mazo y añade a la pila)
        cartaButton.setOnMouseClicked(event -> {
            manejarClicCarta(event);
            //jugadorH.removeCarta(carta); // esto se mueve a cartaclicked, no afecta el funcionamiento
            //que remueve cartas, pero ayuda a que no se presenten bugs cuando el jugador utiliza el bloqueo
            cartaPila = carta;
            cartaClicked(carta);


        });


        // Todo el codigo en lo que queda de funcion va en otra clase enfocada en animaciones
        // Efecto de elevación (opcional)
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



        // Establecer la posición horizontal (efecto acordeón)
        //cartaButton.setTranslateX(-1 * indice); // Ajusta el valor según necesites

        return cartaButton;

    }
    public int barajaSiz(){
        return baraja.size();
    }


    //LOGICA """"

    public void jugar() {
        baraja.crearCartas();
        baraja.mezclarBaraja();
        cartaPila = baraja.getCarta(0);
        baraja.eliminarCarta(cartaPila);
        primCarta(cartaPila.getRutaImagen());
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

    public boolean mazoVacio(Jugador cualquierJugador) {
        boolean mazoVacio = true;//cuando decimos mazoVacio "true" significa que no tiene opciones validas.
        for (int i = 0; i < cualquierJugador.mazoSize(); i++) {
            if (evaluar(cualquierJugador.getCarta(i))) {
                mazoVacio = false;
                break;
            }
        }
        return mazoVacio;
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



    public void cartaClicked(Cartas carta) {
        //btn
        jugadorH.removeCarta(carta);
        //btn
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
                //No pasa nada, se supone que el turno sigue siendo el mismo
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
            //Usa gemini para crear una interfaz funcional dentro de model para la ventana q selecciona el color
            cartaPila = carta;
            leerNuevaPila(cartaPila);
            //texto pa ver q pasa
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
    //Random random = new Random(); se usa el private de arriba
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
            //leerMazo(mazoPlayer);
            turno = 1;
            if(hayGanador()){
                System.out.println("Hay Ganador y es : "+ganador);
                turno = 0;
            }
            leerMazo();

        }
    }

    public void ejecutarTurnoCpu() {
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> {
            turnoCpu();

        });
        pausa.play();
    }
    public void turnoCpu() {
        //Aqui ejecutar el darTiempo()

        System.out.println("TURNO DE CPU");
        if(mazoVacio(jugadorCPU)){
            System.out.println("CPU comio \n");
            repCpu();
            //leerMazo(controlador.getMazoPlayer());
            turno = 1;
            leerMazo();
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

                    cartaSelected(carta);

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

    //hilo
    /**
     * 3) Inicia la “carrera UNO”:
     *    - side = "H" para humano, "C" para CPU.
     *    - Evita duplicados, resetea flags, habilita botón.
     *    - Para humano: hilo Java 2–4 s.
     *    - Para CPU: PauseTransition 0–4 s.
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
        // EJEMPLO: Sin esta comprobación,
        // múltiples llamadas a startUnoRace("H") crearían hilos en cascada.

        // 3.2) Limpiar quién pulsó
        humanUnoPressed = false;
        cpuUnoPressed   = false;
        // EJEMPLO: Si no resetearas estos flags,
        // en carreras posteriores seguirías teniendo marcados valores previos.

        // 3.3) Mostrar botón UNO en la UI

        unoButton.setDisable(false);
        // EJEMPLO: Si no habilitaras el botón aquí,
        // el humano no tendría forma de pulsarlo para declarar UNO.

        // 3.4) Arrancar el retraso
        if ("H".equals(side)) {
            // HUMANO: hilo clásico que duerme 2–4 segundos.
            new Thread(() -> {
                try {
                    long delay = 2000 + random.nextInt(2000);
                    Thread.sleep(delay);
                    Platform.runLater(() -> {
                        if (!hayGanador()) {
                            resolveUno("H");
                            humanRaceStarted = false;
                        }

                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            // EJEMPLO: Si omitiréramos Thread.sleep(),
            // resolveUno("H") se ejecutaría de inmediato,
            // penalizando al humano antes de poder pulsar.
        } else {
            // CPU: PauseTransition de JavaFX 0–4 segundos.
            double delaySec = 1 + random.nextDouble() * 3.0;
            PauseTransition cpuDelay = new PauseTransition(Duration.seconds(delaySec));
            cpuDelay.setOnFinished(ev -> {
                if(!hayGanador()){
                    resolveUno("C");
                    cpuRaceStarted = false;
                }

            });
            cpuDelay.play();
            // EJEMPLO: Sin usar PauseTransition (o similar),
            // la CPU declararía UNO instantáneamente al llegar a 1 carta.
        }
    }

    /**
     * 4) Llamado por HelloController cuando el humano pulsa el botón:
     *    marca el flag correspondiente.
     */
    public void onHumanPressUno() {
        if (jugadorH.mazoSize() == 1) {
            humanUnoPressed = true;
            System.out.println("Jugador cantó UNO (propio).");
        } else if (jugadorCPU.mazoSize() == 1) {
            cpuUnoPressed = true;
            System.out.println("Jugador cantó UNO por la CPU.");
        }
        // EJEMPLO: Si no diferenciáramos ambos casos,
        // el humano no podría penalizar a la CPU y viceversa.
    }

    /**
     * 5) Resuelve la carrera cuando expira el temporizador:
     *    comprueba quién pulsó y aplica penalización o no.
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
        // EJEMPLO: Sin esta línea, el botón UNO seguiría habilitado
        // y permitiría clics fuera de contexto para la siguiente mano.
        unoButton.setDisable(true);
    }
    //hilo

    public void manejarClicCarta(MouseEvent event) {
        Button cartaButtonClickeada = (Button) event.getSource();
        Cartas cartaAsociada = (Cartas) cartaButtonClickeada.getUserData();
        if (cartaAsociada != null) {
            String rutaImagen = cartaAsociada.getRutaImagen();
            Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));
            vistaPila.setImage(imagen);
        } else {
            System.out.println("No se encontró información de la carta en el botón.");
        }
    }

    public void primCarta(String cartaRuta) {
        Image imagen = new Image(getClass().getResourceAsStream(cartaRuta));
        vistaPila.setImage(imagen);
    }

    public void resetearColorActual(){
        lblColorActual.setText("");
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

    public void leerNuevaPila(Cartas carta) {
        Image imagen = new Image(getClass().getResourceAsStream(carta.getRutaImagen()));
        vistaPila.setImage(imagen);
    }
}




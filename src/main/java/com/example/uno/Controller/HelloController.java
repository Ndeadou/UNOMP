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
    private Button idbotonUno;

    @FXML
    public void manejarClicBotonUNO() { //este es el método para detectar el clic en el botón UNO
        presionoUNO = true;
    }



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

    private boolean presionoUNO = false; //este es la variable buleana que dice que por el momento es mentira que el humano ha presionado el boton

    public void activarTemporizadorUNO() {
        presionoUNO = false; // Esto reinicia el estado es decir que se asume que el jugador no ha presionado el botón UNO.
        // El por qué de esto es sencillo (Sí no se reinicia puede quedar como si fuera true de una jugada anterior.)

        new Thread(() -> { //Aquí se crea el hilo a por qué se crea? porque si se mandara el sleep afuera mandaria a la verga el boton
            try {
                Thread.sleep(3000); // Aquí esta lo que hace es mandar a mimir un rato el hilito por 3000 milisengundos que son 3 sec wei
                if (!presionoUNO) { // Este condicional lo que hace es verificar si despues de los 3 segundos o el humano no presionó el boton
                    //se come una carta
                    // Aquí se penaliza comiendo una carta
                    Platform.runLater(() -> { // a ver esto se usa porque estamos en un hilo secundario y Javafx solo permite modificar la interfaz
                        // (como repartir cartas) desde el hilo principal
                        // entonces Platform.runLater() es como decir: “Ejecuta esto dentro de la interfaz gráfica, cuando sea seguro.”
                        mesa.repPlayer( idMazo1);// Aquí se aplica la penalización: el jugador humano roba una carta como castigo por no decir "UNO".
                        System.out.println("¡Penalizado por no decir UNO!"); //Esto no mas es pa que aparezca en la consolita abajo hay un else que pues es otro msj
                    });
                } else {
                    System.out.println("UNO presionado a tiempo");
                }
            } catch (InterruptedException e) { //Esto parece raro pero en realidad solamente imprime en la consola
                // lanzo el error si algo falla al ejecutar el Thread.sleep() (o sea, si el hilo es interrumpido antes de que terminen los 3 segundos).

                e.printStackTrace();
            }
        })
        .start(); //Esto inicia el pinche hilo siempre va afuera del hilo no dentro
    }



}
package com.example.uno.Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Esta clase reprsenta el mazo de UNO.
 * Crea todas las cartas (CartasN y Comodines).
 * Permite mezclar, robar y eliminar cartas de la baraja.
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class Baraja {

    private ArrayList<Cartas> cartas = new ArrayList<>();

    /**
     * Este método inserta la carta recibida al final de la lista interna.
     * Se usa, por ejemplo, cuando reparte o penaliza añadiendo nuevas cartas a la mano de un jugador
     * @param carta
     */
    public void añadirCarta(Cartas carta) {cartas.add(carta);}

    /**
     * Este método mezcla aleatoriamente todos los elementos de la lista usando Collections.shuffle.
     * Garantiza que el orden del mazo sea impredecible antes de repartir
     */
    public void mezclarBaraja() {Collections.shuffle(this.cartas);}

    /**
     * Este método busca la primera aparición de esa carta en la lista y la quita.
     * Sirve para “robar” (la baraja pierde una carta) o para descartar del mazo
     * @param carta
     */
    public void eliminarCarta(Cartas carta) {this.cartas.remove(carta);}

    /**
     * Este método devuelve cuántas cartas hay actualmente en la baraja (this.cartas.size()).
     * para saber si la baraja se ha agotado o para calcular índices
     * @return size
     */
    public int size() {return this.cartas.size();}

    /**
     * Este método retorna la carta que está en la posición i de la lista.
     * permitiendo acceder a una carta concreta por índice
     * @param i
     * @return get
     */
    public Cartas getCarta(int i){return this.cartas.get(i);}

    /**
     * Este método nicializa completamente la baraja con todas las cartas de UNO:
     * Cartas numéricas (0–9) de cuatro colores (azul, verde, rojo, amarillo).
     * Comodines “+2” en cada color.
     * Comodines de bloqueo (“skip”) en cada color.
     */
    public void crearCartas() {

        //Debemos instanciar los numeros "0 y 2" manualmente
        for (int i = 0; i < 10; i++ ) {
            for (int j = 1; j <= 4; j++ ) {
                if(j==1){
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_blue.png");
                    this.cartas.add(carta);
                }else if (j==2){
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_green.png");
                    this.cartas.add(carta);
                }else if (j==3) {
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_red.png");
                    this.cartas.add(carta);
                }else {
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_yellow.png");
                    this.cartas.add(carta);
                }
            }
        }

        //Comodines +2
        Comodines masDosAzul = new Comodines(1, "comodin", "+2", "/Cartas/2_wild_draw_blue.png");
        this.cartas.add(masDosAzul);
        Comodines masDosVerde = new Comodines(2, "comodin", "+2", "/Cartas/2_wild_draw_green.png");
        this.cartas.add(masDosVerde);
        Comodines masDosRojo = new Comodines(3, "comodin", "+2", "/Cartas/2_wild_draw_red.png");
        this.cartas.add(masDosRojo);
        Comodines masDosAmarillo = new Comodines(4, "comodin", "+2", "/Cartas/2_wild_draw_yellow.png");
        this.cartas.add(masDosAmarillo);

        //Comodines bloqueo
        Comodines bloqueoAzul = new Comodines(1, "comodin", "block", "/Cartas/skip_blue.png");
        this.cartas.add(bloqueoAzul);
        Comodines bloqueoVerde = new Comodines(2, "comodin", "block", "/Cartas/skip_green.png");
        this.cartas.add(bloqueoVerde);
        Comodines bloqueoRojo = new Comodines(3, "comodin", "block", "/Cartas/skip_red.png");
        this.cartas.add(bloqueoRojo);
        Comodines bloqueoAmarillo = new Comodines(4, "comodin", "block", "/Cartas/skip_yellow.png");
        this.cartas.add(bloqueoAmarillo);
    }
}


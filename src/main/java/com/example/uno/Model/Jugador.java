package com.example.uno.Model;

import java.util.ArrayList;

/**
 * Esta clase es el modelo que representa a un jugador (humano o CPU).
 * Mantiene una lista de cartas en el mazo.
 * Inclute métodos para añadir y quitar cartas,
 * consultar tamaño de la mano y obtener la lista.
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class Jugador {

    private  ArrayList<Cartas> mazo = new ArrayList();

    /**
     * Este método entrega la cantidad de cartas que tiene actualmente en mano el jugador,
     * para detectar mazo vacío (victoria) o para iterar sobre sus cartas
     * @return mazo.size
     */
    public int mazoSize() {
        return mazo.size();
    }

    /**
     * Este método añade la carta indicada al final de la mano del jugador,
     * por ejemplo al repartir o como penalización.
     * @param carta
     */
    public void addCarta(Cartas carta){
        mazo.add(carta);
    }

    /**
     * Este métooo devuelve la carta que está en la posición i de la mano,
     * permitiendo inspeccionarla o jugarla.
     * @param i
     * @return mazo.get
     */
    public Cartas getCarta(int i){
        return mazo.get(i);
    }

    /**
     * Este método elimina la primera aparición de esa carta de la mano del jugador,
     * usado cuando el jugador o la CPU descarta o penaliza.
     * @param carta
     */
    public void removeCarta(Cartas carta){
        mazo.remove(carta);
    }
}

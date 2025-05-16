package com.example.uno.Model;

import java.util.ArrayList;

/**
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class Jugador {

    private  ArrayList<Cartas> mazo = new ArrayList();

    //LAS SIGUIENTES FUNCIONES SE USAN EN LA REPARTICION DE CARTAS
    public int mazoSize() {
        return mazo.size();
    }

    public void addCarta(Cartas carta){
        mazo.add(carta);
    }

    public Cartas getCarta(int i){
        return mazo.get(i);
    }

    public void removeCarta(Cartas carta){
        mazo.remove(carta);
    }
}

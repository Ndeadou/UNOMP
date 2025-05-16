package com.example.uno.Model;

/**
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class  Comodines extends Cartas {

    //Tipos de comodines 4, Reversa, Bloqueo, +2, +4
    private String simbolo;

    public Comodines(int color, String tipodecarta, String simbolo, String ruta) {

        super(tipodecarta, color, ruta);
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}


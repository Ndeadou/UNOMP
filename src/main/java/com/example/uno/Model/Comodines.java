package com.example.uno.Model;

/**
 * Esta es una subclase de Cartas para las cartas especiales (+2, +4, bloqueo y cambio de color).
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

    /**
     * Este método retorna el símbolo del comodín ("+2", "+4", "block", "camb", etc.),
     * permitiendo a mesa de juego distinguir qué acción especial ejecutar cuando se juega esa carta CartasN.
     * @return simbolo
     */
    public String getSimbolo() {
        return simbolo;
    }
}


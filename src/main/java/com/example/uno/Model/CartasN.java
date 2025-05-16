package com.example.uno.Model;

/**
 * Esta es una subclase de Cartas para las cartas numéricas (0–9).
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class CartasN extends Cartas{

    private int numero;

    public CartasN(int color, String tipodecarta, int numero, String ruta) {

        super(tipodecarta, color, ruta);
        this.numero = numero;
    }

    /**
     * Este método devuelve el valor numérico de la carta (0–9) para las cartas de tipo numérico.
     * Se usa, por ejemplo, al comparar números iguales para ver si la carta puede jugarse sobre la pila .
     * @return numero
     */

    public int getNumero() {
        return numero;
    }
}

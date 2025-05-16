package com.example.uno.Model;

/**
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

    public int getNumero() {
        return numero;
    }
}

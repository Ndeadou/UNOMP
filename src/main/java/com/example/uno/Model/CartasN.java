package com.example.uno.Model;

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

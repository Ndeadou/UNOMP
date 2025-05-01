package com.example.uno.Model;

public class  Comodines extends Cartas {

    //Tipos de comodines 4, Reversa, Bloqueo, +2, +4
    private String simbolo;

    public Comodines(int color, String tipodecarta, String simbolo, String ruta) {

        super(tipodecarta, color, ruta);
        this.simbolo = simbolo;
    }
}


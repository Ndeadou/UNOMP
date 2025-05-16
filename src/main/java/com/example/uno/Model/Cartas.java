package com.example.uno.Model;

/**
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class Cartas {

    private String ruta;
    private String tipodecarta;
    private int color;

    public Cartas(String tipodecarta, int color, String ruta) {
        this.tipodecarta = tipodecarta;
        this.color = color;
        this.ruta = ruta;
    }

    // Getter para el atributo 'tipodecarta'
    public String getTipodecarta() {
        return tipodecarta;
    }

    public String getRutaImagen(){
        return ruta;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

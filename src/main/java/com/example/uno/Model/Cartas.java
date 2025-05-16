package com.example.uno.Model;

/**
 * Esta clase define propiedades de las cartas como:
 * color, ruta de imagen, símbolo, etc.
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

    /**
     * Este método devuelve el valor de tipodecarta,
     * para saber si es una carta numérica o de comodín
     * @return tipodecarta
     */
    public String getTipodecarta() {
        return tipodecarta;
    }

    /**
     * Retorna la cadena ruta de la imagen, para cargarla en un ImageView.
     * @return ruta
     */
    public String getRutaImagen(){
        return ruta;
    }

    /**
     * Este método devuelve el código numérico del color de la carta,
     * para la lógica de juego (comparar colores).
     * @return color
     */
    public int getColor() {
        return color;
    }

    /**
     * Este método permite cambiar dinámicamente el color de la carta.
     * @return color
     */
    public void setColor(int color) {
        this.color = color;
    }
}

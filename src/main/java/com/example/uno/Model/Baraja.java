package com.example.uno.Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Miguel Descance
 * @author Erick Obando
 * @version 1.0
 */

public class Baraja {

    private ArrayList<Cartas> cartas = new ArrayList<>();

    public void añadirCarta(Cartas carta) {cartas.add(carta);}

    public void mezclarBaraja() {Collections.shuffle(this.cartas);}

    public void eliminarCarta(Cartas carta) {
        this.cartas.remove(carta);
    }

    public int size() {
        return this.cartas.size();
    }

    public Cartas getCarta(int i){
        return this.cartas.get(i);
    }

    public void crearCartas() {

        //Debemos instanciar los numeros "0 y 2" manualmente
        for (int i = 0; i < 10; i++ ) {
            for (int j = 1; j <= 4; j++ ) {
                if(j==1){
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_blue.png");
                    this.cartas.add(carta);
                }else if (j==2){
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_green.png");
                    this.cartas.add(carta);
                }else if (j==3) {
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_red.png");
                    this.cartas.add(carta);
                }else {
                    CartasN carta = new CartasN(j, "num", i, "/Cartas/"+i+"_yellow.png");
                    this.cartas.add(carta);
                }
            }
        }

        //Comodines +2
        Comodines masDosAzul = new Comodines(1, "comodin", "+2", "/Cartas/2_wild_draw_blue.png");
        this.cartas.add(masDosAzul);
        Comodines masDosVerde = new Comodines(2, "comodin", "+2", "/Cartas/2_wild_draw_green.png");
        this.cartas.add(masDosVerde);
        Comodines masDosRojo = new Comodines(3, "comodin", "+2", "/Cartas/2_wild_draw_red.png");
        this.cartas.add(masDosRojo);
        Comodines masDosAmarillo = new Comodines(4, "comodin", "+2", "/Cartas/2_wild_draw_yellow.png");
        this.cartas.add(masDosAmarillo);

       /* //Comodines reserva
        Comodines reservaAzul = new Comodines(1, "comodin", "res", "/Cartas/reserve_blue.png");
        this.cartas.add(reservaAzul);
        Comodines reservaVerde = new Comodines(2, "comodin", "res", "/Cartas/reserve_green.png");
        this.cartas.add(reservaVerde);
        Comodines reservaRojo= new Comodines(3, "comodin", "res", "/Cartas/reserve_red.png");
        this.cartas.add(reservaRojo);
        Comodines reservaAmarillo = new Comodines(4, "comodin", "res", "/Cartas/reserve_yellow.png");
        this.cartas.add(reservaAmarillo);
*/
        //Comodines bloqueo
        Comodines bloqueoAzul = new Comodines(1, "comodin", "block", "/Cartas/skip_blue.png");
        this.cartas.add(bloqueoAzul);
        Comodines bloqueoVerde = new Comodines(2, "comodin", "block", "/Cartas/skip_green.png");
        this.cartas.add(bloqueoVerde);
        Comodines bloqueoRojo = new Comodines(3, "comodin", "block", "/Cartas/skip_red.png");
        this.cartas.add(bloqueoRojo);
        Comodines bloqueoAmarillo = new Comodines(4, "comodin", "block", "/Cartas/skip_yellow.png");
        this.cartas.add(bloqueoAmarillo);

        //Comodines +4
        //Comodines mas4 = new Comodines(0, "comodin", "+4", "/Cartas/4_wild_draw.png");
        //this.cartas.add(mas4);

        //Comodines cambiodecolor
        //Comodines cambioColor = new Comodines(0, "comodin", "camb", "/Cartas/wild.png");
        //this.cartas.add(cambioColor);

    }
}


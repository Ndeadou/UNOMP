package com.example.uno.Controller;

import com.example.uno.Model.MesaDeJuego;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.swing.*;

public class HelloController {
    @FXML
    private Button idBaraja;

    @FXML
    private HBox idMazo1;

    MesaDeJuego mesa = new MesaDeJuego();
    @FXML
    public void initialize() {
        mesa.crearCartas();
        mesa.repartirCartas(5);
    }
}
package com.example.climatemonitoringapp;

public class AreaInteresse {
    private String nome;
    private String coordX;
    private String coordY;
    private String stato;


    public AreaInteresse(String nome, String stato, String coordX, String coordY) {
        this.nome = nome;
        this.coordX = coordX;
        this.coordY = coordY;
        this.stato = stato;
    }

    public String getNome() {
        return nome;
    }

    public String getCoordX() {
        return coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public String getStato() {
    	return stato;
    }



}

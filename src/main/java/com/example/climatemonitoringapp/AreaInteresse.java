package com.example.climatemonitoringapp;

import java.io.Serializable;



//prova
/**
 * Classe che rappresenta un'area di interesse
 * Contiene il nome, le coordinate e lo stato di un'area di interesse
 */
public class AreaInteresse implements Serializable {
	private static final long serialVersionUID = 1L;
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

    public AreaInteresse(String nome) {
        this.nome = nome;

    }

    /**
     * Metodo che restituisce il nome dell'area di interesse
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     * Metodo che restituisce le coordinate dell'area di interesse
     * @return
     */
    public String getCoordX() {
        return coordX;
    }

    /**
     * Metodo che restituisce le coordinate dell'area di interesse
     * @return
     */
    public String getCoordY() {
        return coordY;
    }

    /**
     * Metodo che restituisce lo stato dell'area di interesse
     * @return
     */
    public String getStato() {
    	return stato;
    }

    @Override
    public String toString() {
        return nome;
    }
}

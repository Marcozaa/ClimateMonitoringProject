package com.example.climatemonitoringapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Componente che gestisce la visualizzazione dei dati di un'area di interesse
 */
public class DatiMeteoAreaComponentController {
    @FXML
    private Label nomeCentroLabel;
    @FXML
    private Label ventoLabel;
    @FXML
    private Label pressioneLabel;
    @FXML
    private Label umiditaLabel;
    @FXML
    private Label altitudineLabel;
    @FXML
    private Label temperaturaLabel;
    @FXML
    private Label massaLabel;
    @FXML
    private Label precipitazioniLabel;

    @FXML
    private Label dataLabel;
    @FXML
    private Label oraLabel;

    public void setNomeCentroLabel(String nomeCentro){
        nomeCentroLabel.setText(nomeCentro);
    }

    public void setVentoLabel(String ventoScore){
        ventoLabel.setText(ventoScore);
    }

    public void setDataLabel(String data){
        dataLabel.setText(data);
    }

    public void setOraLabel(String ora){
        oraLabel.setText(ora);
    }

    public void setPressioneLabel(String pressioneScore){
        pressioneLabel.setText(pressioneScore);
    }

    public void setUmiditaLabel(String umiditaScore){
        umiditaLabel.setText(umiditaScore);
    }

    public void setAltitudineLabel(String altitudineScore){
        altitudineLabel.setText(altitudineScore);
    }

    public void setTemperaturaLabel(String temperaturaScore){
        temperaturaLabel.setText(temperaturaScore);
    }

    public void setMassaLabel(String massaScore){
        massaLabel.setText(massaScore);
    }

    public void setPrecipitazioniLabel(String precipitazioniScore){
        precipitazioniLabel.setText(precipitazioniScore);
    }

    // TODO create 1 single function to set all the labels


}

package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InformazioniMeteoAreaController {

    private String nomeArea;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label nomeAreaLabel;
    @FXML
    private VBox vbox;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label nRilevazioniVento;
    @FXML
    private Label nRilevazioniPrecipitazioni;
    @FXML
    private Label nRilevazioniTemperatura;
    @FXML
    private Label nRilevazioniUmidita;
    @FXML
    private Label nRilevazioniPressione;
    @FXML
    private Label nRilevazioniAltitudine;
    @FXML
    private Label nRilevazioniMassa;
    @FXML
    private Label mediaVento;
    @FXML
    private Label mediaPrecipitazioni;
    @FXML
    private Label mediaTemperatura;
    @FXML
    private Label mediaUmidita;
    @FXML
    private Label mediaPressione;
    @FXML
    private Label mediaAltitudine;
    @FXML
    private Label mediaMassa;




    AnchorPane anchorPane2 = new AnchorPane();

    private int offsetY = 0;




    public void fillScene() throws IOException {
        List<List<String>> parametriArea = new ArrayList<>(); // Contiene i parametri di registrazione di un'area (es. nomeCentro,vento, timestamp, ecc.)
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/parametriClimatici.dati.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                System.out.println(line);
                System.out.println("Lunghezza: " + values.length);
                if(values.length == 11 ){
                    if(values[1].equals(nomeArea)) {
                        parametriArea.add(Arrays.asList(values));
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(parametriArea.size() == 0) {

                Label label = new Label("Non esistono dati per questa Area");
                label.setLayoutY(offsetY+=150);
                anchorPane2.getChildren().add(label);
                vbox.getChildren().add(anchorPane2);


        }

        for(List<String> record : parametriArea){
            //System.out.println(record);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DatiMeteoAreaComponent.fxml"));
            AnchorPane pane = loader.load();
            DatiMeteoAreaComponentController datiController = loader.getController();
            datiController.setNomeCentroLabel(record.get(0));
            datiController.setVentoLabel(record.get(2));
            datiController.setUmiditaLabel(record.get(3));
            datiController.setPressioneLabel(record.get(4));
            datiController.setTemperaturaLabel(record.get(5));
            datiController.setPrecipitazioniLabel(record.get(6));
            datiController.setAltitudineLabel(record.get(7));
            datiController.setMassaLabel(record.get(8));
            datiController.setDataLabel(record.get(9));
            datiController.setOraLabel(record.get(10));
            pane.setLayoutY(offsetY+=150);
            vbox.getChildren().add(pane);
        }

        fillStats();

    }

    public void fillStats(){
        //List<List<Integer>> rilevazioniSpecifiche = new ArrayList<>(); // Contiene le rilevazioni di un determinato tipo (es. vento per record[0]))
        Map<String, List<Integer>> rilevazioniSpecifiche = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/parametriClimatici.dati.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                System.out.println(line);
                System.out.println("Lunghezza: " + values.length);
                if(values.length == 11 ){
                    if(values[1].equals(nomeArea)) {
                        addScores(rilevazioniSpecifiche, "vento", Integer.parseInt(values[2]));
                        addScores(rilevazioniSpecifiche, "umidita", Integer.parseInt(values[3]));
                        addScores(rilevazioniSpecifiche, "pressione", Integer.parseInt(values[4]));
                        addScores(rilevazioniSpecifiche, "temperatura", Integer.parseInt(values[5]));
                        addScores(rilevazioniSpecifiche, "precipitazioni", Integer.parseInt(values[6]));
                        addScores(rilevazioniSpecifiche, "altitudine", Integer.parseInt(values[7]));
                        addScores(rilevazioniSpecifiche, "massa", Integer.parseInt(values[8]));


                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(rilevazioniSpecifiche);

        nRilevazioniVento.setText(String.valueOf(rilevazioniSpecifiche.get("vento").size()));
        nRilevazioniUmidita.setText(String.valueOf(rilevazioniSpecifiche.get("umidita").size()));
        nRilevazioniPressione.setText(String.valueOf(rilevazioniSpecifiche.get("pressione").size()));
        nRilevazioniTemperatura.setText(String.valueOf(rilevazioniSpecifiche.get("temperatura").size()));
        nRilevazioniPrecipitazioni.setText(String.valueOf(rilevazioniSpecifiche.get("precipitazioni").size()));
        nRilevazioniAltitudine.setText(String.valueOf(rilevazioniSpecifiche.get("altitudine").size()));
        nRilevazioniMassa.setText(String.valueOf(rilevazioniSpecifiche.get("massa").size()));

        mediaVento.setText(String.valueOf(rilevazioniSpecifiche.get("vento").stream().mapToInt(Integer::intValue).average().orElse(0)));
        mediaUmidita.setText(String.valueOf(rilevazioniSpecifiche.get("umidita").stream().mapToInt(Integer::intValue).average().orElse(0)));
        mediaPressione.setText(String.valueOf(rilevazioniSpecifiche.get("pressione").stream().mapToInt(Integer::intValue).average().orElse(0)));
        mediaTemperatura.setText(String.valueOf(rilevazioniSpecifiche.get("temperatura").stream().mapToInt(Integer::intValue).average().orElse(0)));
        mediaPrecipitazioni.setText(String.valueOf(rilevazioniSpecifiche.get("precipitazioni").stream().mapToInt(Integer::intValue).average().orElse(0)));
        mediaAltitudine.setText(String.valueOf(rilevazioniSpecifiche.get("altitudine").stream().mapToInt(Integer::intValue).average().orElse(0)));
        mediaMassa.setText(String.valueOf(rilevazioniSpecifiche.get("massa").stream().mapToInt(Integer::intValue).average().orElse(0)));

    }

    private static void addScores(Map<String, List<Integer>> weatherScores, String condition, int score) {
        weatherScores.computeIfAbsent(condition, k -> new ArrayList<>()).add(score);
    }

    public void setNomeArea(String nomeArea) {
        System.out.println("Nome area: " + nomeArea);
        this.nomeArea = nomeArea;
        nomeAreaLabel.setText(nomeArea);
    }

    public void tornaIndietro(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        root = loader.load();



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }


}

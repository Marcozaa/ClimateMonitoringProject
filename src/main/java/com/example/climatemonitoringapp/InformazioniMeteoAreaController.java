package com.example.climatemonitoringapp;

import climatemonitoringserver.Rilevazione;
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

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Questa classe gestisce la visualizzazione delle informazioni meteo di un'area di interesse
 *
 */
public class InformazioniMeteoAreaController {

    private String nomeArea;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;

    


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

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;





    AnchorPane anchorPane2 = new AnchorPane();

    private int offsetY = 0;




    public void fillScene() throws IOException {
        List<List<String>> parametriArea = new ArrayList<>(); // Contiene i parametri di registrazione di un'area (es. nomeCentro,vento, timestamp, ecc.)
        /*
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

         */

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

        HomepageController controller = loader.getController();
        if(currentUser!=null) {
            controller.setLoggedUser(currentUser);
        }
        controller.userCheck();
        controller.setConnectionSocket(socket, in, out);



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }


    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
        System.out.println(currentUser);
    }

    public void getRilevazioni(){
        try {
            out.writeObject("getRilevazioniCitta");
            out.writeObject(nomeArea);
            List< Rilevazione> rilevazioni = (List<Rilevazione>) in.readObject();
            for (Rilevazione r : rilevazioni) {
                System.out.println(r.getNomeCentroMonitoraggio() + " ");
                System.out.println(r.getVento() + " ");
                System.out.println(r.getUmidita() + " ");
                System.out.println(r.getPressione() + " ");
                System.out.println(r.getTemperatura() + " ");
                System.out.println(r.getPrecipitazioni() + " ");
                System.out.println(r.getAltitudineGhiacciai() + " ");
                System.out.println(r.getMassaGhiacciai() + " ");
                System.out.println(r.getDataRilevazione() + " ");
                System.out.println(r.getOraRilevazione() + " ");
                
            }

            if(rilevazioni.size() == 0) {
                Label label = new Label("Non sono ancora state inserite rilevazioni per questa Area");
                label.setLayoutY(offsetY+=150);
                anchorPane2.getChildren().add(label);
                vbox.getChildren().add(anchorPane2);
            }else{
                addRilevazioni(rilevazioni);
            }

            nRilevazioniVento.setText(String.valueOf(rilevazioni.size()));
            nRilevazioniPrecipitazioni.setText(String.valueOf(rilevazioni.size()));
            nRilevazioniTemperatura.setText(String.valueOf(rilevazioni.size()));
            nRilevazioniUmidita.setText(String.valueOf(rilevazioni.size()));
            nRilevazioniPressione.setText(String.valueOf(rilevazioni.size()));
            nRilevazioniAltitudine.setText(String.valueOf(rilevazioni.size()));
            nRilevazioniMassa.setText(String.valueOf(rilevazioni.size()));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getStatistics(){
        try {
            out.writeObject("getStatistics");
            out.writeObject(nomeArea);
            List<Double> rilevazioni = (List<Double>) in.readObject();
            for (Double r : rilevazioni) {
                System.out.println(r);
            }
            mediaTemperatura.setText(String.valueOf(rilevazioni.get(0)));
            mediaUmidita.setText(String.valueOf(rilevazioni.get(1)));
            mediaPressione.setText(String.valueOf(rilevazioni.get(2)));
            mediaPrecipitazioni.setText(String.valueOf(rilevazioni.get(3)));
            mediaAltitudine.setText(String.valueOf(rilevazioni.get(4)));
            mediaMassa.setText(String.valueOf(rilevazioni.get(5)));
            mediaVento.setText(String.valueOf(rilevazioni.get(6)));





        } catch (Exception e) {
            e.printStackTrace();
            }
    }
    
    public void setConnectionSocket(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        this.in=in;
        this.out=out;
    }


    private void addRilevazioni(List<Rilevazione> rilevazioni){

        for(Rilevazione r : rilevazioni){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DatiMeteoAreaComponent.fxml"));
            AnchorPane pane = null;
            try {
                pane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DatiMeteoAreaComponentController datiController = loader.getController();
            datiController.setNomeCentroLabel(r.getNomeCentroMonitoraggio());
            datiController.setVentoLabel(String.valueOf(r.getVento()));
            datiController.setUmiditaLabel(String.valueOf(r.getUmidita()));
            datiController.setPressioneLabel(String.valueOf(r.getPressione()));
            datiController.setTemperaturaLabel(String.valueOf(r.getTemperatura()));
            datiController.setPrecipitazioniLabel(String.valueOf(r.getPrecipitazioni()));
            datiController.setAltitudineLabel(String.valueOf(r.getAltitudineGhiacciai()));
            datiController.setMassaLabel(String.valueOf(r.getMassaGhiacciai()));
            datiController.setDataLabel(r.getDataRilevazione());
            datiController.setOraLabel(r.getOraRilevazione());
            pane.setLayoutY(offsetY+=150);
            vbox.getChildren().add(pane);
        }

    }


}

package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Componente che gestisce la visualizzazione dei dati di un'area d'interesse
 */
public class AreaResultController {

    @FXML
    Label areaName,distanzaLabel;
    @FXML
    Label areaCoords;

    @FXML
    Label areaState;

    private double distance = 0.0;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public void setAreaName(String name){
        areaName.setText(name);
    }
    public void setAreaCoords(String coords){
        areaCoords.setText(coords);
    }

    public void setAreaState(String state){
        areaState.setText(state);
    }

    /**
     * Questo metodo imposta come testo della label la distanza dalle coordinate inserite
     */
    public void setDistance(double distance){
        this.distance = distance;
        distance = Math.round(distance * 100.0) / 100.0; // Arrotonda a 2 decimali
        distanzaLabel.setText("Distanza: " + String.valueOf(distance) + " km");
    }

    public void setAreaDistance(){

    }


    /**
     * Questo metodo permette di visualizzare i dati dell'area di interesse, (quando si clicca sul bottone "Visualizza")
     * @param e
     * @throws Exception
     */
    public void visualizzaDatiArea(ActionEvent e) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InformazioniMeteoArea.fxml"));
        root = loader.load();

        InformazioniMeteoAreaController controller = loader.getController();

        controller.setNomeArea(areaName.getText());
        controller.fillScene();
        controller.setCurrentUser(currentUser);
        controller.setConnectionSocket(socket, in, out);
        controller.getRilevazioni();
        controller.getStatistics();



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void setConnectionSocket(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
        System.out.println(currentUser);
    }
}

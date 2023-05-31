package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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

    public void setAreaName(String name){
        areaName.setText(name);
    }
    public void setAreaCoords(String coords){
        areaCoords.setText(coords);
    }

    public void setAreaState(String state){
        areaState.setText(state);
    }

    public void setDistance(double distance){
        this.distance = distance;
        distanzaLabel.setText("Distanza " + String.valueOf(distance) + " km");
    }

    public void setAreaDistance(){

    }


    public void visualizzaDatiArea(ActionEvent e) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InformazioniMeteoArea.fxml"));
        root = loader.load();

        InformazioniMeteoAreaController controller = loader.getController();

        controller.setNomeArea(areaName.getText());
        controller.fillScene();



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}

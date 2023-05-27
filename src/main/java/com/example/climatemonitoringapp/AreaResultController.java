package com.example.climatemonitoringapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AreaResultController {

    @FXML
    Label areaName;
    @FXML
    Label areaCoords;

    @FXML
    Label areaState;

    public void setAreaName(String name){
        areaName.setText(name);
    }
    public void setAreaCoords(String coords){
        areaCoords.setText(coords);
    }

    public void setAreaState(String state){
        areaState.setText(state);
    }
}

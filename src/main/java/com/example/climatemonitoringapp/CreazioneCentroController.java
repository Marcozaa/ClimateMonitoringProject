package com.example.climatemonitoringapp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreazioneCentroController {
    @FXML
    public MenuButton menuButton;

    public void initialize() {

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/AreeInteresse.csv"))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                System.out.println(values[0]);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < records.size(); i++) {
            CheckBox cb = new CheckBox(records.get(i).get(0));
            CustomMenuItem item = new CustomMenuItem(cb);
            item.setHideOnClick(false);
            cb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (cb.isSelected()) {
                        System.out.println(cb.getText()+" Added");
                    } else {
                        System.out.println(cb.getText()+" Removed");
                    }
                }
            });
            menuButton.getItems().add(item);
        }
        System.out.println(menuButton.getItems());


    }


}

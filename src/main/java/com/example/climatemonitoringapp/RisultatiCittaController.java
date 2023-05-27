package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RisultatiCittaController {


    @FXML
    Label cittaLabel;
    @FXML
    GridPane resultsGrid;
    @FXML
    AnchorPane testpane;




    private Stage stage;
    private Scene scene;
    private Parent root;


    private String cittaCercata;

    private List<AreaInteresse> areeInteressePossibili = new ArrayList<>();

    @FXML
    protected void initialize() throws IOException {






        //testpane.getChildren().add(pane);


        //List<Button> buttonlist = new ArrayList<>(); //our Collection to hold newly created Buttons
        //for (int i = 0; i < 3; i++) {




        //}
    }


    public void displayName(String citta){
        cittaLabel.setText("Risultati di ricerca per: " + citta );


    }

    public void setCittaCercata(String cittaCercata) throws IOException {
        this.cittaCercata = cittaCercata;
        filterFile(cittaCercata);
    }

    private void filterFile(String cittaCercata) throws IOException {
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


        for(int i=0; i<records.size(); i++){
            if(records.get(i).get(0).contains(cittaCercata)){
                areeInteressePossibili.add(new AreaInteresse(records.get(i).get(0),records.get(i).get(1),records.get(i).get(2).toString(),records.get(i).get(3).toString()));
            }

        }

        System.out.println(".....................");
        for (int i=0; i< areeInteressePossibili.size(); i++){
            System.out.println(areeInteressePossibili.get(i).getNome());
        }
        System.out.println(".....................");



        for (int j = 0; j < areeInteressePossibili.size(); j++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AreaResult.fxml"));
            AnchorPane pane = loader.load();
            AreaResultController arc = loader.getController();
            arc.setAreaName(areeInteressePossibili.get(j).getNome());
            arc.setAreaState(areeInteressePossibili.get(j).getStato());
            arc.setAreaCoords(areeInteressePossibili.get(j).getCoordX() + "N, " + areeInteressePossibili.get(j).getCoordY() + "E");
            //Button button = new Button("Button " + i + " " + j); //create new Button
            //button.setLayoutX(30 * i); //set x coordinate
            resultsGrid.add(pane, 0, j); //add button to the GridPane
        }

    }


    public void tornaIndietro(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        root = loader.load();



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

}

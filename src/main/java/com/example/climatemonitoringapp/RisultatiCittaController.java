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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



/**
 * Questa classe gestisce la visualizzazione dei risultati di ricerca di una città
 * sia per coordinate che per nome
 */
public class RisultatiCittaController {


    @FXML
    Label cittaLabel;
    @FXML
    ScrollPane scrollPane;
    @FXML
    VBox vbox;




    private Stage stage;
    private Scene scene;
    private Parent root;
    private User currentUser;


    private String cittaCercata;

    final int NUM_AREAS_BY_COORDINATES = 3;

    private List<AreaInteresse> areeInteressePossibili = new ArrayList<>();

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @FXML
    protected void initialize() throws IOException {






        //testpane.getChildren().add(pane);


        //List<Button> buttonlist = new ArrayList<>(); //our Collection to hold newly created Buttons
        //for (int i = 0; i < 3; i++) {




        //}
    }


    /**
     * Questo metodo imposta come testo nella label in alto a sinistra della UI la citta ricercata.
     * @param citta
     */
    public void displayName(String citta){
        cittaLabel.setText("Risultati di ricerca per: " + citta );


    }

    public void setCittaCercata(String cittaCercata, Socket socket, ObjectInputStream in,ObjectOutputStream out ) throws IOException {
        this.cittaCercata = cittaCercata;
        this.socket = socket;
        this.out = out;
        this.in = in;
        filterFile(cittaCercata);
    }

    public void setConnectionSocket(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        this.in = in;
        this.out = out;
    }


    /**
     * Questo metodo scandisce il file AreeInteresse.csv e filtra le aree di interesse in base
     * alla città più vicina alle coordinate inserite dall'utente
     */
    public void filterByCoordinates(Coordinate coordinate) throws IOException {
        List<List<String>> records = new ArrayList<>();
        List<Double> distances = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/AreeInteresse.csv"))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Coordinate currentCoords = new Coordinate(Double.parseDouble(values[2]),Double.parseDouble(values[3]));
                double distance = coordinate.getDistance(currentCoords);
                records.add(Arrays.asList(values));
                distances.add(distance);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int j=0; j<records.size(); j++){
            System.out.println(records.get(j).get(0));
            System.out.println(distances.get(j));
        }

        selectionSort(distances,records);


        for (int j = 0; j < NUM_AREAS_BY_COORDINATES; j++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AreaResult.fxml"));
            AnchorPane pane = loader.load();
            AreaResultController arc = loader.getController();
            arc.setAreaName(records.get(j).get(0));
            arc.setAreaState(records.get(j).get(1));
            arc.setAreaCoords(records.get(j).get(2) + "N, " + records.get(j).get(3) + "E");
            arc.setDistance(metersToKm(distances.get(j)));
            //Button button = new Button("Button " + i + " " + j); //create new Button
            //button.setLayoutX(30 * i); //set x coordinate
            vbox.getChildren().add(pane);

        }


    }


    private void selectionSort(List<Double> distances, List<List<String>> records){
        for(int i=0; i< distances.size(); i++){
            int currentIndex = i;
            for(int j=i+1; j<distances.size(); j++){
                if(distances.get(j)<distances.get(currentIndex)){
                    Collections.swap(records, currentIndex, j);


                    double temp = distances.get(currentIndex);
                    distances.set(currentIndex,distances.get(j));
                    distances.set(j,temp);
                }
            }
        }

    }

    private double metersToKm(double meters){
        return (double) (Math.round( meters/1000*10.0)/10.0);
    }

    /**
     * Questo metodo filtra il file AreeInteresse.csv in base alla città inserita dall'utente
     * @param cittaCercata
     * @throws IOException
     */
    private void filterFile(String cittaCercata) throws IOException {

        /*List<List<String>> records = new ArrayList<>();
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
            vbox.getChildren().add(pane);

        }*/

        while(out == null || in == null) {
            System.out.println("Waiting for connection");
        }
        getAreasFromServer();

    }

    public void getAreasFromServer(){
        try {
            out.writeObject("getAreeInteresse");
            out.writeObject(cittaCercata);
            List<AreaInteresse> aree = (List<AreaInteresse>) in.readObject();
            for (AreaInteresse a : aree) {
                System.out.println(a.getNome());
            }

            for (int j = 0; j < aree.size(); j++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AreaResult.fxml"));
                AnchorPane pane = loader.load();
                AreaResultController arc = loader.getController();
                arc.setAreaName(aree.get(j).getNome());
                arc.setAreaState(aree.get(j).getStato());
                arc.setAreaCoords(aree.get(j).getCoordX() + "N, " + aree.get(j).getCoordY() + "E");
                //Button button = new Button("Button " + i + " " + j); //create new Button
                //button.setLayoutX(30 * i); //set x coordinate
                vbox.getChildren().add(pane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    public void tornaIndietro(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        root = loader.load();

        HomepageController controller = loader.getController();
        controller.setLoggedUser(currentUser);
        controller.setConnectionSocket(socket, in, out);


        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

}

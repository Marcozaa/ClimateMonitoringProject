package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {

    @FXML
    private TextField cittaCercata;
    @FXML
    private Button loginOperatoriCTA;
    @FXML
    private Label userName;
    @FXML
    private Button creazioneCentroCTA;

    private User currentUser;



    private Stage stage;
    private Scene scene;
    private Parent root;
    public void cercaCitta(ActionEvent e) throws IOException {
        String city = cittaCercata.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("RisultatiCitta.fxml"));
        root = loader.load();

        RisultatiCittaController rcController = loader.getController();

        rcController.displayName(city);
        rcController.setCittaCercata(city);

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);




        System.out.println(cittaCercata.getText());
    }

    public void loginOperatoriCTA(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginOperatore.fxml"));
        root = loader.load();



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void setLoggedUser(User user){
        this.currentUser = user;
        userName.setText(currentUser.getUsername());
    }

    public void creazioneCentroCTA(ActionEvent e) throws IOException {
        if(currentUser!= null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreazioneCentro.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }

    }


}

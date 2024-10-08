package com.example.climatemonitoringapp;

//import animatefx.animation.FadeIn;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//import animatefx.animation.Bounce;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Questa classe gestisce la homepage dell'applicazione
 * Al suo interno sono presenti i metodi per la ricerca di una città, per il login degli operatori e per la creazione di un nuovo centro di monitoraggio
 * @Author: Marco, Stefano
 */
public class HomepageController {

    @FXML
    private TextField cittaCercata, latitudineInserita, longitudineInserita;
    @FXML
    private Button loginOperatoriCTA;
    @FXML
    private Button inserimentoAreaCTA;
    @FXML
    private Label userName;
    @FXML
    private Button creazioneCentroCTA;
    @FXML
    private Button inserimentoValoriCTA;

    private User currentUser;

    private Pane pane1;



    private Stage stage;
    private Scene scene;
    private Parent root;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/NewAmsterdam-Regular.ttf"), 12);


    public void userCheck() {
        if(currentUser==null){
            creazioneCentroCTA.setVisible(false);
            inserimentoValoriCTA.setVisible(false);
            inserimentoAreaCTA.setVisible(false);
        }else{
            creazioneCentroCTA.setVisible(true);
            inserimentoValoriCTA.setVisible(true);
            inserimentoAreaCTA.setVisible(true);}

    }
    public void initialize(){
        userCheck();

        userName.setFont(font);



    }



    public void cercaCitta(ActionEvent e) throws IOException {
        String city = cittaCercata.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("RisultatiCitta.fxml"));
        root = loader.load();

        RisultatiCittaController rcController = loader.getController();

        rcController.displayName(city);
        rcController.setCurrentUser(currentUser);

        System.out.println(currentUser + " homepagecontroller");
        rcController.setCittaCercata(city,socket, in, out);

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);




        System.out.println(cittaCercata.getText());
    }

    public void loginOperatoriCTA(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginOperatore.fxml"));

        root = loader.load();

        LoginOperatoreController loController = loader.getController();
        loController.setLoggedUser(currentUser);
        loController.setConnectionSocket(socket, in, out);

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

            CreazioneCentroController ccController = loader.getController();
            ccController.setLoggedUser(currentUser);
            ccController.setConnectionSocket(socket, in, out);

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }

    }


    public void inserimentoValoriCTA(ActionEvent e) throws IOException {
        if(currentUser!= null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InserimentoDatiClimatici.fxml"));
            root = loader.load();

            InserimentoDatiController idController = loader.getController();
            idController.setCurrentUser(currentUser);
            idController.setConnectionSocket(socket, in, out);
            //idController.fillOptions();

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }
    }

    public void setConnectionSocket(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        this.in = in;
        this.out = out;
    }
    public void inserimentoAreaCTA(ActionEvent e) throws IOException {
        if(currentUser!= null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InserimentoAreaInteresse.fxml"));
            root = loader.load();

            InserimentoAreaInteresseController iaController = loader.getController();
            iaController.setCurrentUser(currentUser);
            iaController.setConnectionSocket(socket, in, out);

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }
    }

    public void cercaCittaByCoordinate(ActionEvent e ) throws IOException {
        String latitudine = latitudineInserita.getText();
        String longitudine = longitudineInserita.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("RisultatiCitta.fxml"));
        try {
            root = loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        RisultatiCittaController rcController = loader.getController();

        rcController.setCurrentUser(currentUser);
        rcController.setConnectionSocket(socket, in, out);
        rcController.setCittaCercata("Ricerca per coordinate: " + latitudine + "N " + longitudine + "E");

        rcController.filterByCoordinates(new Coordinate(Double.parseDouble(latitudine), Double.parseDouble(longitudine)));

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }


    public void testPane(){
        System.out.println("reyu");
        //new FadeIn(pane1).play();
    }




}

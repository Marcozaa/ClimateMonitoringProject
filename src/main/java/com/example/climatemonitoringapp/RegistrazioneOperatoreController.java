package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Questa classe gestisce la registrazione di un nuovo operatore
 */
public class RegistrazioneOperatoreController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private AnchorPane pannelloAncora;

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Questo metodo registra un nuovo operatore, ossia ne inserisce le credenziali nel file credenzialiOperatori.txt
     * @param e
     */
    public void registraOperatore(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(insertCredentialsInDB(username, password)){
            System.out.println("Operatore registrato");
            Label label = new Label("Operatore registrato con successo!");
            label.setTextFill(javafx.scene.paint.Color.RED);
            pannelloAncora.getChildren().add(label);
            label.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            label.setAlignment(Pos.CENTER);
        }else{
            System.out.println("Errore nella registrazione");
            Label label = new Label("Errore nella registrazione");
            label.setTextFill(javafx.scene.paint.Color.RED);
            pannelloAncora.getChildren().add(label);
            label.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            label.setAlignment(Pos.CENTER);
        }

    }

    public static boolean insertCredentialsInDB(String username, String password){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/CimateMonitoringApp","postgres","Marco2003");

            if(connection != null){
                System.out.println("connection ok");

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("INSERT INTO operatore (nomeutente, password) VALUES ('"+username+"','"+password+"')");

                return true;



            }else{
                System.out.println("connection failed");
                return false;
            }
            //connection.close();

        } catch (Exception e) {
            System.out.println(e);
            return false;

        }

    }

    /**
     * Questo metodo collegato al bottone torna alla schermata di login
     * @param e
     * @throws IOException
     */
    public void tornaIndietroLogin(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginOperatore.fxml"));
        root = loader.load();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}

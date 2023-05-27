package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RegistrazioneOperatoreController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void registraOperatore(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            Files.write(Paths.get("src/main/resources/credenzialiOperatori.txt"), ("\n"+username+"\n"+password).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException ex) {

        }
        System.out.println(username);
        System.out.println(password);
    }

    public void tornaIndietroLogin(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginOperatore.fxml"));
        root = loader.load();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
}

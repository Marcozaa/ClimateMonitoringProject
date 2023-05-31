package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LoginOperatoreController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private User currentUser;


    public void checkCredenziali(ActionEvent e) throws IOException {
        boolean logged = false;
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            File myObj = new File("src/main/resources/credenzialiOperatori.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                if(username.equals(data) && password.equals(myReader.nextLine())){

                        logged = true;
                        break;


                }else{
                    System.out.println("Credenziali errate");
                }



            }
            myReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }

        if(logged){
            System.out.println("logged");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            root = loader.load();

            HomepageController controller = loader.getController();
            controller.setLoggedUser(new User(username, password));
            controller.userCheck();

            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
        }
        else{
            System.out.println("not logged");
        }
    }

    public void registraOperatoreCTA(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RegistrazioneOperatore.fxml"));
        root = loader.load();



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void tornaIndietro(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        root = loader.load();

        HomepageController controller = loader.getController();
        controller.setLoggedUser(currentUser);
        controller.userCheck();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public void setLoggedUser(User user){
        this.currentUser = user;
    }


}

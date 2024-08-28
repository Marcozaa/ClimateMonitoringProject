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

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Questa classe gestisce il login dell'operatore
 */
public class LoginOperatoreController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private AnchorPane pannelloAncora;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private User currentUser;

    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;


    /**
     * Questo metodo controlla le credenziali inserite dall'operatore e se corrette lo reindirizza alla homepage
     * @param e
     * @throws IOException
     */
    public void checkCredenziali(ActionEvent e) throws IOException {
        boolean logged = false;
        String username = usernameField.getText();
        String password = passwordField.getText();


        if(checkCredentialsInDB(username, password)){
            System.out.println("logged");
            logged = true;

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
            Label label = new Label("Credenziali errate");
            label.setTextFill(javafx.scene.paint.Color.RED);
            pannelloAncora.getChildren().add(label);
            label.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(label, 0.0);
            AnchorPane.setRightAnchor(label, 0.0);
            label.setAlignment(Pos.CENTER);

        }

    }

    /**
     * Questo metodo collegato al bottone permette di passare alla schermata di registrazione di un nuovo operatore
     * @param e
     * @throws IOException
     */
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
        if(currentUser!=null) {
            controller.setLoggedUser(currentUser);
        }
        controller.userCheck();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    public static boolean checkCredentialsInDB(String TriedUsername, String TriedPassword){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/CimateMonitoringApp","postgres","Marco2003");

            if(connection != null){
                System.out.println("connection ok");

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM operatore");
                while (rs.next()) {
                    System.out.print("Column 1 returned ");
                    String username = rs.getString(2);
                    String password = rs.getString(7);

                    if(username.equals(TriedUsername) && password.equals(TriedPassword)){
                        return true;
                    }
                }

                rs.close();
                st.close();
            }else{
                System.out.println("connection failed");
            }
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public void checkCredentials(ActionEvent e) throws IOException, ClassNotFoundException {
        String username = usernameField.getText();
        String password = passwordField.getText();


        out.writeObject("validateUser");
        out.writeObject(username);
        out.writeObject(password);

        // Receiving server response
        boolean isValid = (boolean) in.readObject();
        if(isValid){
        System.out.println("logged");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        root = loader.load();

        HomepageController controller = loader.getController();
        controller.setLoggedUser(new User(username, password));
        controller.userCheck();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }else{
        System.out.println("not logged");
        Label label = new Label("Credenziali errate");
        label.setTextFill(javafx.scene.paint.Color.RED);
        pannelloAncora.getChildren().add(label);
        label.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(label, 0.0);
        AnchorPane.setRightAnchor(label, 0.0);
        label.setAlignment(Pos.CENTER);

    }

    }

    public void setConnectionSocket(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        System.out.println("socket connesso in loginop= " + socket);
        this.in=in;
        this.out=out;
        //out = new ObjectOutputStream(socket.getOutputStream());
        //in = new ObjectInputStream(socket.getInputStream());
    }
    public void setLoggedUser(User user){
        this.currentUser = user;
    }


}

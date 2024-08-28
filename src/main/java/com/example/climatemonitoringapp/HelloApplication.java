package com.example.climatemonitoringapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Questa classe gestisce l'avvio dell'applicazione, dove creiamo la finestra principale
 */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //ConnessioneDB(); // Rimuovere dopo aver implementato la connessione al server


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
        //ConnessioneServer(fxmlLoader);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("ClimateMonitoring");
        stage.setScene(scene);

        String css = this.getClass().getResource("style.css").toExternalForm();

        scene.getStylesheets().add(css);

        InetAddress addr = InetAddress.getByName(null);
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, 2345);

        try {
            System.out.println("Client connected: socket = " + socket);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            HomepageController controller = fxmlLoader.getController();
            controller.setConnectionSocket(socket, in, out);
        } catch (IOException e){

        }

        stage.show();




    }

    public static void ConnessioneDB(){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/CimateMonitoringApp","postgres","Marco2003");

            if(connection != null){
                System.out.println("connection ok");
            }else{
                System.out.println("connection failed");
            }
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void ConnessioneServer(FXMLLoader loader) throws IOException {

    }



    public static void main(String[] args) {
        launch();
    }
}
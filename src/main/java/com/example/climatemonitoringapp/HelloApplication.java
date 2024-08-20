package com.example.climatemonitoringapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Questa classe gestisce l'avvio dell'applicazione, dove creiamo la finestra principale
 */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        ConnessioneDB();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("ClimateMonitoring");
        stage.setScene(scene);

        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
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



    public static void main(String[] args) {
        launch();
    }
}
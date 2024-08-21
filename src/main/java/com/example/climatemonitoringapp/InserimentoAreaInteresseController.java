package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Questa classe gestisce l'inserimento di un'area di interesse
 */
public class InserimentoAreaInteresseController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField nomeArea;
    @FXML
    private TextField statoArea;
    @FXML
    private TextField latitudineArea;
    @FXML
    private TextField longitudineArea;

    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void tornaIndietro(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        root = loader.load();

        HomepageController hpController = loader.getController();
        hpController.setLoggedUser(currentUser);
        hpController.userCheck();



        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    /**
     * Questo metodo converte i dati inseriti in una stringa unica da inserire nel file AreeInteresse.csv
     * @param nome
     * @param stato
     * @param latitudine
     * @param longitudine
     * @return
     */
    public String convertStringsToCSV(String nome,String stato, String latitudine,String longitudine){
        StringBuilder sb = new StringBuilder();
        sb.append(nome);
        sb.append(",");
        sb.append(stato);
        sb.append(",");
        sb.append(latitudine);
        sb.append(",");
        sb.append(longitudine);

        return sb.toString();


    }

    public static boolean inserimentoAreaInteresseDB(String nome, String stato, String latitudine, String longitudine) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/CimateMonitoringApp", "postgres", "Marco2003");

            if (connection != null) {
                System.out.println("connection ok");

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("INSERT INTO areainteresse (nomearea, stato, latitudine, longitudine) VALUES ('" + nome + "','" + stato + "','" + latitudine + "','" + longitudine + "')");


                return true;


            } else {
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
     * Questo metodo inserisce i dati di un'area di interesse nel file AreeInteresse.csv
     * @param e
     */
    public void inserisciDati(ActionEvent e){
        String nome = nomeArea.getText();
        String stato = statoArea.getText();
        String latitudine = latitudineArea.getText();
        String longitudine = longitudineArea.getText();

        String csvFilePath = "src/main/resources/AreeInteresse.csv";



        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {

            writer.write(convertStringsToCSV(nome, stato, latitudine, longitudine));
            writer.newLine();

            System.out.println("CSV file written successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if(inserimentoAreaInteresseDB(nome, stato, latitudine, longitudine)) {
            System.out.println("Area di interesse inserita in DB");
        }


    }


}

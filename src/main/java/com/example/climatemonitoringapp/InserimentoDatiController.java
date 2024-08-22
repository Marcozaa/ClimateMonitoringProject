package com.example.climatemonitoringapp;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Questa classe gestisce l'inserimento dei dati meteo di un'area di interesse
 *
 */
public class InserimentoDatiController {

    @FXML
    private Label nomeCentroLabel;
    @FXML
    private ComboBox citySelector;
    @FXML
    private Button buttonInserimentoDati;


    @FXML
    private TextField scoreVento;
    @FXML
    private TextField scoreMassa;
    @FXML
    private TextField scoreUmidita;
    @FXML
    private TextField scorePressione;
    @FXML
    private TextField scoreAltitudine;
    @FXML
    private TextField scorePrecipitazioni;
    @FXML
    private TextField scoreTemperatura;
    @FXML
    private Label labelVento, labelMassa, labelUmidita, labelPressione, labelAltitudine, labelPrecipitazioni, labelTemperatura;
    @FXML
    private AnchorPane noDataPanel;
    @FXML
    private Button backButton;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private String nomeCentro;


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

    public void getCittaControllate(){

    }

    /**
     * Metodo che viene chiamato quando si clicca sul bottone "Inserisci Dati"
     * permette di inserire le citta controllate dall'utente nella combobox
     * @throws IOException
     */
    public void fillOptions() throws IOException {
        List<List<String>> CentroUser = new ArrayList<>();
        List<String> CittaControllateUser = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/centroMonitoraggio.dati.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(values[0].equals(currentUser.getUsername())) {
                    CentroUser.add(Arrays.asList(values));
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(CentroUser.size() == 0){
            Label label = new Label("Non hai ancora un centro di monitoraggio");
            noDataPanel.getChildren().add(label);
            scoreTemperatura.setVisible(false);
            scorePrecipitazioni.setVisible(false);
            scoreAltitudine.setVisible(false);
            scorePressione.setVisible(false);
            scoreUmidita.setVisible(false);
            scoreMassa.setVisible(false);
            scoreVento.setVisible(false);
            buttonInserimentoDati.setVisible(false);
            nomeCentroLabel.setVisible(false);
            citySelector.setVisible(false);
            labelVento.setVisible(false);
            labelMassa.setVisible(false);
            labelUmidita.setVisible(false);
            labelPressione.setVisible(false);
            labelAltitudine.setVisible(false);
            labelPrecipitazioni.setVisible(false);
            labelTemperatura.setVisible(false);



        }else {

            for (List<String> record : CentroUser) {
                System.out.println(record);
                nomeCentroLabel.setText(record.get(1));


            }
            for(int i=7; i<CentroUser.get(0).size(); i++){
                CittaControllateUser.add(CentroUser.get(0).get(i));
                citySelector.getItems().add(CentroUser.get(0).get(i));
            }
        }



       





    }

    public static boolean inserimentoRilevazioneInDB(int scoreVento, int scoreUmidita, int scorePressione, int scoreTemperatura, int scorePrecipitazioni, int scoreAltitudine, int scoreMassa, String citta, String centro) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/CimateMonitoringApp", "postgres", "Marco2003");

            if (connection != null) {
                System.out.println("connection ok");

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String time = sdf.format(new Date());

                String pattern = "dd/MM/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("INSERT INTO rilevazione (data_di_rilevazione, ora,vento, umidita, pressione, temperatura, precipitazioni, altitudine_ghiacciai, massa_ghiacciai) VALUES ( '" + date + "','" + time + "','" + scoreVento + "','" + scoreUmidita + "','" + scorePressione + "','" + scoreTemperatura + "','" + scorePrecipitazioni + "','" + scoreAltitudine + "','" + scoreMassa + "')");


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

    public void initialize() {
        if(currentUser!= null) {

        }



    }



    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String convertDataToCSV(){
        StringBuilder csvData = new StringBuilder();
        csvData.append(nomeCentroLabel.getText());
        csvData.append(",");
        csvData.append(citySelector.getValue());
        csvData.append(",");
        csvData.append(scoreVento.getText());
        csvData.append(",");
        csvData.append(scoreUmidita.getText());
        csvData.append(",");
        csvData.append(scorePressione.getText());
        csvData.append(",");
        csvData.append(scoreTemperatura.getText());
        csvData.append(",");
        csvData.append(scorePrecipitazioni.getText());
        csvData.append(",");
        csvData.append(scoreAltitudine.getText());
        csvData.append(",");
        csvData.append(scoreMassa.getText());
        csvData.append(",");
        csvData.append(java.time.LocalDate.now());
        csvData.append(",");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(new Date());
        csvData.append(time);
        //csvData.append("\n");

        String csvContent = csvData.toString();
        return csvContent;
    }

    public void inserisciDati(ActionEvent e){

        /*
        String csvFilePath = "src/main/resources/parametriClimatici.dati.csv";



        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {

            writer.write(convertDataToCSV());
            writer.newLine();

            System.out.println("CSV file written successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */

        try {
            inserimentoRilevazioneInDB(Integer.parseInt(scoreVento.getText()), Integer.parseInt(scoreUmidita.getText()), Integer.parseInt(scorePressione.getText()), Integer.parseInt(scoreTemperatura.getText()), Integer.parseInt(scorePrecipitazioni.getText()), Integer.parseInt(scoreAltitudine.getText()), Integer.parseInt(scoreMassa.getText()), citySelector.getValue().toString(), nomeCentroLabel.getText());
        }catch (Exception ex){
            System.out.println(ex);
        }
        scoreTemperatura.clear();
        scorePrecipitazioni.clear();
        scoreAltitudine.clear();
        scoreMassa.clear();
        scorePressione.clear();
        scoreUmidita.clear();
        scoreVento.clear();
        citySelector.getSelectionModel().clearSelection();
    }
}

package com.example.climatemonitoringapp;

import javafx.application.Platform;
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
import java.net.Socket;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import climatemonitoringserver.AreaInteresse;


import climatemonitoringserver.CentroMonitoraggio;

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

    private CentroMonitoraggio centroMonitoraggio;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public void tornaIndietro(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
        root = loader.load();

        HomepageController hpController = loader.getController();
        hpController.setLoggedUser(currentUser);
        hpController.userCheck();
        hpController.setConnectionSocket(socket, in, out);

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }

    private boolean checkMonitoringCenter(){
          try {
                	out.writeObject("checkExistingMonitoringCenter");
        			out.writeObject(currentUser.getUsername());
        			Label label = new Label("");
        			System.out.println("label + " + label);
        			if(!(boolean)in.readObject()){
        				label.setText("Non hai ancora un centro di monitoraggio");
        				System.out.println("label + " + label);
                                          label.setVisible(true);
                                          System.out.println("label è visibile? + " + label.isVisible());
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

        			}else{
        				label.setText("Centro di monitoraggio esistente");
        				System.out.println("label + " + label);
                        System.out.println("Centro di monitoraggio esistente");
                        out.writeObject("getCentroFromOperatore");
                        out.writeObject(currentUser.getUsername());
                        centroMonitoraggio = (CentroMonitoraggio)in.readObject();
                        setNomeCentroLabel(centroMonitoraggio.getNome());
                        scoreTemperatura.setVisible(true);
                                        label.setVisible(false);
                                        System.out.println("label è visibile? + " + label.isVisible());
                                                    scorePrecipitazioni.setVisible(true);
                                                                    scoreAltitudine.setVisible(true);
                                                                    scorePressione.setVisible(true);
                                                                    scoreUmidita.setVisible(true);
                                                                    scoreMassa.setVisible(true);
                                                                    scoreVento.setVisible(true);
                                                                    buttonInserimentoDati.setVisible(true);
                                                                    nomeCentroLabel.setVisible(true);
                                                                    citySelector.setVisible(true);
                                                                    labelVento.setVisible(true);
                                                                    labelMassa.setVisible(true);
                                                                    labelUmidita.setVisible(true);
                                                                    labelPressione.setVisible(true);
                                                                    labelAltitudine.setVisible(true);
                                                                    labelPrecipitazioni.setVisible(true);
                                                                    labelTemperatura.setVisible(true);
        			return true;
        			}
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		return false;
    }

    public void getCittaControllate(){

    }

    public void setNomeCentroLabel(String nomeCentro){
        this.nomeCentro = nomeCentro;
        nomeCentroLabel.setText(nomeCentro);
    }
    
    public void setConnectionSocket(Socket socket, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        this.in = in;
        this.out = out;
    }






    public void initialize() {
        if(currentUser!= null) {

        }

        System.out.println("InserimentoDatiController initialized");
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                while(socket == null){
                    try {
                        System.out.println("Threads sleep for 100 milsec...");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                System.out.println("checkMonitoringCenter thread");
                if(checkMonitoringCenter()){
                    try {
                        out.writeObject("getCittaControllate");
                        out.writeObject(centroMonitoraggio.getId());
                        List<AreaInteresse> cittaControllate = (List<AreaInteresse>)in.readObject();
                        centroMonitoraggio.setAreaInteresse(cittaControllate);
                        for(AreaInteresse citta : cittaControllate){
                            citySelector.getItems().add(citta.getNome());
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                }


            }
        }

        );





    }


    /*
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
*/

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }



    public void inserisciDati(ActionEvent e){

        if(citySelector.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore nell'inserimento dei dati");
            alert.setContentText("Seleziona una città dalla lista");
            alert.showAndWait();
            return;
        }

        if(scoreTemperatura.getText().isEmpty() || scorePrecipitazioni.getText().isEmpty() || scoreAltitudine.getText().isEmpty() || scorePressione.getText().isEmpty() || scoreUmidita.getText().isEmpty() || scoreMassa.getText().isEmpty() || scoreVento.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore nell'inserimento dei dati");
            alert.setContentText("Compila tutti i campi");
            alert.showAndWait();
            return;
        }

        try {
            out.writeObject("insertClimateParameters");
            out.writeObject(centroMonitoraggio.getId());
            out.writeObject(getIdFromSelectedCity());
            out.writeObject(Integer.parseInt(scoreTemperatura.getText()));
            out.writeObject(Integer.parseInt(scoreUmidita.getText()));
            out.writeObject(Integer.parseInt(scorePressione.getText()));
            out.writeObject(Integer.parseInt(scorePrecipitazioni.getText()));
            out.writeObject(Integer.parseInt(scoreAltitudine.getText()));

            out.writeObject(Integer.parseInt(scoreMassa.getText()));
            out.writeObject(Integer.parseInt(scoreVento.getText()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText("Dati inseriti correttamente");
            alert.showAndWait();
            clearFields();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void clearFields(){
        scoreTemperatura.clear();
        scorePrecipitazioni.clear();
        scoreAltitudine.clear();
        scoreMassa.clear();
        scorePressione.clear();
        scoreUmidita.clear();
        scoreVento.clear();
        citySelector.getSelectionModel().clearSelection();
    }

    private int getIdFromSelectedCity() {

        String nomeCitta = citySelector.getValue().toString();
        
        for(AreaInteresse citta : centroMonitoraggio.getAreeInteresse()){
            if(citta.getNome().equals(nomeCitta)){
                System.out.println("ID della citta sellezionata: " + citta.getId());
                return citta.getId();
            }
        }

        return -1;
        
    }
}

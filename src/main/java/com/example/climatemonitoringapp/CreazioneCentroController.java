package com.example.climatemonitoringapp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Questa classe gestisce la creazione di un nuovo centro di monitoraggio
 *
 */
public class CreazioneCentroController {
    @FXML
    public MenuButton menuButton;
    @FXML
    private Button creaCentroButton;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField viaField;
    @FXML
    private TextField capField;
    @FXML
    private TextField provinciaField;
    @FXML
    private TextField numCivicoField;
    @FXML
    private TextField comuneField;

    private List<AreaInteresse> areeInteresseSelezionate = new ArrayList<>();

    private User currentUser;


    private Stage stage;
    private Scene scene;
    private Parent root;


    /**
     * Con questo metodo andiamo a prendere le aree di interesse e inserirle nella combobox che verrà visualizzata
     * nella schermata di creazione di un nuovo centro di monitoraggio
     */
    public void initialize() {

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/AreeInteresse.csv"))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                System.out.println(values[0]);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < records.size(); i++) {
            CheckBox cb = new CheckBox(records.get(i).get(0));
            CustomMenuItem item = new CustomMenuItem(cb);
            item.setHideOnClick(false);
            cb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (cb.isSelected()) {
                        System.out.println(cb.getText()+" Added");
                        areeInteresseSelezionate.add(new AreaInteresse(cb.getText()));
                        stampaAreeInteresseSelezionate(areeInteresseSelezionate);
                    } else {
                        System.out.println(cb.getText()+" Removed");
                        rimuoviAreaInteresseSelezionata(cb.getText(), areeInteresseSelezionate);
                        stampaAreeInteresseSelezionate(areeInteresseSelezionate);
                    }
                }
            });
            menuButton.getItems().add(item);
        }
        System.out.println(menuButton.getItems());


    }



    public void rimuoviAreaInteresseSelezionata(String areaInteresseSelezionata,
                                                List<AreaInteresse> areeInteresseSelezionate){
        for(int i=0; i<areeInteresseSelezionate.size(); i++){
            if(areeInteresseSelezionate.get(i).getNome().equals(areaInteresseSelezionata)){
                areeInteresseSelezionate.remove(i);
            }
        }
    }

    public void stampaAreeInteresseSelezionate(List<AreaInteresse> areeInteresseSelezionate){
        for(int i=0; i<areeInteresseSelezionate.size(); i++){
            System.out.println(areeInteresseSelezionate.get(i).getNome());
        }
    }

    /**
     * Questo metodo aggiunge un nuovo centro di monitoraggio al file csv tramite l'ausilio del metodo
     * listCSVConverted() che converte i dati inseriti dall'utente in una stringa
     */
    public void creazioneCentro(ActionEvent e){
        String csvFilePath = "src/main/resources/centroMonitoraggio.dati.csv";



        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {

               writer.write(listCSVConverted());
                writer.newLine();

            System.out.println("CSV file written successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        /*
        try {
            Files.write(Paths.get("src/main/resources/centroMonitoraggio.dati.txt"), (
                    currentUser.getUsername()+
                    "\n"+nomeField.getText()+
                    "\n"+viaField.getText()+
                    "\n"+numCivicoField.getText()+
                    "\n"+comuneField.getText()+
                    "\n"+provinciaField.getText()+
                    "\n"+capField.getText() + "\n")
                    .getBytes(), StandardOpenOption.APPEND);

            Path filePath = Paths.get("src/main/resources/centroMonitoraggio.dati.txt");
            for (AreaInteresse ai : areeInteresseSelezionate) {
                Files.writeString(filePath, ai.getNome() + "\n",
                        StandardOpenOption.APPEND);
            }
            //Files.write(Paths.get("src/main/resources/centroMonitoraggio.dati.txt"), (areeInteresseSelezionate.toString()).getBytes(), StandardOpenOption.APPEND);
        }catch (IOException ex) {

        }

        */
    }


    public String listCSVConverted(){
        StringBuilder csvData = new StringBuilder();

        csvData.append(currentUser.getUsername());
        csvData.append(",");
        csvData.append(nomeField.getText());
        csvData.append(",");
        csvData.append(viaField.getText());
        csvData.append(",");
        csvData.append(numCivicoField.getText());
        csvData.append(",");
        csvData.append(comuneField.getText());
        csvData.append(",");
        csvData.append(provinciaField.getText());
        csvData.append(",");
        csvData.append(capField.getText());
        csvData.append(",");

        for (AreaInteresse ai : areeInteresseSelezionate) {
            csvData.append(ai.getNome());
            csvData.append(",");
        }

        String csvContent = csvData.toString();
        return csvContent;

    }

    public void setLoggedUser(User user) {
        this.currentUser = user;
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


}

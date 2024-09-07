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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private TextField emailField;
    @FXML
    private TextField CognomeField;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField codFiscField;
    
    @FXML
    private AnchorPane pannelloAncora;
    
    

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

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
    
    
    
    public void insertCredentials(ActionEvent e){
        String nome = nomeField.getText();
        String cognome = CognomeField.getText();
        String cf = codFiscField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
       
        try {
			out.writeObject("registerOperator");
			out.writeObject(nome);
	        out.writeObject(cognome);
	        out.writeObject(cf);
	        out.writeObject(email);
	        out.writeObject(password);

            if(in.readObject().equals("Operator registered")) {
            	System.out.println("Operatore registrato");
                Label label = new Label("Operatore registrato con successo!");
                label.setTextFill(javafx.scene.paint.Color.GREEN);
                pannelloAncora.getChildren().add(label);
                label.setMaxWidth(Double.MAX_VALUE);
                AnchorPane.setLeftAnchor(label, 0.0);
                AnchorPane.setRightAnchor(label, 0.0);
                label.setAlignment(Pos.CENTER);
            }else {
                	System.out.println("Errore nella registrazione");
                    Label label = new Label("Errore nella registrazione");
                    label.setTextFill(javafx.scene.paint.Color.RED);
                    pannelloAncora.getChildren().add(label);
                    label.setMaxWidth(Double.MAX_VALUE);
                    AnchorPane.setLeftAnchor(label, 0.0);
                    AnchorPane.setRightAnchor(label, 0.0);
                    label.setAlignment(Pos.CENTER);
            }
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
        
        LoginOperatoreController controller = loader.getController();

        controller.setConnectionSocket(socket, in, out);

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
    }
    
    public void setConnectionSocket(Socket socket, ObjectOutputStream out, ObjectInputStream in){
        this.socket= socket;
        this.in = in;
		this.out=out;        
    }
}

module com.example.climatemonitoringapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.climatemonitoringapp to javafx.fxml;
    exports com.example.climatemonitoringapp;
}
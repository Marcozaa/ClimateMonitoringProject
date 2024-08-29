module com.example.climatemonitoringapp {
    requires javafx.controls;
    requires javafx.fxml;
    //requires AnimateFX;
    requires java.sql;


    opens com.example.climatemonitoringapp to javafx.fxml;
    exports com.example.climatemonitoringapp;
}
module com.example.climatemonitoringapp {
    requires javafx.controls;
    requires javafx.fxml;
    //requires AnimateFX;
    requires java.sql;
	requires javafx.graphics;

    opens com.example.climatemonitoringapp to javafx.fxml, javafx.controls;
    exports com.example.climatemonitoringapp;
}
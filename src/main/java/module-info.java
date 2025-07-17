module com.example.databaselab2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.databaselab2 to javafx.fxml;
    exports com.example.databaselab2;
}
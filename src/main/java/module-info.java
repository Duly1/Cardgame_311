module com.example.csc311_24game {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.csc311_24game to javafx.fxml;
    exports com.example.csc311_24game;
}
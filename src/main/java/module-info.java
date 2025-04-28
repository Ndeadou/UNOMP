module com.example.uno {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.uno to javafx.fxml;
    exports com.example.uno;
    exports com.example.uno.Controller;
    opens com.example.uno.Controller to javafx.fxml;
}
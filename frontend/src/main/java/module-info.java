module com.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires java.sql;

    opens com.example.frontend.models to com.google.gson;

    exports com.example.frontend;
}

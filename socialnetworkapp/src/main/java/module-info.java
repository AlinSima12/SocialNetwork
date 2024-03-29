module com.example.socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;


    opens com.example.socialnetwork.gui to javafx.fxml;
    exports com.example.socialnetwork.gui;

    opens com.example.socialnetwork.domain to javafx.base;
    exports com.example.socialnetwork.domain;
}
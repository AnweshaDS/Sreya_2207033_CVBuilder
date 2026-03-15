module com.example.sreya_2207033_cvbuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires transitive java.sql;

    opens com.example.sreya_2207033_cvbuilder to javafx.fxml;
    exports com.example.sreya_2207033_cvbuilder;
    opens com.example.sreya_2207033_cvbuilder.database to java.sql;
    exports com.example.sreya_2207033_cvbuilder.database;
    exports com.example.sreya_2207033_cvbuilder.model;

}
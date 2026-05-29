module com.example.maptest2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
//    requires com.example.maptest2;
//    requires javafx.base;

    opens com.example.maptest2 to javafx.fxml;
    exports com.example.maptest2;
}
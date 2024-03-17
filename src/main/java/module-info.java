module it.polimi.ingsw.am11 {
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
    requires org.jetbrains.annotations;
    requires com.google.common;

    requires lombok;
    requires java.sql;

    opens it.polimi.ingsw.am11 to javafx.fxml;
    exports it.polimi.ingsw.am11;
}
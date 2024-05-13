@SuppressWarnings("JavaModuleNaming")
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

    requires java.sql;
    requires java.rmi;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens it.polimi.ingsw.am11 to javafx.fxml;
    exports it.polimi.ingsw.am11;

    opens it.polimi.ingsw.am11.view.client.GUI.window to javafx.fxml;
    exports it.polimi.ingsw.am11.view.client.GUI.window;
    exports it.polimi.ingsw.am11.view.client.GUI.utils;
    opens it.polimi.ingsw.am11.view.client.GUI.utils to javafx.fxml;

    exports it.polimi.ingsw.am11.network.RMI.Client to java.rmi;
    exports it.polimi.ingsw.am11.network.RMI.RemoteInterfaces to java.rmi;
    exports it.polimi.ingsw.am11.network.RMI.Server to java.rmi;
    exports it.polimi.ingsw.am11.network.RMI.Chat to java.rmi;

}

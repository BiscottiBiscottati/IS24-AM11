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
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires org.slf4j;
    requires org.fusesource.jansi;
    requires ch.qos.logback.classic;
    requires java.smartcardio;
    requires jdk.net;

    opens it.polimi.ingsw.am11 to javafx.fxml;
    exports it.polimi.ingsw.am11;

    opens it.polimi.ingsw.am11.view.client.GUI.window to javafx.fxml;
    exports it.polimi.ingsw.am11.view.client.GUI to javafx.graphics;
    exports it.polimi.ingsw.am11.view.client.GUI.window;
    exports it.polimi.ingsw.am11.view.client.GUI.utils;
    opens it.polimi.ingsw.am11.view.client.GUI.utils to javafx.fxml;

    exports it.polimi.ingsw.am11.network.RMI.client;
    exports it.polimi.ingsw.am11.network.RMI.remote;
    exports it.polimi.ingsw.am11.network.RMI.server;
    exports it.polimi.ingsw.am11.network.RMI.client.chat;
    exports it.polimi.ingsw.am11.network.RMI.client.game;
    exports it.polimi.ingsw.am11.network.RMI.server.chat;
    exports it.polimi.ingsw.am11.network.RMI.server.game;
    exports it.polimi.ingsw.am11.network.RMI.remote.chat;
    exports it.polimi.ingsw.am11.network.RMI.remote.game;
    exports it.polimi.ingsw.am11.network.RMI.remote.heartbeat;
    exports it.polimi.ingsw.am11.view.client.GUI.windows1;
    opens it.polimi.ingsw.am11.view.client.GUI.windows1 to javafx.fxml;

    exports it.polimi.ingsw.am11.persistence.memento to com.fasterxml.jackson.databind;

}

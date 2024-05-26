package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class NetworkPage {

    public void createNetworkPage(Font font, int halfButtonSize,
                                  Label connectionType, Label connectionFailed,
                                  TextField ipAddress, TextField port) {
        ipAddress.setAlignment(Pos.CENTER);
        ipAddress.setPromptText("Ip Address");
        ipAddress.setFont(font);
        ipAddress.setStyle("-fx-background-color: #D7BC49; " +
                           "-fx-background-radius: 5;" +
                           " -fx-max-width: " + 20 * halfButtonSize);

        port.setAlignment(Pos.CENTER);
        port.setPromptText("Port");
        port.setFont(font);
        port.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                      " -fx-max-width: " + 20 * halfButtonSize);


        connectionType.setBackground(Background.EMPTY);
        connectionType.setFont(font);
        connectionType.setTranslateY(- 6 * halfButtonSize);

        // Label: connection failed
        connectionFailed.setBackground(Background.EMPTY);
        connectionFailed.setTranslateY(6 * halfButtonSize);
        connectionFailed.setText("Server not found");
        connectionFailed.setTextFill(Color.RED);
        connectionFailed.setVisible(false);
        Label nameAlreadyTaken = new Label();

        connectionType.setVisible(false);
        connectionFailed.setVisible(false);

    }
}

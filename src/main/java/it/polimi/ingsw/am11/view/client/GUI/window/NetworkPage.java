package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class NetworkPage {

    public void createNetworkPage(Font font, int halfButtonSize, List<Label> labels,
                                  List<Button> buttonList, List<TextField> textFields,
                                  GuiActuator guiActuator, VBox theBox) {

        TextField ipAddress = textFields.get(0);
        TextField port = textFields.get(1);

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

        Label connectionType = labels.get(0);
        Label connectionFailed = labels.get(1);

        connectionType.setBackground(Background.EMPTY);
        connectionType.setFont(font);
        connectionType.setTranslateY(- 6 * halfButtonSize);

        // Label: connection failed
        connectionFailed.setBackground(Background.EMPTY);
        connectionFailed.setTranslateY(6 * halfButtonSize);
        connectionFailed.setText("Server not found");
        connectionFailed.setTextFill(Color.RED);
        connectionFailed.setVisible(false);

        connectionType.setVisible(false);
        connectionFailed.setVisible(false);

        Button chooseSocket = buttonList.get(0);
        Button chooseRMI = buttonList.get(1);
        Button joinButton = buttonList.get(2);

        // Socket, Rmi and Join buttons
        chooseSocket.setOnAction(event -> {
            connectionType.setText("Socket");
        });

        chooseRMI.setOnAction(event -> {
            connectionType.setText("Rmi");
        });

        Button enterNumOfPlayers = buttonList.get(3);
        Button goToNetwork = buttonList.get(4);
        Button chooseNick = buttonList.get(5);
        TextField writeNick = textFields.get(2);
        Label yourName = labels.get(2);

        joinButton.setOnAction(event -> {
            String connectionTypeText = connectionType.getText().toLowerCase();
            String ip = ipAddress.getCharacters().toString();
            int portNumber = Integer.parseInt(port.getCharacters().toString());
            try {
                guiActuator.connect(connectionTypeText, ip, portNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //TODO: handle exception
            if (ipAddress.getCharacters().toString().equals("Fail")) {
                connectionFailed.setVisible(true);
            } else {
                chooseRMI.setVisible(false);
                chooseSocket.setVisible(false);
                theBox.setVisible(false);
                connectionFailed.setVisible(false);
                connectionType.setVisible(false);
                joinButton.setVisible(false);
                enterNumOfPlayers.setVisible(false);

                writeNick.setVisible(true);
                goToNetwork.setVisible(true);
                chooseNick.setVisible(true);
                yourName.setVisible(true);
            }
        });


    }
}

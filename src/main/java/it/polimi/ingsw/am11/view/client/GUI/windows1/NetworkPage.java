package it.polimi.ingsw.am11.view.client.GUI.windows1;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis1;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class NetworkPage {
    private final CodexNaturalis1 codexNaturalis;
    TextField ipAddress, port;
    Font font;
    int halfButtonSize;
    Label connectionType, connectionFailed;
    Button chooseSocket, chooseRMI, joinButton;
    GuiActuator guiActuator;
    VBox theBox;

    public NetworkPage(CodexNaturalis1 codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createNetworkPage() {
        int halfButtonSize = codexNaturalis.getHalfButtonSize();
        StackPane root = codexNaturalis.getRoot();
        font = codexNaturalis.getFont();
        halfButtonSize = codexNaturalis.getHalfButtonSize();
        guiActuator = codexNaturalis.getGuiActuator();

        theBox = new VBox(2 * halfButtonSize);
        theBox.setAlignment(Pos.CENTER);

        ipAddress = new TextField();
        TextField port = new TextField();
        theBox.getChildren().addAll(ipAddress, port);
        theBox.setVisible(false);
        root.getChildren().addAll(theBox);

        // Label: socket or rmi
        connectionType = new Label();
        connectionFailed = new Label();
        root.getChildren().addAll(connectionFailed, connectionType);

        TextField writeNick = new TextField();
        Label yourName = new Label("Your Name:");
        Label nameAlreadyTaken = new Label();

        root.getChildren().addAll(yourName, writeNick, nameAlreadyTaken);
        chooseSocket = new Button("_Cancel");
        chooseRMI = new Button("_Cancel");
        joinButton = new Button("_Cancel");
        root.getChildren().addAll(chooseRMI, chooseSocket, joinButton);
    }

    public void showNetworkPage() {
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

        connectionType.setVisible(false);
        connectionFailed.setVisible(false);

        // Socket, Rmi and Join buttonsList
        chooseSocket.setOnAction(event -> {
            connectionType.setText("Socket");
        });

        chooseRMI.setOnAction(event -> {
            connectionType.setText("Rmi");
        });

        joinButton.setOnMouseClicked(event -> {
            String connectionTypeText = connectionType.getText().toLowerCase();
            String ip = ipAddress.getCharacters().toString();
            int portNumber;
            try {
                portNumber = Integer.parseInt(port.getCharacters().toString());
                try {
                    guiActuator.connect(connectionTypeText, ip, portNumber);
                    chooseRMI.setVisible(false);
                    chooseSocket.setVisible(false);
                    theBox.setVisible(false);
                    connectionFailed.setVisible(false);
                    connectionType.setVisible(false);
                    joinButton.setVisible(false);

                    //TODO: go to settingNickPage

                } catch (Exception e) {
                    ipAddress.setText("Fail");
                    connectionFailed.setVisible(true);
                }
            } catch (NumberFormatException e) {
                port.setText("Fail");
                connectionFailed.setVisible(true);
            }
        });
    }
}

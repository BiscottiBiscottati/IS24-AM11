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
    int halfButtonSize, distanceToBorder;
    Label connectionType, connectionFailed;
    Button chooseSocket, chooseRMI, joinButton;
    GuiActuator guiActuator;
    VBox theBox;

    public NetworkPage(CodexNaturalis1 codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createNetworkPage() {
        StackPane root = codexNaturalis.getRoot();
        font = codexNaturalis.getFont();
        halfButtonSize = codexNaturalis.getHalfButtonSize();
        guiActuator = codexNaturalis.getGuiActuator();
        distanceToBorder = codexNaturalis.getDistanceToBorder();

        theBox = new VBox(2 * halfButtonSize);
        theBox.setAlignment(Pos.CENTER);

        ipAddress = new TextField();
        port = new TextField();

        ipAddress.setAlignment(Pos.CENTER);
        ipAddress.setPromptText("localhost");
        ipAddress.setFont(font);
        ipAddress.setStyle("-fx-background-color: #D7BC49; " +
                           "-fx-background-radius: 5;" +
                           " -fx-max-width: " + 20 * halfButtonSize);

        port.setAlignment(Pos.CENTER);
        port.setPromptText("12345");
        port.setFont(font);
        port.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                      " -fx-max-width: " + 20 * halfButtonSize);

        theBox.getChildren().addAll(ipAddress, port);
        root.getChildren().addAll(theBox);

        // Label: socket or rmi
        connectionType = new Label();
        connectionFailed = new Label();

        connectionType.setBackground(Background.EMPTY);
        connectionType.setFont(font);
        connectionType.setTranslateY(- 6 * halfButtonSize);

        // Label: connection failed
        connectionFailed.setBackground(Background.EMPTY);
        connectionFailed.setTranslateY(6 * halfButtonSize);
        connectionFailed.setText("Server not found");
        connectionFailed.setTextFill(Color.RED);
        connectionFailed.setVisible(false);

        root.getChildren().addAll(connectionFailed, connectionType);

        chooseSocket = new Button("_Cancel");
        chooseRMI = new Button("_Cancel");
        joinButton = new Button("_Cancel");

        chooseSocket.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        chooseSocket.setTranslateX(- 5 * halfButtonSize);
        chooseSocket.setTranslateY(- 10 * halfButtonSize);
        chooseSocket.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chooseSocket.setText("Socket");
        chooseSocket.setFont(font);
        chooseSocket.setTextFill(Color.web("#351F17"));
        chooseSocket.setOnMousePressed(
                event -> chooseSocket.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chooseSocket.setOnMouseReleased(event -> chooseSocket.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        chooseRMI.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        chooseRMI.setTranslateX(5 * halfButtonSize);
        chooseRMI.setTranslateY(- 10 * halfButtonSize);
        chooseRMI.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chooseRMI.setText("Rmi");
        chooseRMI.setFont(font);
        chooseRMI.setTextFill(Color.web("#351F17"));
        chooseRMI.setOnMousePressed(
                event -> chooseRMI.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chooseRMI.setOnMouseReleased(event -> chooseRMI.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        joinButton.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        joinButton.setTranslateY(10 * halfButtonSize - distanceToBorder);
        joinButton.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        joinButton.setText("Join");
        joinButton.setFont(font);
        joinButton.setTextFill(Color.web("#351F17"));
        joinButton.setOnMousePressed(
                event -> joinButton.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        joinButton.setOnMouseReleased(event -> joinButton.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        root.getChildren().addAll(chooseRMI, chooseSocket, joinButton);

        joinButton.setVisible(false);
        chooseRMI.setVisible(false);
        chooseSocket.setVisible(false);
        ipAddress.setVisible(false);
        port.setVisible(false);
        connectionType.setVisible(false);
        connectionFailed.setVisible(false);
        theBox.setVisible(false);
    }

    public void showNetworkPage() {

        connectionType.setVisible(true);
        ipAddress.setVisible(true);
        port.setVisible(true);
        joinButton.setVisible(true);
        chooseRMI.setVisible(true);
        chooseSocket.setVisible(true);
        theBox.setVisible(true);

        // Socket, Rmi and Join buttonsList
        chooseSocket.setOnAction(event -> {
            connectionType.setText("Socket");
            ipAddress.setText("localhost");
            port.setText("12345");
        });

        chooseRMI.setOnAction(event -> {
            connectionType.setText("Rmi");
            ipAddress.setText("localhost");
            port.setText("54321");
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

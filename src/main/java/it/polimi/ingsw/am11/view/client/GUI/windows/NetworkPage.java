package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontManager;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontsEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.DISTANCE_TO_BORDER;
import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.HALF_BUTTON_SIZE;

/**
 * The NetworkPage class is responsible for creating and managing the Network Page in the GUI.
 */
public class NetworkPage {
    private static CodexNaturalis codexNaturalis;
    private static TextField ipAddress, port;
    private static Font font;
    private static int halfButtonSize, distanceToBorder;
    private static Label connectionType, connectionFailed;
    private static Button chooseSocket, chooseRMI, joinButton;
    private static GuiActuator guiActuator;
    private static VBox theBox;

    /**
     * This static method is used to create the Network Page in the GUI. It initializes the
     * necessary components and sets their properties.
     *
     * @param codexNaturalis The GUI instance that the Network Page is a part of.
     */
    public static void createNetworkPage(CodexNaturalis codexNaturalis) {
        NetworkPage.codexNaturalis = codexNaturalis;

        StackPane root = codexNaturalis.getSmallRoot();
        font = FontManager.getFont(FontsEnum.CLOISTER_BLACK, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));

        halfButtonSize = HALF_BUTTON_SIZE.getValue();
        guiActuator = codexNaturalis.getGuiActuator();
        distanceToBorder = DISTANCE_TO_BORDER.getValue();

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

    /**
     * This static method is used to show the Network Page in the GUI. It sets the visibility of the
     * Network Page to true.
     */
    public static void showNetworkPage() {

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
            port.requestFocus();
        });

        chooseRMI.setOnAction(event -> {
            connectionType.setText("Rmi");
            ipAddress.setText("localhost");
            port.setText("54321");
            port.requestFocus();
        });

        joinButton.setOnMouseClicked(event -> handleJoinButton());

        port.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleJoinButton();
            }
        });

        ipAddress.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleJoinButton();
            }
        });
    }

    /**
     * This static method is used to handle the event of clicking the Join button. It checks if the
     * connection type, IP address and port number are valid and navigates to the Set Nick Page if
     * they are.
     */
    public static void handleJoinButton() {
        String connectionTypeText = connectionType.getText().toLowerCase();
        String ip = ipAddress.getCharacters().toString();
        int portNumber;
        try {
            portNumber = Integer.parseInt(port.getCharacters().toString());
        } catch (NumberFormatException e) {
            port.setText("Fail");
            connectionFailed.setVisible(true);
            return;
        }
        try {
            guiActuator.connect(connectionTypeText, ip, portNumber);
            chooseRMI.setVisible(false);
            chooseSocket.setVisible(false);
            theBox.setVisible(false);
            connectionFailed.setVisible(false);
            connectionType.setVisible(false);
            joinButton.setVisible(false);

            SetNickPage.showSettingNickPage();

        } catch (Exception e) {
            ipAddress.setText("Fail");
            connectionFailed.setVisible(true);
        }
    }

}

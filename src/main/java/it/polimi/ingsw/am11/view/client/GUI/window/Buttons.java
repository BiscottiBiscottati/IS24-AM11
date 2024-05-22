package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Buttons {

    public void createButtons(Button goToNetwork, int halfButtonSize, int distanceToBorder,
                              Font font, Button chooseNick, Button chooseSocket, Button chooseRMI
            , Button joinButton, Button enterNumOfPlayers, Button goBack) {

        goToNetwork.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        goToNetwork.setTranslateX(- 5 * halfButtonSize);
        goToNetwork.setTranslateY(10 * halfButtonSize - distanceToBorder);
        goToNetwork.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        goToNetwork.setText("go Back");
        goToNetwork.setFont(font);
        goToNetwork.setTextFill(Color.web("#351F17"));
        goToNetwork.setOnMousePressed(
                event -> goToNetwork.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        goToNetwork.setOnMouseReleased(event -> goToNetwork.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        chooseNick.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        chooseNick.setTranslateX(5 * halfButtonSize);
        chooseNick.setTranslateY(10 * halfButtonSize - distanceToBorder);
        chooseNick.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chooseNick.setText("Choose");
        chooseNick.setFont(font);
        chooseNick.setTextFill(Color.web("#351F17"));
        chooseNick.setOnMousePressed(
                event -> chooseNick.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chooseNick.setOnMouseReleased(event -> chooseNick.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

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

        enterNumOfPlayers.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        enterNumOfPlayers.setTranslateX(5 * halfButtonSize);
        enterNumOfPlayers.setTranslateY(10 * halfButtonSize - distanceToBorder);
        enterNumOfPlayers.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        enterNumOfPlayers.setText("Enter");
        enterNumOfPlayers.setFont(font);
        enterNumOfPlayers.setTextFill(Color.web("#351F17"));
        enterNumOfPlayers.setOnMousePressed(
                event -> enterNumOfPlayers.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        enterNumOfPlayers.setOnMouseReleased(event -> enterNumOfPlayers.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        goBack.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        goBack.setTranslateX(- 5 * halfButtonSize);
        goBack.setTranslateY(10 * halfButtonSize - distanceToBorder);
        goBack.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        goBack.setText("Go back");
        goBack.setFont(font);
        goBack.setTextFill(Color.web("#351F17"));
        goBack.setOnMousePressed(
                event -> goBack.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        goBack.setOnMouseReleased(event -> goBack.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));


        chooseNick.setVisible(false);
        goToNetwork.setVisible(false);
        chooseRMI.setVisible(false);
        chooseSocket.setVisible(false);
        joinButton.setVisible(false);
        enterNumOfPlayers.setVisible(false);
        goBack.setVisible(false);
    }
}

package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Font;

public class WaitingRoom {

    public void createWaitingRoom(Label waitingForPlayers, ProgressIndicator loadingWheel,
                                  Font font) {

        waitingForPlayers.setTranslateY(- 60);
        waitingForPlayers.setTranslateX(80);
        waitingForPlayers.setPrefSize(400, 50);
        waitingForPlayers.setFont(font);
        waitingForPlayers.setStyle("-fx-font-size: 30");
        waitingForPlayers.setStyle("-fx-text-fill: #D7BC49");
        waitingForPlayers.setStyle("-fx-background-color: #351F17");
        waitingForPlayers.setStyle("-fx-background-radius: 5");
        waitingForPlayers.setVisible(false);

        loadingWheel.setTranslateY(0);
        loadingWheel.setTranslateX(0);
        loadingWheel.setStyle("-fx-border-color: #D7BC49");
        loadingWheel.setStyle("-fx-background-color: #351F17");
        loadingWheel.setStyle("-fx-background-radius: 5");
        loadingWheel.setVisible(false);
    }
}

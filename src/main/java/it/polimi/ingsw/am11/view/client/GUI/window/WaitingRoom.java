package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Font;

public class WaitingRoom {

    private ProgressBar progressBar;

    public void createWaitingRoom(Label waitingForPlayers, ProgressBar progressBar, Font font) {

        waitingForPlayers.setTranslateY(- 60);
        waitingForPlayers.setTranslateX(80);
        waitingForPlayers.setPrefSize(400, 50);
        waitingForPlayers.setFont(font);
        waitingForPlayers.setStyle("-fx-font-size: 30");
        waitingForPlayers.setStyle("-fx-text-fill: #D7BC49");
        waitingForPlayers.setStyle("-fx-background-color: #351F17");
        waitingForPlayers.setStyle("-fx-background-radius: 5");
        waitingForPlayers.setVisible(false);
        progressBar.setProgress(0);
        progressBar.setPrefSize(300, 50);
        progressBar.setStyle("-fx-accent: #D7BC49");
        progressBar.setStyle("-fx-background-color: #351F17");
        progressBar.setStyle("-fx-background-radius: 5");
        progressBar.setVisible(false);
        this.progressBar = progressBar;
    }

    public void updateProgressBar(int currentPlayers, int totalPlayers) {
        double progress = (double) currentPlayers / totalPlayers;
        progressBar.setProgress(progress);
    }
}

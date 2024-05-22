package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javax.swing.*;

public class WaitingRoom {

    private ProgressBar progressBar;

    public void createWaitingRoom(Label waitingForPlayers, ProgressBar progressBar) {
        waitingForPlayers.setTranslateY(- 6 * 50);
        waitingForPlayers.setText("Waiting for players...");
        waitingForPlayers.setStyle("-fx-font-size: 30");
        waitingForPlayers.setStyle("-fx-text-fill: #D7BC49");
        waitingForPlayers.setStyle("-fx-background-color: #351F17");
        waitingForPlayers.setStyle("-fx-background-radius: 5");
        waitingForPlayers.setVisible(false);

        progressBar.setTranslateY(6 * 50);
        progressBar.setProgress(0);
        progressBar.setPrefSize(400, 50);
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

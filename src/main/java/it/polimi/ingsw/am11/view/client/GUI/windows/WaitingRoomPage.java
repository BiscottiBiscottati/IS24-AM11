package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class WaitingRoomPage {
    private final CodexNaturalis codexNaturalis;
    ProgressIndicator loadingWheel;
    StackPane root;
    Font font;
    Label waitingForPlayers;

    public WaitingRoomPage(CodexNaturalis codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createWaitingRoomPage() {
        root = codexNaturalis.getRoot();
        font = codexNaturalis.getFont();
        waitingForPlayers = new Label("Waiting...");
        loadingWheel = new ProgressIndicator();
        root.getChildren().addAll(waitingForPlayers, loadingWheel);
        waitingForPlayers.setAlignment(Pos.CENTER);
        waitingForPlayers.setTranslateY(- 80);
        waitingForPlayers.setPrefSize(400, 50);
        waitingForPlayers.setFont(font);
        waitingForPlayers.setStyle("-fx-font-size: 30");
        waitingForPlayers.setStyle("-fx-text-fill: #D7BC49");
        waitingForPlayers.setStyle("-fx-background-color: #351F17");
        waitingForPlayers.setStyle("-fx-background-radius: 5");

        loadingWheel.setTranslateY(0);
        loadingWheel.setTranslateX(0);
        loadingWheel.setStyle("-fx-border-color: #D7BC49");
        loadingWheel.setStyle("-fx-background-color: #351F17");
        loadingWheel.setStyle("-fx-background-radius: 5");

        waitingForPlayers.setVisible(false);
        loadingWheel.setVisible(false);

        StackPane.setAlignment(waitingForPlayers, Pos.CENTER);
    }

    public void showWaitingRoomPage() {
        waitingForPlayers.setVisible(true);
        loadingWheel.setVisible(true);

    }

    public void hideWaitingRoomPage() {
        waitingForPlayers.setVisible(false);
        loadingWheel.setVisible(false);
    }
}

package it.polimi.ingsw.am11.view.client.GUI.windows1;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis1;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class WaitingRoomPage {
    private final CodexNaturalis1 codexNaturalis;
    ProgressIndicator loadingWheel;
    StackPane root;
    Font font;
    Label waitingForPlayers;

    public WaitingRoomPage(CodexNaturalis1 codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createWaitingRoomPage() {
        root = codexNaturalis.getRoot();
        font = codexNaturalis.getFont();
        waitingForPlayers = new Label("Waiting for other players to join...");
        loadingWheel = new ProgressIndicator();
        root.getChildren().addAll(waitingForPlayers, loadingWheel);
        waitingForPlayers.setTranslateY(- 60);
        waitingForPlayers.setTranslateX(80);
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
}

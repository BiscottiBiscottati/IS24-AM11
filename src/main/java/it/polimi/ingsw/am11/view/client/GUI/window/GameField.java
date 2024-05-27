package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class GameField {

    public void createGameField(Pane playerField, VBox decksContainer, HBox playerCards,
                                ListView<String> playerList, Font font) {
        playerField.setPrefSize(800, 800);
        playerField.setStyle("-fx-background-color: #351F17");
        playerField.setStyle("-fx-font-size: 30");
        playerField.setStyle("-fx-text-fill: #D7BC49");
        playerField.setStyle("-fx-background-color: #351F17");
        playerField.setStyle("-fx-background-radius: 5");
        playerField.setVisible(false);

        decksContainer.setPrefSize(800, 200);
        decksContainer.setStyle("-fx-background-color: #351F17");
        decksContainer.setStyle("-fx-font-size: 30");
        decksContainer.setStyle("-fx-text-fill: #D7BC49");
        decksContainer.setStyle("-fx-background-color: #351F17");
        decksContainer.setStyle("-fx-background-radius: 5");
        decksContainer.setVisible(false);

        playerCards.setPrefSize(200, 800);
        playerCards.setStyle("-fx-background-color: #351F17");
        playerCards.setStyle("-fx-font-size: 30");
        playerCards.setStyle("-fx-text-fill: #D7BC49");
        playerCards.setStyle("-fx-background-color: #351F17");
        playerCards.setStyle("-fx-background-radius: 5");
        playerCards.setVisible(false);

        playerList.setPrefSize(200, 800);
        playerList.setStyle("-fx-font-style: italic");
        playerList.setStyle("-fx-background-color: #351F17");
        playerList.setStyle("-fx-font-size: 30");
        playerList.setStyle("-fx-text-fill: #D7BC49");
        playerList.setStyle("-fx-background-color: #351F17");
        playerList.setStyle("-fx-background-radius: 5");
        playerList.setVisible(false);


    }
}

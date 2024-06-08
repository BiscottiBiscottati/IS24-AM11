package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.io.IOException;

public class GamePage {
    Parent root1;

    @FXML
    Label decksLabel;

    public GamePage() throws IOException {
    }

    public void createGamePage(CodexNaturalis codexNaturalis) throws IOException {

        Font font = codexNaturalis.getFont();

        decksLabel.setFont(font);
        decksLabel.setStyle("-fx-font-size: 30");
        decksLabel.setStyle("-fx-text-fill: #D7BC49");
        decksLabel.setStyle("-fx-background-color: #351F17");
        decksLabel.setStyle("-fx-background-radius: 5");
    }

    public void showGamePage() {
        root1.setVisible(true);
    }
}

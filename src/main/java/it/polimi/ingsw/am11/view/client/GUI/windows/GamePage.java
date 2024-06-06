package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Objects;

public class GamePage {
    private final CodexNaturalis codexNaturalis;
    Parent root1 = FXMLLoader.load(
            Objects.requireNonNull(getClass().getResource("/fxml/gamePage.fxml")));
    @FXML
    Label decksLabel;
    ImageView goldDeck;
    ImageView resourceDeck;

    public GamePage(CodexNaturalis codexNaturalis) throws IOException {
        this.codexNaturalis = codexNaturalis;
    }

    public void createGamePage() throws IOException {

        Font font = codexNaturalis.getFont();

        decksLabel.setFont(font);
        decksLabel.setStyle("-fx-font-size: 30");
        decksLabel.setStyle("-fx-text-fill: #D7BC49");
        decksLabel.setStyle("-fx-background-color: #351F17");
        decksLabel.setStyle("-fx-background-radius: 5");


        StackPane stackPane = codexNaturalis.getRoot();
        stackPane.getChildren().add(root1);
    }

    public void showGamePage() {
    }
}

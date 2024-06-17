package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.io.IOException;

public class GamePage {

    @FXML
    Label decksLabel;
    @FXML
    StackPane gamePage;
    @FXML
    ImageView GPBackground;

    public GamePage() throws IOException {
    }

    public static void showGamePage(Parent root1) {
        root1.setVisible(true);
    }

    public void createGamePage(CodexNaturalis codexNaturalis) throws IOException {

        Font font = codexNaturalis.getFont();
        GuiResources guiResources = codexNaturalis.getGuiResources();

        GPBackground = guiResources.getTheImageView(GuiResEnum.GAME_BACKGROUND);

        decksLabel.setFont(font);
        decksLabel.setStyle("-fx-font-size: 30");
        decksLabel.setStyle("-fx-text-fill: #D7BC49");
        decksLabel.setStyle("-fx-background-color: #351F17");
        decksLabel.setStyle("-fx-background-radius: 5");


    }
}

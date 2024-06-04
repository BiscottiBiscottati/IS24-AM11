package it.polimi.ingsw.am11.view.client.GUI.windows1;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis1;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class SetStarterCardsPage {
    private final CodexNaturalis1 codexNaturalis;
    StackPane root;
    GuiResources guiResources;
    GuiActuator guiActuator;
    Label message;
    ImageView cardImage, cardRetro;
    VBox layout;
    Font font;
    int halfButtonSize;

    public SetStarterCardsPage(CodexNaturalis1 codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }


    public void createStarterCardsPage(int cardId) {
        root = codexNaturalis.getRoot();
        font = codexNaturalis.getFont();
        halfButtonSize = codexNaturalis.getHalfButtonSize();
        guiResources = codexNaturalis.getGuiResources();
        guiActuator = codexNaturalis.getGuiActuator();
        message = new Label("This is your starter card:");
        message.setFont(font);
        message.setAlignment(Pos.CENTER);
        message.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                         " -fx-max-width: " + 20 * halfButtonSize);
        cardImage = guiResources.getCardImage(cardId);
        cardImage.setFitHeight(200);
        cardImage.setFitWidth(100);

        cardRetro = guiResources.getCardImageRetro(cardId);
        cardRetro.setFitHeight(200);
        cardRetro.setFitWidth(100);

        layout = new VBox(10);
        layout.getChildren().addAll(message, cardImage, cardRetro);
        layout.setAlignment(Pos.CENTER);

        root.getChildren().add(layout);

        layout.setVisible(false);
    }

    public void showStarterCardsPage() {
        message.setVisible(true);
        cardImage.setVisible(true);
        layout.setVisible(true);
        cardRetro.setVisible(true);

        cardImage.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            guiActuator.setStarterCard(false);
            codexNaturalis.showWaitingRoomPage();
        });

        cardRetro.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            guiActuator.setStarterCard(true);
            codexNaturalis.showWaitingRoomPage();
        });


    }
}

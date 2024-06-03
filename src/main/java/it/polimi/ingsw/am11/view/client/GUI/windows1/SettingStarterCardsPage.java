package it.polimi.ingsw.am11.view.client.GUI.windows1;

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

public class SettingStarterCardsPage {
    Label message;
    ImageView cardImage;
    ImageView cardRetro;
    VBox layout;

    public void createStarterCardsWindow(int cardId, Font font, GuiResources guiResources,
                                         int halfButtonSize, StackPane root) {
        // Create the starter cards

        message = new Label("This is your starter card:");
        message.setFont(font);
        message.setAlignment(Pos.CENTER);
        message.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                         " -fx-max-width: " + 20 * halfButtonSize);
        cardImage = guiResources.getTheImageView(GuiResEnum.getEnumByCardId(cardId));
        cardImage.setFitHeight(200);
        cardImage.setFitWidth(100);

        cardRetro = guiResources.getTheImageView(GuiResEnum.getEnumByCardIdRetro(cardId));
        cardRetro.setFitHeight(200);
        cardRetro.setFitWidth(100);

        layout = new VBox(10);
        layout.getChildren().addAll(message, cardImage);
        layout.setAlignment(Pos.CENTER);

        root.getChildren().add(layout);

        layout.setVisible(false);
    }

    public void showStarterCardsWindow(ProgressIndicator loadingwheel, List<Label> labels) {
        loadingwheel.setVisible(false);
        labels.get(6).setVisible(false);

        message.setVisible(true);
        cardImage.setVisible(true);
        layout.setVisible(true);
        cardRetro.setVisible(true);

        cardImage.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);

            //TODO send the card to the server and show SettingObjectiveCardsPage

        });

        cardRetro.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);

            //TODO send the card to the server and show SettingObjectiveCardsPage

        });


    }
}

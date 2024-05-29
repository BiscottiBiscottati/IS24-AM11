package it.polimi.ingsw.am11.view.client.GUI.window;

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

public class SettingStarterCards {

    public void createStarterCardsWindow(int cardId, Font font, GuiResources guiResources,
                                         int halfButtonSize, StackPane root,
                                         ProgressIndicator loadingwheel, List<Label> labels) {
        // Create the starter cards

        Label message = new Label("This is your starter card:");
        message.setFont(font);
        message.setAlignment(Pos.CENTER);
        message.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                         " -fx-max-width: " + 20 * halfButtonSize);
        ImageView cardImage = guiResources.getTheImageView(GuiResEnum.getEnumByCardId(cardId));
        cardImage.setFitHeight(200);
        cardImage.setFitWidth(100);

        ImageView cardRetro = guiResources.getTheImageView(GuiResEnum.getEnumByCardId(cardId));
        cardRetro.setFitHeight(200);
        cardRetro.setFitWidth(100);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(message, cardImage);
        layout.setAlignment(Pos.CENTER);

        root.getChildren().add(layout);

        Label waitingForPlayers = labels.get(6);

        layout.setVisible(true);

        waitingForPlayers.setVisible(false);
        loadingwheel.setVisible(false);

        cardImage.setOnMouseClicked(event -> {
            layout.setVisible(false);
        });

        cardRetro.setOnMouseClicked(event -> {
            layout.setVisible(false);
        });
    }
}

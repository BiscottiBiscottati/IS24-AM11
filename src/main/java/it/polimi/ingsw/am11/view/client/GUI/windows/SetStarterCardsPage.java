package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SetStarterCardsPage {
    private final CodexNaturalis codexNaturalis;
    StackPane root;
    GuiResources guiResources;
    GuiActuator guiActuator;
    Label message;
    ImageView cardImage, cardRetro;
    HBox layout;
    VBox vbox;
    Font font;
    int halfButtonSize;

    public SetStarterCardsPage(CodexNaturalis codexNaturalis) {
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
        vbox = new VBox(10);
        try {
            cardImage = guiResources.getCardImage(cardId);
            cardImage.setFitHeight(100);
            cardImage.setFitWidth(80);

            cardRetro = guiResources.getCardImageRetro(cardId);
            cardRetro.setFitHeight(100);
            cardRetro.setFitWidth(80);
        } catch (Exception e) {
            System.err.println("Error loading card image in SetStarterCardsPage");
        }

        System.out.println("Added starter card");
        try {
            message.setVisible(false);
            layout = new HBox(10);
            layout.getChildren().addAll(cardImage, cardRetro);
            layout.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(message, layout);
            vbox.setAlignment(Pos.CENTER);
            root.getChildren().addAll(vbox);
            layout.setVisible(false);
            vbox.setVisible(false);
        } catch (Exception e) {
            System.err.println("Error adding starter card to root in SetStarterCardsPage");
            e.printStackTrace();
        }
    }

    public void showStarterCardsPage() {
        message.setVisible(true);
        layout.setVisible(true);
        vbox.setVisible(true);

        cardImage.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setStarterCard(false);
            codexNaturalis.showWaitingRoomPage();
        });

        cardRetro.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setStarterCard(true);
            codexNaturalis.showWaitingRoomPage();
        });


    }
}

package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Set;

public class SetObjCardsPage {
    private final CodexNaturalis codexNaturalis;
    StackPane root;
    GuiResources guiResources;
    GuiActuator guiActuator;
    Label message;
    ImageView cardImage1, cardImage2;
    HBox layout;
    VBox vbox;
    Font font;
    int halfButtonSize;
    List<Integer> cardIdList;
    MiniGameModel miniGameModel;

    public SetObjCardsPage(CodexNaturalis codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
        this.miniGameModel = codexNaturalis.getMiniGameModel();
    }


    public void createObjCardsPage(Set<Integer> cardId) {


        root = codexNaturalis.getInitialRoot();
        font = codexNaturalis.getFont();
        halfButtonSize = codexNaturalis.getHalfButtonSize();
        guiResources = codexNaturalis.getGuiResources();
        guiActuator = codexNaturalis.getGuiActuator();
        message = new Label("Choose your objective card:");
        message.setFont(font);
        message.setAlignment(Pos.CENTER);
        message.setBackground(Background.EMPTY);
        cardIdList = List.copyOf(cardId);
        vbox = new VBox(10);
        try {
            cardImage1 = guiResources.getImageView(cardIdList.getFirst());
            cardImage1.setFitHeight(75);
            cardImage1.setFitWidth(120);

            cardImage2 = guiResources.getImageView(cardIdList.get(1));
            cardImage2.setFitHeight(75);
            cardImage2.setFitWidth(120);
        } catch (Exception e) {
            System.err.println("Error loading card image in ObjectiveCardsPage");
        }

        try {
            message.setVisible(false);
            message.setAlignment(Pos.CENTER);
            layout = new HBox(10);
            layout.getChildren().addAll(cardImage1, cardImage2);
            layout.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(message, layout);
            vbox.setAlignment(Pos.CENTER);
            root.getChildren().addAll(vbox);
            layout.setVisible(false);
            vbox.setVisible(false);
        } catch (Exception e) {
            System.err.println("Error adding starter card to root in ObjectiveCardsPage");
            e.printStackTrace();
        }
    }

    public void showObjCardsPage() {
        message.setVisible(true);
        layout.setVisible(true);
        vbox.setVisible(true);

        cardImage1.setOnMouseClicked(event -> {
            cardImage1.setVisible(false);
            cardImage2.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setPersonalObjective(cardIdList.getFirst());
            miniGameModel.addPersonalObjective(cardIdList.getFirst());
            CodexNaturalis.showWaitingRoomPage();
        });

        cardImage2.setOnMouseClicked(event -> {
            cardImage1.setVisible(false);
            cardImage2.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setPersonalObjective(cardIdList.get(1));
            miniGameModel.addPersonalObjective(cardIdList.get(1));
            CodexNaturalis.showWaitingRoomPage();
        });


    }
}

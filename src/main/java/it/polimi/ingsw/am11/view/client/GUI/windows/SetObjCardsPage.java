package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontManager;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontsEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
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

import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.HALF_BUTTON_SIZE;

public class SetObjCardsPage {
    private static GuiActuator guiActuator;
    private static Label message;
    private static ImageView cardImage1 = null;
    private static ImageView cardImage2;
    private static HBox layout;
    private static VBox vbox;
    private static MiniGameModel miniGameModel;


    public static void createObjCardsPage(CodexNaturalis codexNaturalis) {
        SetObjCardsPage.miniGameModel = codexNaturalis.getMiniGameModel();

        StackPane root = codexNaturalis.getSmallRoot();
        Font font = FontManager.getFont(FontsEnum.CLOISTER_BLACK, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));
        guiActuator = codexNaturalis.getGuiActuator();
        message = new Label("Choose your objective card:");
        message.setFont(font);
        message.setAlignment(Pos.CENTER);
        message.setBackground(Background.EMPTY);
        vbox = new VBox(10);

        message.setVisible(false);
        message.setAlignment(Pos.CENTER);
        layout = new HBox(10);

        layout.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(message, layout);
        vbox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(vbox);
        layout.setVisible(false);
        vbox.setVisible(false);
    }

    public static void showObjCardsPage() {
        List<Integer> cardIdList =
                List.copyOf(miniGameModel.getCliPlayer(
                        miniGameModel.myName()).getSpace().getCandidateObjectives());

        cardImage1 = GuiResources.getImageView(cardIdList.getFirst());
        cardImage2 = GuiResources.getImageView(cardIdList.get(1));
        cardImage1.setFitHeight(75);
        cardImage1.setFitWidth(120);

        cardImage2.setFitHeight(75);
        cardImage2.setFitWidth(120);

        layout.getChildren().addAll(cardImage1, cardImage2);

        message.setVisible(true);
        layout.setVisible(true);
        vbox.setVisible(true);
        cardImage1.setVisible(true);
        cardImage2.setVisible(true);

        cardImage1.setOnMouseClicked(event -> {
            cardImage1.setVisible(false);
            cardImage2.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setPersonalObjective(cardIdList.getFirst());
            miniGameModel.addPersonalObjective(cardIdList.getFirst());
            WaitingRoomPage.showWaitingRoomPage();
        });

        cardImage2.setOnMouseClicked(event -> {
            cardImage1.setVisible(false);
            cardImage2.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setPersonalObjective(cardIdList.get(1));
            miniGameModel.addPersonalObjective(cardIdList.get(1));
            WaitingRoomPage.showWaitingRoomPage();
        });
    }

    public static void hideObjCardsPage() {
        if (cardImage1 != null) {
            cardImage1.setVisible(false);
            cardImage2.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
        }
    }
}

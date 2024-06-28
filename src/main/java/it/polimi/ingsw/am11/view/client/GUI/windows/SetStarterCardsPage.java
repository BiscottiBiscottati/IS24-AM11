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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is used to create the page where the player can choose the starter card.
 */
public class SetStarterCardsPage {
    private static GuiActuator guiActuator;
    private static @Nullable Label message = null;
    private static @Nullable ImageView cardImage = null;
    private static @Nullable ImageView cardRetro = null;
    private static @Nullable HBox layout = null;
    private static @Nullable VBox vbox = null;
    private static MiniGameModel miniGameModel;

    /**
     * This static method is used to create the starter cards page in the GUI. It initializes the
     * necessary components and sets their properties.
     *
     * @param codexNaturalis The GUI instance that the starter cards page is a part of.
     */
    public static void createStarterCardsPage(@NotNull CodexNaturalis codexNaturalis) {
        miniGameModel = codexNaturalis.getMiniGameModel();

        StackPane root = codexNaturalis.getSmallRoot();
        Font font = FontManager.getFont(FontsEnum.CLOISTER_BLACK, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));
        guiActuator = codexNaturalis.getGuiActuator();
        message = new Label("This is your starter card:");
        message.setFont(font);
        message.setAlignment(Pos.CENTER);
        message.setBackground(Background.EMPTY);
        vbox = new VBox(10);

        message.setVisible(false);
        layout = new HBox(10);
        layout.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(message, layout);
        vbox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(vbox);
        layout.setVisible(false);
        vbox.setVisible(false);

    }

    /**
     * This static method is used to display the starter cards page in the GUI. It retrieves the
     * starter card images, sets their properties, and adds them to the layout. It also sets up the
     * event handlers for the card images, which hide the current page and navigate to the waiting
     * room page when clicked.
     */
    public static void showStarterCardsPage() {
        cardImage =
                GuiResources.getImageView(miniGameModel.getCliPlayer(
                        miniGameModel.myName()).getSpace().getStarterCard());
        cardRetro =
                GuiResources.getCardImageRetro(miniGameModel.getCliPlayer(
                        miniGameModel.myName()).getSpace().getStarterCard());
        cardImage.setFitHeight(75);
        cardImage.setFitWidth(120);

        cardRetro.setFitHeight(75);
        cardRetro.setFitWidth(120);
        assert layout != null;
        layout.getChildren().addAll(cardImage, cardRetro);

        assert message != null;
        message.setVisible(true);
        layout.setVisible(true);
        assert vbox != null;
        vbox.setVisible(true);
        cardImage.setVisible(true);
        cardRetro.setVisible(true);

        cardImage.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setStarterCard(false);
            WaitingRoomPage.showWaitingRoomPage();
        });

        cardRetro.setOnMouseClicked(event -> {
            cardImage.setVisible(false);
            cardRetro.setVisible(false);
            message.setVisible(false);
            layout.setVisible(false);
            vbox.setVisible(false);
            guiActuator.setStarterCard(true);
            WaitingRoomPage.showWaitingRoomPage();
        });
    }

    /**
     * This static method is used to hide the starter cards page in the GUI. It sets the visibility
     * of the starter cards page to false.
     */
    public static void hideStarterCardsPage() {
        if (cardImage != null) {
            cardImage.setVisible(false);
            assert cardRetro != null;
            cardRetro.setVisible(false);
            assert message != null;
            message.setVisible(false);
            assert layout != null;
            layout.setVisible(false);
            assert vbox != null;
            vbox.setVisible(false);
        }
    }
}

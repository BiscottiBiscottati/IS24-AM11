package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontManager;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontsEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * This class is used to create the waiting room page, where the player waits for the other players
 * to join the game or for the game to start
 */
public class WaitingRoomPage {
    private static CodexNaturalis codexNaturalis;
    private static ProgressIndicator loadingWheel;
    private static Label waitingForPlayers;


    /**
     * This static method is used to create the waiting room page in the GUI. It initializes the
     * necessary parts and sets their properties.
     *
     * @param codexNaturalis The GUI instance that the waiting room page is a part of.
     */
    public static void createWaitingRoomPage(CodexNaturalis codexNaturalis) {
        WaitingRoomPage.codexNaturalis = codexNaturalis;

        StackPane root = codexNaturalis.getSmallRoot();
        Font font = FontManager.getFont(FontsEnum.CLOISTER_BLACK,
                                        (int) (Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));
        waitingForPlayers = new Label("Waiting...");
        loadingWheel = new ProgressIndicator();
        root.getChildren().addAll(waitingForPlayers, loadingWheel);
        waitingForPlayers.setAlignment(Pos.CENTER);
        waitingForPlayers.setTranslateY(- 80);
        waitingForPlayers.setPrefSize(400, 50);
        waitingForPlayers.setFont(font);
        waitingForPlayers.setStyle("-fx-font-size: 30");
        waitingForPlayers.setStyle("-fx-text-fill: #D7BC49");
        waitingForPlayers.setStyle("-fx-background-color: #351F17");
        waitingForPlayers.setStyle("-fx-background-radius: 5");

        loadingWheel.setTranslateY(0);
        loadingWheel.setTranslateX(0);
        loadingWheel.setStyle("-fx-border-color: #D7BC49");
        loadingWheel.setStyle("-fx-background-color: #351F17");
        loadingWheel.setStyle("-fx-background-radius: 5");

        waitingForPlayers.setVisible(false);
        loadingWheel.setVisible(false);

        StackPane.setAlignment(waitingForPlayers, Pos.CENTER);
    }

    /**
     * This static method is used to show the waiting room page in the GUI. It sets the visibility
     * of the waiting room page to true.
     */
    public static void showWaitingRoomPage() {
        waitingForPlayers.setVisible(true);
        loadingWheel.setVisible(true);

    }

    /**
     * This static method is used to hide the waiting room page in the GUI. It sets the visibility
     * of the waiting room page to false.
     */
    public static void hideWaitingRoomPage() {
        waitingForPlayers.setVisible(false);
        loadingWheel.setVisible(false);
    }
}

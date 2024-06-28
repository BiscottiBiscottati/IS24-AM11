package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontManager;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontsEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.DISTANCE_TO_BORDER;
import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.HALF_BUTTON_SIZE;

/**
 * The SetNumOfPlayersPage class is responsible for creating and managing the Set Number of Players
 * Page in the GUI.
 */
public class SetNumOfPlayersPage {
    private static CodexNaturalis codexNaturalis;
    private static GuiActuator guiActuator;
    private static Label numOfPlayers = null;
    private static Label invalidNumOfPlayers = null;
    private static Font font;
    private static int halfButtonSize, distanceToBorder;
    private static TextField writeNumOfPlayers = null;
    private static Button enterNumOfPlayers = null;
    private static StackPane root;


    /**
     * This static method is used to create the Set Number of Players Page in the GUI. It
     * initializes the necessary components and sets their properties.
     *
     * @param codexNaturalis The GUI instance that the Set Number of Players Page is a part of.
     */
    public static void createNumOfPlayersPage(CodexNaturalis codexNaturalis) {
        SetNumOfPlayersPage.codexNaturalis = codexNaturalis;

        font = FontManager.getFont(FontsEnum.CLOISTER_BLACK, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));
        halfButtonSize = HALF_BUTTON_SIZE.getValue();
        guiActuator = codexNaturalis.getGuiActuator();
        distanceToBorder = DISTANCE_TO_BORDER.getValue();
        root = codexNaturalis.getSmallRoot();

        numOfPlayers = new Label("Number of players:");
        numOfPlayers.setBackground(Background.EMPTY);
        numOfPlayers.setFont(font);
        numOfPlayers.setTranslateY(- 4 * halfButtonSize);

        writeNumOfPlayers = new TextField();
        writeNumOfPlayers.setAlignment(Pos.CENTER);
        writeNumOfPlayers.setPromptText("Number of players");
        writeNumOfPlayers.setFont(font);
        writeNumOfPlayers.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                                   " -fx-max-width: " + 20 * halfButtonSize);

        invalidNumOfPlayers = new Label("Invalid number of players");
        invalidNumOfPlayers.setBackground(Background.EMPTY);
        invalidNumOfPlayers.setTranslateY(6 * halfButtonSize);
        invalidNumOfPlayers.setText("Invalid number of players");
        invalidNumOfPlayers.setTextFill(Color.RED);
        invalidNumOfPlayers.setVisible(false);

        numOfPlayers.setVisible(false);
        writeNumOfPlayers.setVisible(false);
        invalidNumOfPlayers.setVisible(false);

        enterNumOfPlayers = new Button();
        enterNumOfPlayers.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        enterNumOfPlayers.setTranslateY(100);
        enterNumOfPlayers.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        enterNumOfPlayers.setText("Enter");
        enterNumOfPlayers.setFont(font);
        enterNumOfPlayers.setTextFill(Color.web("#351F17"));
        enterNumOfPlayers.setOnMousePressed(
                event -> enterNumOfPlayers.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        enterNumOfPlayers.setOnMouseReleased(event -> enterNumOfPlayers.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
        enterNumOfPlayers.setVisible(false);

        root.getChildren().addAll(numOfPlayers, writeNumOfPlayers, invalidNumOfPlayers,
                                  enterNumOfPlayers);
    }

    /**
     * This static method is used to show the Set Number of Players Page in the GUI.
     */
    public static void showSetNumOfPlayersPage() {
        WaitingRoomPage.hideWaitingRoomPage();
        numOfPlayers.setVisible(true);
        writeNumOfPlayers.setVisible(true);
        enterNumOfPlayers.setVisible(true);
        writeNumOfPlayers.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleEnterNumOfPlayers();
            }
        });
        enterNumOfPlayers.setOnMouseClicked(event -> {
            handleEnterNumOfPlayers();
        });
    }

    /**
     * This static method is used to handle the event of entering the number of players. It checks
     * if the number of players is valid and navigates to the Waiting Room Page if it is.
     */
    public static void handleEnterNumOfPlayers() {
        int num = 0;
        try {
            num = Integer.parseInt(writeNumOfPlayers.getCharacters().toString());
        } catch (NumberFormatException e) {
            invalidNumOfPlayers.setVisible(true);
        }
        if (num < 2 || num > 4) {
            invalidNumOfPlayers.setVisible(true);
        } else {
            guiActuator.setNumOfPlayers(num);
            enterNumOfPlayers.setVisible(false);
            numOfPlayers.setVisible(false);
            writeNumOfPlayers.setVisible(false);
            WaitingRoomPage.showWaitingRoomPage();
            invalidNumOfPlayers.setVisible(false);
        }
    }

    /**
     * This static method is used to show an error message when the number of players entered is
     * invalid.
     */
    public static void showErrorMesssage() {
        invalidNumOfPlayers.setVisible(true);
    }

    /**
     * This static method is used to hide the Set Number of Players Page in the GUI. It sets the
     * visibility of the Set Number of Players Page to false.
     */
    public static void hideSetNumOfPlayersPage() {
        if (numOfPlayers != null) {
            numOfPlayers.setVisible(false);
            writeNumOfPlayers.setVisible(false);
            enterNumOfPlayers.setVisible(false);
            invalidNumOfPlayers.setVisible(false);
        }
    }

}

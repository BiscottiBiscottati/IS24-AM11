package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontManager;
import it.polimi.ingsw.am11.view.client.GUI.utils.FontsEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.DISTANCE_TO_BORDER;
import static it.polimi.ingsw.am11.view.client.GUI.utils.Proportions.HALF_BUTTON_SIZE;

/**
 * This class is used to create the page where the player can set their nickname.
 */
public class SetNickPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetNickPage.class);

    private static final Pattern SPACE_SPLIT = Pattern.compile("\\s+");
    private static MiniGameModel miniGameModel;
    private static GuiActuator guiActuator;
    private static TextField writeNick = null;
    private static Label yourName, nameAlreadyTaken;
    private static Font font, fontBig;
    private static Button chooseNick;
    private static Button goToNetwork;
    private static int halfButtonSize, distanceToBorder;

    /**
     * This static method is used to create the set nickname page in the GUI. It initializes the
     * necessary components and sets their properties.
     *
     * @param codexNaturalis The GUI instance that the set nickname page is a part of.
     */
    public static void createSettingNickPage(CodexNaturalis codexNaturalis) {
        guiActuator = codexNaturalis.getGuiActuator();
        StackPane root = codexNaturalis.getSmallRoot();
        miniGameModel = codexNaturalis.getMiniGameModel();
        font = FontManager.getFont(FontsEnum.CLOISTER_BLACK, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));
        fontBig = FontManager.getFont(FontsEnum.CLOISTER_BLACK,
                                      Proportions.HALF_BUTTON_SIZE.getValue() * 3);
        halfButtonSize = HALF_BUTTON_SIZE.getValue();
        distanceToBorder = DISTANCE_TO_BORDER.getValue();
        writeNick = new TextField();

        writeNick.setAlignment(Pos.CENTER);
        writeNick.setPromptText("Nickname");
        writeNick.setFont(font);
        writeNick.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                           " -fx-max-width: " + 20 * halfButtonSize);

        yourName = new Label("Your Name:");
        nameAlreadyTaken = new Label();

        yourName.setBackground(Background.EMPTY);
        yourName.setFont(fontBig);
        yourName.setTranslateY(- 4 * halfButtonSize);

        nameAlreadyTaken.setBackground(Background.EMPTY);
        nameAlreadyTaken.setTranslateY(6 * halfButtonSize);
        nameAlreadyTaken.setText("Name already taken");
        nameAlreadyTaken.setTextFill(Color.RED);

        root.getChildren().addAll(yourName, writeNick, nameAlreadyTaken);
        chooseNick = new Button("_Cancel");
        goToNetwork = new Button("_Cancel");

        goToNetwork.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        goToNetwork.setTranslateX(- 5 * halfButtonSize);
        goToNetwork.setTranslateY(10 * halfButtonSize - distanceToBorder);
        goToNetwork.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        goToNetwork.setText("go Back");
        goToNetwork.setFont(font);
        goToNetwork.setTextFill(Color.web("#351F17"));
        goToNetwork.setOnMousePressed(
                event -> goToNetwork.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        goToNetwork.setOnMouseReleased(event -> goToNetwork.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        chooseNick.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        chooseNick.setTranslateX(5 * halfButtonSize);
        chooseNick.setTranslateY(10 * halfButtonSize - distanceToBorder);
        chooseNick.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chooseNick.setText("Choose");
        chooseNick.setFont(font);
        chooseNick.setTextFill(Color.web("#351F17"));
        chooseNick.setOnMousePressed(
                event -> chooseNick.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chooseNick.setOnMouseReleased(event -> chooseNick.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        root.getChildren().addAll(chooseNick, goToNetwork);

        writeNick.setVisible(false);
        yourName.setVisible(false);
        nameAlreadyTaken.setVisible(false);
        chooseNick.setVisible(false);
        goToNetwork.setVisible(false);
    }

    /**
     * This static method is used to show the set nickname page in the GUI. It sets the visibility
     * of the set nickname page to true.
     */
    public static void showSettingNickPage() {

        yourName.setVisible(true);
        writeNick.setVisible(true);
        nameAlreadyTaken.setVisible(false);
        chooseNick.setVisible(true);
        goToNetwork.setVisible(true);

        writeNick.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleEnterBtn();
            }
        });


        chooseNick.setOnMouseClicked(event -> {
            handleEnterBtn();
        });

        goToNetwork.setOnMouseClicked(event -> {
            chooseNick.setVisible(false);
            writeNick.setVisible(false);
            yourName.setVisible(false);
            nameAlreadyTaken.setVisible(false);
            goToNetwork.setVisible(false);

            NetworkPage.showNetworkPage();
        });
    }

    /**
     * This static method is used to handle the event when the player clicks the "Choose" button. It
     * checks if the nickname is valid and sets the player's nickname.
     */
    public static void handleEnterBtn() {
        String nick = writeNick.getCharacters().toString().strip();
        LOGGER.debug(nick);
        String[] nickSplit = SPACE_SPLIT.split(nick);
        if (nick.isEmpty()) {
            writeNick.setPromptText("Fail");
            nameAlreadyTaken.setText("Name cannot be empty");
            nameAlreadyTaken.setVisible(true);
        } else if (nickSplit.length > 1) {
            writeNick.setPromptText("Fail");
            nameAlreadyTaken.setText("Name cannot contain spaces");
            nameAlreadyTaken.setVisible(true);
        } else if (nick.length() > 19) {
            writeNick.setPromptText("Fail");
            nameAlreadyTaken.setText("Name must be shorter than 19 characters");
            nameAlreadyTaken.setVisible(true);
        } else {
            miniGameModel.setMyName(nick);
            guiActuator.setName(nick);
            chooseNick.setVisible(false);
            writeNick.setVisible(false);
            yourName.setVisible(false);
            nameAlreadyTaken.setVisible(false);
            goToNetwork.setVisible(false);
            WaitingRoomPage.showWaitingRoomPage();
        }
    }

    /**
     * This static method is used to show an error message when the nickname entered is invalid or
     * the number of player isn't set.
     *
     * @param message The error message to be displayed.
     */
    public static void showErrorMesssage(String message) {
        Platform.runLater(() -> {
            nameAlreadyTaken.setText(message);
            nameAlreadyTaken.setVisible(true);
        });
    }

    /**
     * This static method is used to hide the set nickname page in the GUI. It sets the visibility
     * of the set nickname page to false.
     */
    public static void hideSettingNickPage() {
        if (writeNick != null) {
            chooseNick.setVisible(false);
            writeNick.setVisible(false);
            yourName.setVisible(false);
            nameAlreadyTaken.setVisible(false);
            goToNetwork.setVisible(false);
        }

    }
}

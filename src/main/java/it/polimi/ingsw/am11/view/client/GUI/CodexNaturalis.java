package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.view.client.GUI.GUIParts.FrameHandler;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
import it.polimi.ingsw.am11.view.client.GUI.windows.*;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.SequencedSet;
import java.util.Set;

/**
 * This class is the main class of the GUI, it is used to start the GUI and to interact with the GUI
 * elements
 */
public class CodexNaturalis extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodexNaturalis.class);

    private static Stage primaryStage;
    private static Parent bigRoot;
    private final GuiActuator guiActuator;
    private final GuiUpdater guiUpdater;
    private Scene scene;
    private GamePage gamePage;
    private FXMLLoader fxmlLoader;
    private StackPane smallRoot;


    /**
     * Creates a new CodexNaturalis and initializes the gui updater and the gui actuator
     */
    public CodexNaturalis() {
        this.guiUpdater = new GuiUpdater(this);
        this.guiActuator = new GuiActuator(guiUpdater);
    }

    /**
     * Restarts the GUI
     */
    public static void restart() {
        Platform.runLater(() -> {
            bigRoot.setVisible(false);
            primaryStage.hide();
            primaryStage.close();
            new CodexNaturalis().start(new Stage());
        });
    }


    /**
     * Starts the GUI
     *
     * @param primaryStage the primary stage for this application, onto which the application scene
     *                     can be set. Applications may create other stages, if needed, but they
     *                     will not be primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        CodexNaturalis.primaryStage = primaryStage;
        fxmlLoader = new FXMLLoader(getClass().getResource(
                "/it/polimi/ingsw/am11/view/client/GUI/windows/GamePage.fxml"));
        try {
            bigRoot = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bigRoot.setVisible(false);

        initializeGUI();
        FrameHandler.setIcons(primaryStage, smallRoot);

        scene = new Scene(smallRoot,
                          Proportions.SQUARE_SIZE.getValue(),
                          Proportions.SQUARE_SIZE.getValue(),
                          javafx.scene.paint.Color.BLACK);
        primaryStage.setFullScreen(false);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        LoadingScreen.animateLoadingScreen();
    }

    /**
     * Initializes the GUI
     */
    private void initializeGUI() {
        smallRoot = new StackPane();
        LoadingScreen.createLoadingScreen(this);
        NetworkPage.createNetworkPage(this);
        SetNickPage.createSettingNickPage(this);
        WaitingRoomPage.createWaitingRoomPage(this);
        SetNumOfPlayersPage.createNumOfPlayersPage(this);
        SetObjCardsPage.createObjCardsPage(this);
        SetStarterCardsPage.createStarterCardsPage(this);
        gamePage = fxmlLoader.getController();
    }

    /**
     * Updates the GUI showing the next page
     */
    public static void receiveCandidateObjective() {
        Platform.runLater(() -> {

            WaitingRoomPage.hideWaitingRoomPage();
            SetObjCardsPage.showObjCardsPage();
        });
    }

    /**
     * Updates the god player's GUI showing the next page
     */
    public static void notifyGodPlayer() {
        SetNumOfPlayersPage.showSetNumOfPlayersPage();
    }

    /**
     * Gets the small root of the GUI
     *
     * @return the small root of the GUI
     */
    public StackPane getSmallRoot() {
        return smallRoot;
    }

    /**
     * Gets the GUI actuator
     *
     * @return the gui actuator
     */
    public GuiActuator getGuiActuator() {
        return guiActuator;
    }

    /**
     * This method is used to update the top card of the deck in the GUI.
     *
     * @param type  The type of the playable card. It can be RESOURCE, GOLD, etc.
     * @param color The color of the card. It can be RED, BLUE, etc.
     */
    public void updateDeckTop(PlayableCardType type, @Nullable GameColor color) {
        gamePage.updateDeckTop(type, color);
    }

    /**
     * This method is used to update the field in the GUI.
     *
     * @param nickname The nickname of the player whose field is being updated.
     * @param x        The x-coordinate of the card on the field.
     * @param y        The y-coordinate of the card on the field.
     * @param cardId   The ID of the card being placed on the field.
     * @param isRetro  A boolean indicating whether the card is placed face down (true) or face up
     *                 (false).
     */
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro) {
        gamePage.printCardsOnField(nickname);
    }

    /**
     * This method is used to update the displayed playable card in the GUI.
     *
     * @param previousId The ID of the previously displayed playable card.
     * @param currentId  The ID of the currently displayed playable card.
     */
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        gamePage.updateShownPlayable();
    }

    /**
     * This method is used to update the turn change in the GUI.
     *
     * @param nickname The nickname of the player whose turn it is now.
     */
    public void updateTurnChange(String nickname) {
        gamePage.updateTurnChange(nickname);
        gamePage.printCardsOnField(nickname);
    }

    /**
     * This method is used to update the points of a player in the GUI.
     *
     * @param nickname The nickname of the player whose points are being updated.
     * @param points   The new point total for the player.
     */
    public void updatePlayerPoint(String nickname, int points) {
        gamePage.updatePlayerPoints(nickname, points);
    }

    /**
     * This method is used to update the status of the game in the GUI.
     *
     * @param status The new status of the game.
     */
    public void updateGameStatus(GameStatus status) {
        switch (status) {
            case ONGOING -> {
                gamePage.placeStarterCard();
                Platform.runLater(this::showGamePage);
            }
            case LAST_TURN -> gamePage.showLastTurnMessage("LAST TURN!");
            case ARMAGEDDON -> gamePage.showLastTurnMessage("ARMAGEDDON!");

        }
    }

    /**
     * This method is used to display the game page in the GUI. It sets the visibility of the game
     * page, adjusts the stage settings, and enters full screen mode.
     */
    private void showGamePage() {
        smallRoot.setVisible(false);
        primaryStage.close();
        primaryStage = new Stage();
        primaryStage.setResizable(true);
        bigRoot.setVisible(true);
        scene.setRoot(bigRoot);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    /**
     * This method is used to update the common objective in the GUI.
     *
     * @param cardId     A set of IDs of the cards that are part of the common objective.
     * @param removeMode A boolean indicating whether the method should add (false) or remove (true)
     *                   the cards from the common objective.
     */
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        gamePage.updateCommonObj();
    }

    /**
     * This method is used to receive the final leaderboard of the game.
     *
     * @param finalLeaderboard A map where the key is the player's name and the value is the
     *                         player's final score.
     */
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        LOGGER.debug("Leaderboard received");
        gamePage.gameEnded();
    }

    /**
     * This method is used to update the hand of a player in the GUI.
     *
     * @param cardId     The ID of the card being added or removed from the player's hand.
     * @param removeMode A boolean indicating whether the method should add (false) or remove (true)
     *                   the card from the player's hand.
     */
    public void updateHand(int cardId, boolean removeMode) {
        gamePage.updateHand();
    }

    /**
     * This method is used to update the personal objective in the GUI.
     *
     * @param cardId     The ID of the card being added or removed from the player's personal
     *                   objective.
     * @param removeMode A boolean indicating whether the method should add (false) or remove (true)
     *                   the card from the player's personal objective.
     */
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        gamePage.updatePersonalObjective();
    }

    /**
     * This method is used to handle the reception of the starter card in the game. It updates the
     * GUI accordingly and prepares the game for the next phase.
     */
    public void receiveStarterCard() {
        Platform.runLater(() -> {
            WaitingRoomPage.hideWaitingRoomPage();
            SetStarterCardsPage.showStarterCardsPage();
            try {
                gamePage.createGamePage(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method is invoked when the server connection is lost. It handles the disconnection event
     * and updates the GUI accordingly.
     */
    public void disconnectedFromServer() {
        Platform.runLater(() -> {
            if (getMiniGameModel().table().getStatus().equals(GameStatus.ENDED)) {
                gamePage.gameEnded();
                gamePage.stopMusic();
            } else if (getMiniGameModel().table().getStatus().equals(GameStatus.ONGOING) ||
                       getMiniGameModel().table().getStatus().equals(GameStatus.LAST_TURN) ||
                       getMiniGameModel().table().getStatus().equals(GameStatus.ARMAGEDDON)) {

                gamePage.stopMusic();

                primaryStage.close();
                primaryStage = new Stage();
                bigRoot.setVisible(false);

                initializeGUI();
                FrameHandler.setIcons(primaryStage, smallRoot);
                scene = new Scene(smallRoot,
                                  Proportions.SQUARE_SIZE.getValue(),
                                  Proportions.SQUARE_SIZE.getValue(),
                                  javafx.scene.paint.Color.BLACK);
                primaryStage.setFullScreen(false);
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.show();
                LoadingScreen.animateLoadingScreen();
            } else {

                gamePage.stopMusic();
                SetNumOfPlayersPage.hideSetNumOfPlayersPage();
                SetObjCardsPage.hideObjCardsPage();
                SetStarterCardsPage.hideStarterCardsPage();
                WaitingRoomPage.hideWaitingRoomPage();
                SetNickPage.hideSettingNickPage();

                NetworkPage.showNetworkPage();
            }
        });
    }

    /**
     * This method is used to get the MiniGameModel instance.
     *
     * @return The MiniGameModel instance associated with the current game.
     */
    public MiniGameModel getMiniGameModel() {
        return guiUpdater.getMiniGameModel();
    }

    /**
     * This method is used to update the chat in the GUI.
     */
    public void updateChat() {
        LOGGER.debug("Chat updated");
        gamePage.updateChat();
    }

    /**
     * This method is used to display an error message on the game page in the GUI.
     *
     * @param message The error message to be displayed.
     */
    public void showErrorGamePage(String message) {
        gamePage.showErrorMessage(message);
    }

    /**
     * This method is invoked when the server connection is reestablished after a disconnection. It
     * handles the reconnection event, updates the GUI accordingly, and prepares the game to
     * continue from its last state.
     *
     * @param status The current status of the game when the server connection is reestablished.
     */
    public void reconnectedToServer(@NotNull GameStatus status) {
        switch (status) {
            case ENDED -> {
                Platform.runLater(this::showGamePage);
                gamePage.gameEnded();
            }
            case ONGOING, ARMAGEDDON, LAST_TURN -> {
                try {
                    gamePage.createGamePage(this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    showGamePage();
                    gamePage.placeStarterCard();
                    gamePage.updateHand();
                    gamePage.updateCommonObj();
                    gamePage.updatePersonalObjective();
                    gamePage.updateShownPlayable();
                    gamePage.updateDeckTop(PlayableCardType.RESOURCE,
                                           getMiniGameModel().table().getDeckTop(
                                                   PlayableCardType.RESOURCE));
                    gamePage.updateDeckTop(PlayableCardType.GOLD,
                                           getMiniGameModel().table().getDeckTop(
                                                   PlayableCardType.GOLD));
                    gamePage.printCardsOnField(getMiniGameModel().myName());
                    gamePage.updateTurnChange(getMiniGameModel().getCurrentTurn());
                    SequencedSet<String> players = getMiniGameModel().getPlayers();
                    players.forEach(player -> gamePage.updatePlayerPoints(player,
                                                                          getMiniGameModel().
                                                                                  getCliPlayer(
                                                                                          player).
                                                                                  getPoints()));
                    if (status == GameStatus.ARMAGEDDON) {
                        gamePage.showLastTurnMessage("ARMAGEDDON!");
                    } else if (status == GameStatus.LAST_TURN) {
                        gamePage.showLastTurnMessage("LAST TURN!");
                    }
                });
            }
        }
    }
}

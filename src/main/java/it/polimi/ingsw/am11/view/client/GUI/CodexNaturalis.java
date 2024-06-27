package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.SequencedSet;
import java.util.Set;

public class CodexNaturalis extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodexNaturalis.class);

    private static Stage primaryStage;
    private static Parent bigRoot;
    private final GuiActuator guiActuator;
    private final GuiUpdater guiUpdater;
    Scene scene;
    GamePage gamePage;
    FXMLLoader fxmlLoader;
    private StackPane smallRoot;


    public CodexNaturalis() {
        this.guiUpdater = new GuiUpdater(this);
        this.guiActuator = new GuiActuator(guiUpdater);
    }

    public static void restart() {
        Platform.runLater(() -> {
            try {
                bigRoot.setVisible(false);
                primaryStage.hide();
                primaryStage.close();
                new CodexNaturalis().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


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

        initializeGUI(true);
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

    private void initializeGUI(boolean withLoadingScreen) {
        smallRoot = new StackPane();
        if (withLoadingScreen) {
            LoadingScreen.createLoadingScreen(this);
        }
        NetworkPage.createNetworkPage(this);
        SetNickPage.createSettingNickPage(this);
        WaitingRoomPage.createWaitingRoomPage(this);
        SetNumOfPlayersPage.createNumOfPlayersPage(this);
        SetObjCardsPage.createObjCardsPage(this);
        SetStarterCardsPage.createStarterCardsPage(this);
        gamePage = fxmlLoader.getController();
    }

    public static void receiveCandidateObjective() {
        Platform.runLater(() -> {

            WaitingRoomPage.hideWaitingRoomPage();
            SetObjCardsPage.showObjCardsPage();
        });
    }

    public static void notifyGodPlayer() {
        SetNumOfPlayersPage.showSetNumOfPlayersPage();
    }

    public StackPane getSmallRoot() {
        return smallRoot;
    }

    public GuiActuator getGuiActuator() {
        return guiActuator;
    }

    public void updateDeckTop(PlayableCardType type, Color color) {
        gamePage.updateDeckTop(type, color);
    }

    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro) {
        gamePage.printCardsOnField(nickname);
    }

    public void updateShownPlayable(Integer previousId, Integer currentId) {
        gamePage.updateShownPlayable();
    }

    public void updateTurnChange(String nickname) {
        gamePage.updateTurnChange(nickname);
        gamePage.printCardsOnField(nickname);
    }

    public void updatePlayerPoint(String nickname, int points) {
        gamePage.updatePlayerPoints(nickname, points);
    }

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

    private void showGamePage() {
        smallRoot.setVisible(false);
        primaryStage.close();
        primaryStage = new Stage();
        primaryStage.setResizable(true);
        bigRoot.setVisible(true);
        primaryStage.setScene(
                new Scene(bigRoot, 1080,
                          720,
                          javafx.scene.paint.Color.BLACK));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        gamePage.updateCommonObj();
    }

    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        LOGGER.debug("Leaderboard received");
        gamePage.gameEnded();
    }

    public void updateHand(int cardId, boolean removeMode) {
        gamePage.updateHand();
    }

    public void updatePersonalObjective(int cardId, boolean removeMode) {
        gamePage.updatePersonalObjective();
    }

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

    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {

    }

    public void updateNumOfPlayers(int numOfPlayers) {

    }

    public void disconnectedFromServer() {
        Platform.runLater(() -> {
            if (getMiniGameModel().table().getStatus().equals(GameStatus.ENDED)) {
                gamePage.gameEnded();
            } else if (getMiniGameModel().table().getStatus().equals(GameStatus.ONGOING) ||
                       getMiniGameModel().table().getStatus().equals(GameStatus.LAST_TURN) ||
                       getMiniGameModel().table().getStatus().equals(GameStatus.ARMAGEDDON)) {

                primaryStage.close();
                primaryStage = new Stage();
                bigRoot.setVisible(false);

                initializeGUI(true);
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
                SetNumOfPlayersPage.hideSetNumOfPlayersPage();
                SetObjCardsPage.hideObjCardsPage();
                SetStarterCardsPage.hideStarterCardsPage();
                WaitingRoomPage.hideWaitingRoomPage();
                SetNickPage.hideSettingNickPage();

                NetworkPage.showNetworkPage();
            }
        });
    }

    public MiniGameModel getMiniGameModel() {
        return guiUpdater.getMiniGameModel();
    }

    public void updateChat() {
        LOGGER.debug("Chat updated");
        gamePage.updateChat();
    }

    public void showErrorGamePage(String message) {
        gamePage.showErrorMessage(message);
    }

    public void reconnectedToServer(GameStatus status) {
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

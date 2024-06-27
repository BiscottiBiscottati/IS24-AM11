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

public class CodexNaturalis extends Application implements GuiObserver {
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

    public StackPane getSmallRoot() {
        return smallRoot;
    }

    public GuiActuator getGuiActuator() {
        return guiActuator;
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        gamePage.updateDeckTop(type, color);
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro) {
        gamePage.printCardsOnField(nickname);
    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        gamePage.updateShownPlayable();
    }

    @Override
    public void updateTurnChange(String nickname) {
        gamePage.updateTurnChange(nickname);
        gamePage.printCardsOnField(nickname);
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        gamePage.updatePlayerPoints(nickname, points);
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        switch (status) {
            case ONGOING -> {
                gamePage.placeStarterCard();
                Platform.runLater(this::showGamePage);
            }
            case ENDED -> {
                gamePage.gameEnded();
            }
            case LAST_TURN -> {
                gamePage.showLastTurnMessage("LAST TURN!");
            }
            case ARMAGEDDON -> {
                gamePage.showLastTurnMessage("PREPARE FOR YOUR LAST TURN!");
            }

        }
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        gamePage.updateCommonObj();
    }

    @Override
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        LOGGER.debug("Leaderboard received");
        gamePage.gameEnded();
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        gamePage.updateHand();
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        gamePage.updatePersonalObjective();
    }

    @Override
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

    @Override
    public void receiveCandidateObjective() {
        Platform.runLater(() -> {

            WaitingRoomPage.hideWaitingRoomPage();
            SetObjCardsPage.showObjCardsPage();
        });
    }

    @Override
    public void notifyGodPlayer() {
        SetNumOfPlayersPage.showSetNumOfPlayersPage();
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {

    }

    @Override
    public void updateNumOfPlayers(int numOfPlayers) {

    }

    @Override
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

    @Override
    public void updateChat() {
        LOGGER.debug("Chat updated");
        gamePage.updateChat();
    }

    @Override
    public void showErrorGamePage(String message) {
        gamePage.showErrorMessage(message);
    }

    @Override
    public void reconnectedToServer(GameStatus status) {
        switch (status) {
            case LAST_TURN -> {
                Platform.runLater(this::showGamePage);

            }
            case ARMAGEDDON -> {
                Platform.runLater(this::showGamePage);
            }
            case ENDED -> {
                Platform.runLater(this::showGamePage);
                gamePage.gameEnded();
            }
            case ONGOING -> {
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
                });
            }
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
}

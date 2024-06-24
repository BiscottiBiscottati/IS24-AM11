package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
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

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CodexNaturalis extends Application implements GuiObserver {
    private final GuiActuator guiActuator;
    private final GuiUpdater guiUpdater;
    private Stage primaryStage;
    private StackPane smallRoot;
    private Parent bigRoot;
    Scene scene;
    GamePage gamePage;
    FXMLLoader fxmlLoader;


    public CodexNaturalis() {
        this.guiUpdater = new GuiUpdater(this);
        this.guiActuator = new GuiActuator(guiUpdater);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        fxmlLoader = new FXMLLoader(getClass().getResource(
                "/it/polimi/ingsw/am11/view/client/GUI/windows/GamePage.fxml"));
        try {
            bigRoot = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bigRoot.setVisible(false);

        try {
            initializeGUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FrameHandler.setIcons(primaryStage, smallRoot);

        scene = new Scene(smallRoot,
                          Proportions.SQUARE_SIZE.getValue(),
                          Proportions.SQUARE_SIZE.getValue(),
                          javafx.scene.paint.Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        LoadingScreen.animateLoadingScreen();
    }

    private void initializeGUI() throws IOException {
        primaryStage.setFullScreen(false);
        smallRoot = new StackPane();
        LoadingScreen.createLoadingScreen(this);
        NetworkPage.createNetworkPage(this);
        SetNickPage.createSettingNickPage(this);
        WaitingRoomPage.createWaitingRoomPage(this);
        SetNumOfPlayersPage.createNumOfPlayersPage(this);
        gamePage = fxmlLoader.getController();

    }

    public StackPane getSmallRoot() {
        return smallRoot;
    }

    public GuiActuator getGuiActuator() {
        return guiActuator;
    }

    public MiniGameModel getMiniGameModel() {
        return guiUpdater.getMiniGameModel();
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        gamePage.updateDeckTop(type, color);
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro) {

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        gamePage.updateShownPlayable();
    }

    @Override
    public void updateTurnChange(String nickname) {
        gamePage.updateTurnChange(nickname);
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {

    }

    @Override
    public void updateGameStatus(GameStatus status) {
        if (status == GameStatus.ONGOING) {
            gamePage.placeStarterCard();
            Platform.runLater(this::showGamePage);
        }
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {
        gamePage.updateCommonObj();
    }

    @Override
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {

    }

    @Override
    public void throwException(Exception ex) {
        //FIXME: to be completed
        if (ex instanceof NotSetNumOfPlayerException) {
            WaitingRoomPage.hideWaitingRoomPage();
            SetNickPage.showSettingNickPage();
        }
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
    public void receiveStarterCard(int cardId) {
        Platform.runLater(() -> {
            SetStarterCardsPage.createStarterCardsPage(this, cardId);
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
    public void receiveCandidateObjective(Set<Integer> cardId) {
        Platform.runLater(() -> {
            SetObjCardsPage.createObjCardsPage(this, cardId);
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

    }


    private void showGamePage() {
        smallRoot.setVisible(false);
        GamePage.showGamePage(bigRoot);
        primaryStage.setScene(
                new Scene(bigRoot, Proportions.SQUARE_SIZE.getValue(),
                          Proportions.SQUARE_SIZE.getValue(),
                          javafx.scene.paint.Color.BLACK));
        primaryStage.setResizable(true);
    }
}

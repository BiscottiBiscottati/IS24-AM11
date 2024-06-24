package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.view.client.GUI.GUIParts.FrameHandler;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.GUI.windows.*;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CodexNaturalis extends Application implements GuiObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodexNaturalis.class);
    private final GuiActuator guiActuator;
    private final GuiUpdater guiUpdater;

    private final GuiResources guiResources;
    private Stage primaryStage;
    private Font font, fontBig;
    private int WindowSize, halfButtonSize, distanceToBorder;
    private GamePage gamePage;
    private FXMLLoader fxmlLoader;
    private StackPane initialRoot;
    private FrameHandler frameHandler;

    public CodexNaturalis() {
        this.guiResources = new GuiResources();
        this.guiUpdater = new GuiUpdater(this);
        this.guiActuator = new GuiActuator(guiUpdater);
    }

    public static void showNetworkPage() {
        NetworkPage.showNetworkPage();
    }

    public static void showWaitingRoomPage() {
        WaitingRoomPage.showWaitingRoomPage();
    }

    public GuiResources getGuiResources() {
        return guiResources;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        fxmlLoader = new FXMLLoader(getClass().getResource(
                "windows/GamePage.fxml"));

        try {
            initializeGUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FrameHandler.setIcons(guiResources, primaryStage, initialRoot);

        Scene scene = new Scene(initialRoot, WindowSize, WindowSize,
                                javafx.scene.paint.Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        LoadingScreen.animateLoadingScreen();
    }

    private void initializeGUI() throws IOException {
        setFullScreen(false);
        font = Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                             1.5 * halfButtonSize);
        fontBig = Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                                3 * halfButtonSize);
        initialRoot = new StackPane();

        LoadingScreen.createLoadingScreen(this);
        NetworkPage.createNetworkPage(this);
        SetNickPage.createSettingNickPage(this);
        WaitingRoomPage.createWaitingRoomPage(this);
        SetNumOfPlayersPage.createNumOfPlayersPage(this);
        gamePage = fxmlLoader.getController();
    }

    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            primaryStage.setFullScreen(true);
            //TODO: reformat for fullscreen
        } else {
            primaryStage.setFullScreen(false);
            WindowSize = (int) (Math.min(Screen.getPrimary().getBounds().getHeight(),
                                         Screen.getPrimary().getBounds().getWidth()) * 0.7);
            halfButtonSize = WindowSize / 48;
            distanceToBorder = halfButtonSize >> 2;
        }
    }

    public int getWindowSize() {
        return WindowSize;
    }

    public int getHalfButtonSize() {
        return halfButtonSize;
    }

    public Font getFont() {
        return font;
    }

    public Font getFontBig() {
        return fontBig;
    }

    public int getDistanceToBorder() {
        return distanceToBorder;
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
        if (ex instanceof NotSetNumOfPlayerException) {
            hideWaitingRoomPage();
            showSettingNickPage();
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
        LOGGER.info("Received starter card in gui: {}", cardId);
        Platform.runLater(() -> {
            SetStarterCardsPage setStarterCardsPage = new SetStarterCardsPage(this);
            setStarterCardsPage.createStarterCardsPage(cardId);
            LOGGER.info("Created StarterCardPage");
            hideWaitingRoomPage();
            setStarterCardsPage.showStarterCardsPage();
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
            SetObjCardsPage setObjCardsPage = new SetObjCardsPage(this);
            setObjCardsPage.createObjCardsPage(cardId);
            hideWaitingRoomPage();
            setObjCardsPage.showObjCardsPage();
        });
    }

    @Override
    public void notifyGodPlayer() {
        showSetNumOfPlayersPage();
    }

    public static void showSetNumOfPlayersPage() {
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

    public static void hideWaitingRoomPage() {
        WaitingRoomPage.hideWaitingRoomPage();
    }

    public static void showSettingNickPage() {
        SetNickPage.showSettingNickPage();
    }

    public void showGamePage() {
        Parent ongoingRoot;
        try {
            ongoingRoot = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ongoingRoot.setVisible(true);
        primaryStage.setScene(
                new Scene(ongoingRoot, WindowSize, WindowSize, javafx.scene.paint.Color.BLACK));
        this.setFullScreen(true);
    }

    public StackPane getInitialRoot() {
        return initialRoot;
    }
}

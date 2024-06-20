package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.GUI.windows.*;
import it.polimi.ingsw.am11.view.client.TUI.TuiUpdater;
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
import java.util.concurrent.CountDownLatch;

public class CodexNaturalis extends Application implements GuiObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiUpdater.class);
    private final GuiActuator guiActuator;
    private final GuiUpdater guiUpdater;


    private final GuiResources guiResources;
    private final CountDownLatch latch;
    GuiExceptionReceiver guiExceptionReceiver;
    Stage primaryStage;
    Scene scene;
    Font font, fontBig;
    int WindowSize, halfButtonSize, distanceToBorder;
    LoadingScreen loadingScreen;
    NetworkPage networkPage;
    SetNickPage setNickPage;
    WaitingRoomPage waitingRoomPage;
    SetNumOfPlayersPage setNumOfPlayersPage;
    GamePage gamePage;
    FXMLLoader fxmlLoader;
    private StackPane root;
    private Parent root1;
    private FrameHandler frameHandler;

    public CodexNaturalis() {
        this.guiResources = new GuiResources();
        this.guiUpdater = new GuiUpdater(this);
        this.guiActuator = new GuiActuator(guiUpdater);
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            initializeGUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        frameHandler = new FrameHandler(guiResources, primaryStage, root);
        scene = new Scene(root, WindowSize, WindowSize, javafx.scene.paint.Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        loadingScreen.animateLoadingScreen();
    }

    public void initializeGUI() throws IOException {
        setFullScreen(false);
        font = Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                             1.5 * halfButtonSize);
        fontBig =
                Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                              3 * halfButtonSize);
        root = new StackPane();
        loadingScreen = new LoadingScreen(this);
        loadingScreen.createLoadingScreen();
        networkPage = new NetworkPage(this);
        networkPage.createNetworkPage();
        setNickPage = new SetNickPage(this);
        setNickPage.createSettingNickPage();
        waitingRoomPage = new WaitingRoomPage(this);
        waitingRoomPage.createWaitingRoomPage();
        setNumOfPlayersPage = new SetNumOfPlayersPage(this);
        setNumOfPlayersPage.createNumOfPlayersPage();
        fxmlLoader = new FXMLLoader(getClass().getResource(
                "/it/polimi/ingsw/am11/view/client/GUI/window/GamePage.fxml"));
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        root1.setVisible(false);
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

    public GuiResources getGuiResources() {
        return guiResources;
    }

    public StackPane getRoot() {
        return root;
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

    public void showNetworkPage() {
        networkPage.showNetworkPage();
    }

    public void showSettingNickPage() {
        setNickPage.showSettingNickPage();
    }

    public void showWaitingRoomPage() {
        waitingRoomPage.showWaitingRoomPage();
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {

    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {

    }

    @Override
    public void updateTurnChange(String nickname) {

    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {

    }

    @Override
    public void updateGameStatus(GameStatus status) throws IOException {
        System.out.println("Updating game status in gui: " + status);
        String statusString = status.toString();
        if (statusString.equals("ONGOING")) {
            System.out.println("Game is ongoing");
            waitingRoomPage.hideWaitingRoomPage();
            gamePage.createGamePage(this);
            showGamePage();
        }
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardId, boolean removeMode) {

    }

    @Override
    public void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard) {

    }

    @Override
    public void throwException(Exception ex) {

    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {

    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
    }

    @Override
    public void receiveStarterCard(int cardId) {
        System.out.println("Received starter card in gui: " + cardId);
        Platform.runLater(() -> {
            SetStarterCardsPage setStarterCardsPage = new SetStarterCardsPage(this);
            setStarterCardsPage.createStarterCardsPage(cardId);
            System.out.println("Created StarterCardPage");
            hideWaitingRoomPage();
            setStarterCardsPage.showStarterCardsPage();
        });
    }

    public void hideWaitingRoomPage() {
        waitingRoomPage.hideWaitingRoomPage();
    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardId) {
        Platform.runLater(() -> {
            SetObjCardsPage setObjCardsPage = new SetObjCardsPage(this);
            setObjCardsPage.createStarterCardsPage(cardId);
            hideWaitingRoomPage();
            setObjCardsPage.showStarterCardsPage();
        });

    }

    @Override
    public void notifyGodPlayer() {
        showSetNumOfPlayersPage();
    }

    public void showSetNumOfPlayersPage() {
        setNumOfPlayersPage.showSetNumOfPlayersPage();
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

    public void showGamePage() {
        primaryStage.setScene(
                new Scene(root1, WindowSize, WindowSize, javafx.scene.paint.Color.BLACK));
        this.setFullScreen(true);
        GamePage.showGamePage(root1);
    }
}

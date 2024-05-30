package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.GUI.window.FrameHandler;
import it.polimi.ingsw.am11.view.client.GUI.windows1.LoadingScreen;
import it.polimi.ingsw.am11.view.client.GUI.windows1.NetworkPage;
import it.polimi.ingsw.am11.view.client.GUI.windows1.SettingNickPage;
import it.polimi.ingsw.am11.view.client.GUI.windows1.WaitingRoomPage;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Map;
import java.util.Set;

public class CodexNaturalis1 extends Application implements GuiObserver {
    private final GuiResources guiResources;
    GuiActuator guiActuator;
    GuiExceptionReceiver guiExceptionReceiver;
    GuiUpdater guiUpdater;
    MiniGameModel miniGameModel;
    Stage primaryStage;
    Scene scene;
    Font font, fontBig;
    int WindowSize, halfButtonSize, distanceToBorder;
    LoadingScreen loadingScreen;
    NetworkPage networkPage;
    SettingNickPage settingNickPage;
    WaitingRoomPage waitingRoomPage;
    private StackPane root;
    private FrameHandler frameHandler;

    public CodexNaturalis1() {
        this.guiResources = new GuiResources();
        this.miniGameModel = new MiniGameModel();
        this.guiExceptionReceiver = new GuiExceptionReceiver(this);
        this.guiUpdater = new GuiUpdater(guiExceptionReceiver, miniGameModel, this);
        this.guiActuator = new GuiActuator(guiUpdater, miniGameModel);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initializeGUI();
        frameHandler = new FrameHandler(guiResources, primaryStage, root);
        scene = new Scene(root, WindowSize, WindowSize, javafx.scene.paint.Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        loadingScreen.animateLoadingScreen();

    }

    public void initializeGUI() {
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
        settingNickPage = new SettingNickPage(this);
        settingNickPage.createSettingNickPage();
        waitingRoomPage = new WaitingRoomPage(this);
        waitingRoomPage.createWaitingRoomPage();

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
        return miniGameModel;
    }

    public void showNetworkPage() {
        networkPage.showNetworkPage();
    }

    public void showSettingNickPage() {
        settingNickPage.showSettingNickPage();
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
    public void updateGameStatus(GameStatus status) {

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

    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardId) {

    }

    @Override
    public void notifyGodPlayer() {

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
}

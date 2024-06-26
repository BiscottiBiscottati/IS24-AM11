package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.view.client.GUI.GUIParts.FrameHandler;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.GUI.utils.Proportions;
import it.polimi.ingsw.am11.view.client.GUI.windows.*;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CodexNaturalis extends Application implements GuiObserver {
    private final GuiActuator guiActuator;
    private final GuiUpdater guiUpdater;
    Scene scene;
    GamePage gamePage;
    FXMLLoader fxmlLoader;
    private Stage primaryStage;
    private StackPane smallRoot;
    private Parent bigRoot;


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
        if (getMiniGameModel().table().getStatus().equals(GameStatus.ENDED)) {
            bigRoot.setVisible(false);
            primaryStage.setResizable(false);
            smallRoot.setVisible(true);
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(false);
            primaryStage.show();
            NetworkPage.showNetworkPage();
        } else {
            NetworkPage.showNetworkPage();
        }
    }

    public MiniGameModel getMiniGameModel() {
        return guiUpdater.getMiniGameModel();
    }

    @Override
    public void updateChat() {
        System.out.println("Chat updated");
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
                gamePage.showLastTurnMessage("LAST TURN!");
            }
            case ARMAGEDDON -> {
                Platform.runLater(this::showGamePage);
                gamePage.showLastTurnMessage("PREPARE FOR YOUR LAST TURN!");
            }
            case ENDED -> {
                Platform.runLater(this::showGamePage);
                gamePage.gameEnded();
            }
            case ONGOING -> {
                Platform.runLater(this::showGamePage);
            }
        }
    }

    private void showGamePage() {
        smallRoot.setVisible(false);
        primaryStage.setResizable(true);
        bigRoot.setVisible(true);
        primaryStage.setScene(
                new Scene(bigRoot, 1920,
                          1080,
                          javafx.scene.paint.Color.BLACK));
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().toString().equals("ESCAPE")) {
                    int size = 1920;
                    int halfButtonSize = Proportions.HALF_BUTTON_SIZE.getValue();
                    int distanceToBorder = Proportions.DISTANCE_TO_BORDER.getValue();
                    Rectangle draggableRect = new Rectangle(
                            size, halfButtonSize << 2);
                    draggableRect.setFill(javafx.scene.paint.Color.BLACK);
                    draggableRect.setTranslateY(- size / 2 + halfButtonSize);
                    draggableRect.setOnMousePressed(pressEvent -> {
                        draggableRect.setOnMouseDragged(dragEvent -> {
                            primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                            primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
                        });
                    });
                    bigRoot.getChildrenUnmodifiable().add(draggableRect);

                    ImageView closeCross = GuiResources.getTheImageView(GuiResEnum.CLOSE_CROSS);
                    closeCross.setFitHeight(2 * halfButtonSize);
                    closeCross.setPreserveRatio(true);
                    closeCross.setOpacity(0.5);

                    Button closeButton = new Button("_Cancel");
                    closeButton.setPrefSize(2 * halfButtonSize, 2 * halfButtonSize);
                    closeButton.setGraphic(closeCross);
                    closeButton.setTranslateX(size / 2 - halfButtonSize);
                    closeButton.setTranslateY(- size / 2 + halfButtonSize + 2 * distanceToBorder);
                    closeButton.setBackground(Background.EMPTY);
                    closeButton.setOnMouseEntered(event -> closeCross.setOpacity(0.7));
                    closeButton.setOnMouseExited(event -> closeCross.setOpacity(0.5));
                    closeButton.setOnMousePressed(event -> closeCross.setOpacity(1));

                    closeButton.setOnAction(event -> Platform.exit());
                    bigRoot.getChildrenUnmodifiable().add(closeButton);

                    //Minimize Button

                    ImageView minimizeBar = GuiResources.getTheImageView(GuiResEnum.MINIMIZE_BAR);
                    minimizeBar.setFitHeight(2 * halfButtonSize);
                    minimizeBar.setPreserveRatio(true);
                    minimizeBar.setOpacity(0.5);

                    Button minimizeButton = new Button("_Cancel");
                    minimizeButton.setPrefSize(2 * halfButtonSize, 2 * halfButtonSize);
                    minimizeButton.setGraphic(minimizeBar);
                    minimizeButton.setTranslateX(
                            (double) size / 2 - 3 * halfButtonSize - 4 * distanceToBorder);
                    minimizeButton.setTranslateY(
                            (double) - size / 2 + halfButtonSize + 2 * distanceToBorder);
                    minimizeButton.setBackground(Background.EMPTY);
                    minimizeButton.setOnMouseEntered(event -> minimizeBar.setOpacity(0.7));
                    minimizeButton.setOnMouseExited(event -> minimizeBar.setOpacity(0.5));
                    minimizeButton.setOnMousePressed(event -> minimizeBar.setOpacity(1));

                    minimizeButton.setOnAction(event -> {primaryStage.setIconified(true);});
                    bigRoot.getChildrenUnmodifiable().add(minimizeButton);
                    primaryStage.setOnShowing(event -> {primaryStage.setIconified(false);});

                }
            }
        });
        primaryStage.setFullScreen(true);
    }
}

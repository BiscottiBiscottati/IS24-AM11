package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.GuiExceptionReceiver;
import it.polimi.ingsw.am11.view.client.GUI.GuiObserver;
import it.polimi.ingsw.am11.view.client.GUI.GuiUpdater;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CodexNaturalis extends Application implements GuiObserver {

    private final GuiResources guiResources;
    private FrameHandler frameHandler;


    public CodexNaturalis() {
        this.guiResources = new GuiResources();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        MiniGameModel miniGameModel = new MiniGameModel();
        GuiExceptionReceiver exceptionReceiver = new GuiExceptionReceiver();
        GuiUpdater guiUpdater = new GuiUpdater(exceptionReceiver, miniGameModel, this);
        GuiActuator guiActuator = new GuiActuator(guiUpdater);

        // Proportions
        int size = (int) (Math.min(Screen.getPrimary().getBounds().getHeight(),
                                   Screen.getPrimary().getBounds().getWidth()) * 0.7);

        int halfButtonSize = size / 48;
        int distanceToBorder = halfButtonSize >> 2;


        //Let's get the custom font
        Font font = Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                                  1.5 * halfButtonSize);
        Font fontBig =
                Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                              3 * halfButtonSize);

        //Let's build the loading screen
        StackPane root = new StackPane();


        ImageView lDBackground = guiResources.getTheImageView(GuiResEnum.LGIN_BACKGROUND);
        ImageView lDSquare = guiResources.getTheImageView(GuiResEnum.LGIN_SQUARE);
        ImageView lDWritings = guiResources.getTheImageView(GuiResEnum.LGIN_WRITINGS);
        ImageView lDDisks = guiResources.getTheImageView(GuiResEnum.LGIN_DISK);

        ImageView wolf = guiResources.getTheImageView(GuiResEnum.WOLF_ICON);
        ImageView butterfly = guiResources.getTheImageView(GuiResEnum.BUTTERLFY_ICON);
        ImageView mushroom = guiResources.getTheImageView(GuiResEnum.MUSHROOM_ICON);
        ImageView leaf = guiResources.getTheImageView(GuiResEnum.LEAF_ICON);

        LoadingScreen loadingScreen = new LoadingScreen();

        SequentialTransition sqT = new SequentialTransition();
        ParallelTransition prT = new ParallelTransition();

        root.getChildren().addAll(lDBackground, lDSquare, lDWritings, lDDisks,
                                  wolf, butterfly, mushroom, leaf);
        // Socket text field

        VBox theBox = new VBox(2 * halfButtonSize);
        theBox.setAlignment(Pos.CENTER);

        TextField ipAddress = new javafx.scene.control.TextField();
        TextField port = new javafx.scene.control.TextField();

        theBox.getChildren().addAll(ipAddress, port);
        theBox.setVisible(false);

        root.getChildren().addAll(theBox);

        // Label: socket or rmi
        Label connectionType = new Label();
        Label connectionFailed = new Label();

        NetworkPage networkPage = new NetworkPage();

        root.getChildren().addAll(connectionFailed, connectionType);

        // TextField : Nickname and Label: Nickname and Label: Name already took

        TextField writeNick = new TextField();
        Label yourName = new Label("Your Name:");
        Label nameAlreadyTaken = new Label();

        SettingNick settingNick = new SettingNick();

        root.getChildren().addAll(yourName, writeNick, nameAlreadyTaken);

        // Buttons declarations
        Button goToNetwork = new Button("_Cancel");
        Button chooseNick = new Button("_Cancel");
        Button chooseSocket = new Button("_Cancel");
        Button chooseRMI = new Button("_Cancel");
        Button joinButton = new Button("_Cancel");
        Button enterNumOfPlayers = new Button("_Cancel");
        Button goBack = new Button("_Cancel");

        Buttons buttons = new Buttons();
        buttons.createButtons(goToNetwork, halfButtonSize, distanceToBorder, font, chooseNick,
                              chooseSocket, chooseRMI, joinButton, enterNumOfPlayers, goBack);

        root.getChildren().addAll(chooseRMI, chooseSocket, joinButton, chooseNick, goToNetwork,
                                  enterNumOfPlayers, goBack);

        // Label: Number of players and TextField: Number of players
        Label numOfPlayers = new Label("Number of players:");
        Label invalidNumOfPlayers = new Label("Invalid number of players");
        TextField writeNumOfPlayers = new TextField();

        root.getChildren().addAll(numOfPlayers, writeNumOfPlayers, invalidNumOfPlayers);

        SettingNumOfPlayers settingNumOfPlayers = new SettingNumOfPlayers();

        AtomicInteger totalPlayers = new AtomicInteger();

        AtomicInteger currentPlayers = new AtomicInteger();

        Label waitingForPlayers = new Label("Waiting for other players to join...");
        ProgressIndicator loadingWheel = new ProgressIndicator();

        WaitingRoom waitingRoom = new WaitingRoom();

        root.getChildren().addAll(waitingForPlayers, loadingWheel);

        // Let's set the frame of the stage and the icons and add it
        frameHandler = new FrameHandler(guiResources, stage, root);

        // Set scene and stage
        Scene scene = new Scene(root, size, size, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        List<Label> labels = new ArrayList<>(
                List.of(numOfPlayers, invalidNumOfPlayers, yourName, nameAlreadyTaken,
                        connectionType, connectionFailed, waitingForPlayers));
        List<Button> buttonList = new ArrayList<>(
                List.of(goToNetwork, chooseNick, chooseSocket, chooseRMI, joinButton,
                        enterNumOfPlayers, goBack));
        List<TextField> textFields = new ArrayList<>(
                List.of(writeNick, ipAddress, port, writeNumOfPlayers));

        List<ImageView> images = new ArrayList<>(
                List.of(lDBackground, lDSquare, lDWritings, lDDisks, wolf, butterfly, mushroom,
                        leaf));

        loadingScreen.animateLoadingScreen(size, images, buttonList, prT, sqT, theBox);
        networkPage.createNetworkPage(font, halfButtonSize, labels, buttonList, textFields,
                                      guiActuator, theBox);
        settingNick.createSettingNick(font, halfButtonSize, fontBig, labels, textFields, buttonList,
                                      guiActuator, theBox, currentPlayers, miniGameModel);
        settingNumOfPlayers.createNumOfPlayersPage(font, halfButtonSize, labels, textFields,
                                                   buttonList, guiActuator, miniGameModel,
                                                   totalPlayers);
        waitingRoom.createWaitingRoom(waitingForPlayers, loadingWheel, font);
    }

    @Override
    public void updateDeckTop(PlayableCardType type,
                              it.polimi.ingsw.am11.model.cards.utils.enums.Color color) {

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
}

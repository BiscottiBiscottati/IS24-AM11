package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
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
import javafx.application.Platform;
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

public class CodexNaturalis extends Application implements GuiObserver {

    private final GuiResources guiResources;
    List<Label> labels;
    List<Button> buttonsList;
    List<TextField> textFields;
    GuiActuator guiActuator;
    MiniGameModel miniGameModel;
    Scene scene;
    SettingNumOfPlayers settingNumOfPlayers = new SettingNumOfPlayers();
    Font font;
    Font fontBig;
    int halfButtonSize;
    ProgressIndicator loadingWheel;
    private FrameHandler frameHandler;
    private StackPane root;


    public CodexNaturalis() {
        this.guiResources = new GuiResources();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        miniGameModel = new MiniGameModel();
        GuiExceptionReceiver exceptionReceiver = new GuiExceptionReceiver(this);
        GuiUpdater guiUpdater = new GuiUpdater(exceptionReceiver, miniGameModel, this);
        guiActuator = new GuiActuator(guiUpdater, miniGameModel);

        // Proportions
        int size = (int) (Math.min(Screen.getPrimary().getBounds().getHeight(),
                                   Screen.getPrimary().getBounds().getWidth()) * 0.7);

        halfButtonSize = size / 48;
        int distanceToBorder = halfButtonSize >> 2;


        //Let's get the custom font
        font = Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                             1.5 * halfButtonSize);
        fontBig =
                Font.loadFont(guiResources.getUrlString(GuiResEnum.CLOISTER_BLACK),
                              3 * halfButtonSize);

        //Let's build the loading screen
        root = new StackPane();


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

        SettingNick settingNick = new SettingNick();

        Label waitingForPlayers = new Label("Waiting for other players to join...");
        loadingWheel = new ProgressIndicator();

        WaitingRoom waitingRoom = new WaitingRoom();

        root.getChildren().addAll(waitingForPlayers, loadingWheel);

        // Let's set the frame of the stage and the icons and add it
        frameHandler = new FrameHandler(guiResources, stage, root);

        // Set scene and stage
        scene = new Scene(root, size, size, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        labels = new ArrayList<>(
                List.of(numOfPlayers, invalidNumOfPlayers, yourName, nameAlreadyTaken,
                        connectionType,
                        connectionFailed, waitingForPlayers));

        numOfPlayers.setVisible(false);
        invalidNumOfPlayers.setVisible(false);
        yourName.setVisible(false);
        nameAlreadyTaken.setVisible(false);
        connectionType.setVisible(false);
        connectionFailed.setVisible(false);
        waitingForPlayers.setVisible(false);

        buttonsList = new ArrayList<>(
                List.of(goToNetwork, chooseNick, chooseSocket, chooseRMI, joinButton,
                        enterNumOfPlayers, goBack));

        textFields = new ArrayList<>(List.of(writeNick, ipAddress, port, writeNumOfPlayers));

        writeNick.setVisible(false);
        ipAddress.setVisible(false);
        port.setVisible(false);
        writeNumOfPlayers.setVisible(false);

        List<ImageView> images = new ArrayList<>(
                List.of(lDBackground, lDSquare, lDWritings, lDDisks, wolf, butterfly, mushroom,
                        leaf));

        loadingScreen.animateLoadingScreen(size, images, buttonsList, textFields, labels, prT, sqT,
                                           theBox);
        networkPage.createNetworkPage(font, halfButtonSize, labels, buttonsList, textFields,
                                      guiActuator, theBox);
        settingNick.createSettingNick(font, halfButtonSize, fontBig, labels, textFields,
                                      buttonsList,
                                      guiActuator, theBox, loadingWheel,
                                      miniGameModel);

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

    @Override
    public void throwException(Exception ex) {
        if (ex instanceof IllegalPlayerSpaceActionException) {
            System.out.println("IllegalPlayerSpaceActionException");
        } else if (ex instanceof TurnsOrderException) {
            System.out.println("TurnsOrderException");
        } else if (ex instanceof PlayerInitException) {
            System.out.println("PlayerInitException");
        } else if (ex instanceof IllegalCardPlacingException) {
            System.out.println("IllegalCardPlacingException");
        } else if (ex instanceof IllegalPickActionException) {
            System.out.println("IllegalPickActionException");
        } else if (ex instanceof NotInHandException) {
            System.out.println("NotInHandException");
        } else if (ex instanceof EmptyDeckException) {
            System.out.println("EmptyDeckException");
        } else if (ex instanceof NumOfPlayersException) {
            System.out.println("NumOfPlayersException");
        } else if (ex instanceof NotGodPlayerException) {
            System.out.println("NotGodPlayerException");
        } else if (ex instanceof GameStatusException) {
            System.out.println("GameStatusException");
        } else if (ex instanceof NotSetNumOfPlayerException) {
            System.out.println("NotSetNumOfPlayerException");
        } else if (ex instanceof IllegalPlateauActionException) {
            System.out.println("IllegalPlateauActionException");
        } else if (ex instanceof MaxHandSizeException) {
            System.out.println("MaxHandSizeException");
        } else if (ex instanceof LostConnectionException) {
            System.out.println("LostConnectionException");
        }
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
        Platform.runLater(() -> {
            System.out.println("You are the god player");

            settingNumOfPlayers.createNumOfPlayersPage(font, halfButtonSize, labels, textFields,
                                                       buttonsList, guiActuator, miniGameModel);
            Label numOfPlayers = labels.get(0);
            TextField writeNumOfPlayers = textFields.get(3);
            Label invalidNumOfPlayers = labels.get(1);
            Button enterNumOfPlayers = buttonsList.get(5);
            Button goToNetwork = buttonsList.get(0);
            Label waitingForPlayers = labels.get(6);


            numOfPlayers.setVisible(true);
            writeNumOfPlayers.setVisible(true);
            enterNumOfPlayers.setVisible(true);
            goToNetwork.setVisible(true);
            waitingForPlayers.setVisible(false);
            loadingWheel.setVisible(false);
            invalidNumOfPlayers.setVisible(false);

            enterNumOfPlayers.setOnMouseClicked(event -> {
                int num = Integer.parseInt(writeNumOfPlayers.getCharacters().toString());
                if (num < 2 || num > 4) {
                    invalidNumOfPlayers.setVisible(true);
                } else if (miniGameModel.myName().equals(miniGameModel.getGodPlayer())) {
                    guiActuator.setNumOfPlayers(num);
                    waitingForPlayers.setVisible(true);
                    enterNumOfPlayers.setVisible(false);
                    numOfPlayers.setVisible(false);
                    writeNumOfPlayers.setVisible(false);
                    invalidNumOfPlayers.setVisible(false);
                    loadingWheel.setVisible(true);
                }
            });
        });
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

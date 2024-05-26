package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.GuiExceptionReceiver;
import it.polimi.ingsw.am11.view.client.GUI.GuiUpdater;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.parseInt;

public class CodexNaturalis extends Application {

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

        GuiExceptionReceiver exceptionReceiver = new GuiExceptionReceiver();
        GuiUpdater guiUpdater = new GuiUpdater(exceptionReceiver);
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
        lDBackground.setFitHeight(size);
        lDBackground.setPreserveRatio(true);

        ImageView lDSquare = guiResources.getTheImageView(GuiResEnum.LGIN_SQUARE);
        lDSquare.setFitHeight(size);
        lDSquare.setPreserveRatio(true);

        ImageView lDWritings = guiResources.getTheImageView(GuiResEnum.LGIN_WRITINGS);
        lDWritings.setFitHeight(size);
        lDWritings.setPreserveRatio(true);

        ImageView lDDisks = guiResources.getTheImageView(GuiResEnum.LGIN_DISK);
        lDDisks.setFitHeight(size);
        lDDisks.setPreserveRatio(true);

        ImageView wolf = guiResources.getTheImageView(GuiResEnum.WOLF_ICON);
        ImageView butterfly = guiResources.getTheImageView(GuiResEnum.BUTTERLFY_ICON);
        ImageView mushroom = guiResources.getTheImageView(GuiResEnum.MUSHROOM_ICON);
        ImageView leaf = guiResources.getTheImageView(GuiResEnum.LEAF_ICON);

        LoadingScreen loadingScreen = new LoadingScreen();

        SequentialTransition sqT = new SequentialTransition();
        ParallelTransition prT = new ParallelTransition();

        loadingScreen.animateLoadingScreen(size, wolf, butterfly, mushroom, leaf, lDWritings,
                                           lDDisks, lDSquare, lDBackground, prT,
                                           sqT);

        root.getChildren().addAll(lDBackground, lDSquare, lDWritings, lDDisks,
                                  wolf, butterfly, mushroom, leaf);

        //TODO -----------------

        // Socket text field

        VBox theBox = new VBox(2 * halfButtonSize);
        theBox.setAlignment(Pos.CENTER);

        javafx.scene.control.TextField ipAddress = new javafx.scene.control.TextField();
        ipAddress.setAlignment(Pos.CENTER);
        ipAddress.setPromptText("Ip Address");
        ipAddress.setFont(font);
        ipAddress.setStyle("-fx-background-color: #D7BC49; " +
                           "-fx-background-radius: 5;" +
                           " -fx-max-width: " + 20 * halfButtonSize);

        javafx.scene.control.TextField port = new javafx.scene.control.TextField();
        port.setAlignment(Pos.CENTER);
        port.setPromptText("Port");
        port.setFont(font);
        port.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                      " -fx-max-width: " + 20 * halfButtonSize);


        theBox.getChildren().addAll(ipAddress, port);
        theBox.setVisible(false);

        root.getChildren().addAll(theBox);

        // Label: socket or rmi
        Label connectionType = new Label();
        Label connectionFailed = new Label();

        NetworkPage networkPage = new NetworkPage();
        networkPage.createNetworkPage(font, halfButtonSize, connectionType,
                                      connectionFailed);

        root.getChildren().addAll(connectionFailed, connectionType);

        // TextField : Nickname and Label: Nickname and Label: Name already took

        TextField writeNick = new TextField();
        Label yourName = new Label("Your Name:");
        Label nameAlreadyTaken = new Label();

        SettingNick settingNick = new SettingNick();
        settingNick.createSettingNick(writeNick, font, halfButtonSize, yourName, fontBig,
                                      nameAlreadyTaken);

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

        // Choose Nickname and go back buttons
        goToNetwork.setOnAction(event -> {
            chooseRMI.setVisible(true);
            chooseSocket.setVisible(true);
            theBox.setVisible(true);
            connectionType.setVisible(true);
            joinButton.setVisible(true);

            writeNick.setVisible(false);
            goToNetwork.setVisible(false);
            chooseNick.setVisible(false);
            yourName.setVisible(false);
            nameAlreadyTaken.setVisible(false);
            enterNumOfPlayers.setVisible(false);
            goBack.setVisible(false);

        });

        chooseNick.setOnAction(event -> {
            nameAlreadyTaken.setVisible(true);
        });

        // Socket, Rmi and Join buttons
        chooseSocket.setOnAction(event -> {
            connectionType.setText("Socket");
        });

        chooseRMI.setOnAction(event -> {
            connectionType.setText("Rmi");
        });

        joinButton.setOnAction(event -> {
            String connectionTypeText = connectionType.getText().toLowerCase();
            String ip = ipAddress.getCharacters().toString();
            //TODO: handle exception
            int portNumber = parseInt(port.getCharacters().toString());
            if (ipAddress.getCharacters().toString().equals("Fail")) {
                connectionFailed.setVisible(true);
            } else {
                guiActuator.connect(connectionTypeText, ip, portNumber);
                chooseRMI.setVisible(false);
                chooseSocket.setVisible(false);
                theBox.setVisible(false);
                connectionFailed.setVisible(false);
                connectionType.setVisible(false);
                joinButton.setVisible(false);
                enterNumOfPlayers.setVisible(false);

                writeNick.setVisible(true);
                goToNetwork.setVisible(true);
                chooseNick.setVisible(true);
                yourName.setVisible(true);
            }
        });

        // Order of animation
        sqT.onFinishedProperty().set(event -> {
            chooseRMI.setVisible(true);
            chooseSocket.setVisible(true);
            chooseSocket.fire();
            joinButton.setVisible(true);
            theBox.setVisible(true);
        });

        Label numOfPlayers = new Label("Number of players:");
        Label invalidNumOfPlayers = new Label("Invalid number of players");
        TextField writeNumOfPlayers = new TextField();

        root.getChildren().addAll(numOfPlayers, writeNumOfPlayers, invalidNumOfPlayers);

        SettingNumOfPlayers settingNumOfPlayers = new SettingNumOfPlayers();
        settingNumOfPlayers.createNumOfPlayersPage(halfButtonSize, font, numOfPlayers,
                                                   writeNumOfPlayers, invalidNumOfPlayers);

        goBack.setOnMouseClicked(event -> {
            goToNetwork.setVisible(true);
            chooseNick.setVisible(true);
            writeNick.setVisible(true);
            yourName.setVisible(true);
            nameAlreadyTaken.setVisible(true);

            enterNumOfPlayers.setVisible(false);
            numOfPlayers.setVisible(false);
            writeNumOfPlayers.setVisible(false);
            goBack.setVisible(false);
        });

        AtomicInteger totalPlayers = new AtomicInteger();

        enterNumOfPlayers.setOnMouseClicked(event -> {
            int num = 0;
            try {
                num = Integer.parseInt(writeNumOfPlayers.getCharacters().toString());
            } catch (NumberFormatException e) {
                writeNumOfPlayers.setText("Fail");
            }
            if (writeNumOfPlayers.getCharacters().toString().equals("Fail")) {
                invalidNumOfPlayers.setVisible(true);
            } else {
                guiActuator.setNumOfPlayers(num);
                totalPlayers.set(parseInt(writeNumOfPlayers.getCharacters().toString()));
                enterNumOfPlayers.setVisible(false);
                goBack.setVisible(false);
                numOfPlayers.setVisible(false);
                writeNumOfPlayers.setVisible(false);
            }
        });

        AtomicInteger currentPlayers = new AtomicInteger();

        chooseNick.setOnMouseClicked(event -> {
            String nick = writeNick.getCharacters().toString();
            if (writeNick.getCharacters().toString().equals("Fail")) {
                nameAlreadyTaken.setVisible(true);
            } else {
                guiActuator.setName(nick);
                chooseNick.setVisible(false);
                goToNetwork.setVisible(false);
                writeNick.setVisible(false);
                yourName.setVisible(false);
                nameAlreadyTaken.setVisible(false);

                enterNumOfPlayers.setVisible(true);
                numOfPlayers.setVisible(true);
                writeNumOfPlayers.setVisible(true);
                goBack.setVisible(true);

                currentPlayers.getAndIncrement();
            }
        });

        Label waitingForPlayers = new Label("Waiting for other players to join...");
        ProgressBar progressBar = new ProgressBar();

        WaitingRoom waitingRoom = new WaitingRoom();
        waitingRoom.createWaitingRoom(waitingForPlayers, progressBar, font);

        root.getChildren().addAll(waitingForPlayers, progressBar);
        StackPane.setAlignment(waitingForPlayers, Pos.CENTER);

        waitingRoom.updateProgressBar(currentPlayers.get(), totalPlayers.get());

        waitingForPlayers.setVisible(false);
        progressBar.setVisible(false);

        // Let's set the frame of the stage and the icons and add it
        frameHandler = new FrameHandler(guiResources, stage, root);

        // Set scene and stage
        Scene scene = new Scene(root, size, size, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

}

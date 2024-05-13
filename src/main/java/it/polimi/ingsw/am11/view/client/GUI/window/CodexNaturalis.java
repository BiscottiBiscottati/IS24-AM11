package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

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


        root.getChildren().add(lDBackground);
        root.getChildren().add(lDDisks);
        root.getChildren().add(lDSquare);
        root.getChildren().add(lDWritings);

        int symbolSize = size / 8;

        root.getChildren().addAll(wolf, butterfly, mushroom, leaf);
        wolf.setFitHeight(symbolSize);
        wolf.setPreserveRatio(true);
        wolf.setTranslateX(117.0 * size / 512.0);
        wolf.setTranslateY(- 121.0 * size / 512.0);
        butterfly.setFitHeight(symbolSize);
        butterfly.setPreserveRatio(true);
        butterfly.setTranslateX(- 122.0 * size / 512.0);
        butterfly.setTranslateY(118.0 * size / 512.0);
        mushroom.setFitHeight(symbolSize);
        mushroom.setPreserveRatio(true);
        mushroom.setTranslateX(119.0 * size / 512.0);
        mushroom.setTranslateY(117.0 * size / 512.0);
        leaf.setFitHeight(symbolSize);
        leaf.setPreserveRatio(true);
        leaf.setTranslateX(- 123.0 * size / 512.0);
        leaf.setTranslateY(- 121.0 * size / 512.0);

        // Make the writings fade away
        Duration FadeTime = Duration.millis(3000);

        FadeTransition WritingsFt = new FadeTransition(FadeTime, lDWritings);
        WritingsFt.setToValue(0.0);

        // Make square and disks rotate
        Duration RotationTime = Duration.millis(5000);

        Rotate lbSelfRotation = new Rotate();
        lbSelfRotation.setPivotX(size / 2);
        lbSelfRotation.setPivotY(size / 2);

        Timeline lbRotationTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                          new KeyValue(
                                                                  lbSelfRotation.angleProperty(),
                                                                  0)),
                                             new KeyFrame(RotationTime,
                                                          new KeyValue(
                                                                  lbSelfRotation.angleProperty(),
                                                                  45)));
        lDDisks.getTransforms().add(lbSelfRotation);
        lDSquare.getTransforms().add(lbSelfRotation);

        // Make symbols rotate around their centers

        Rotate rotate = new Rotate();

        Timeline symbolsSelfRotate = new Timeline(new KeyFrame(Duration.ZERO,
                                                               new KeyValue(rotate.angleProperty(),
                                                                            0)),
                                                  new KeyFrame(RotationTime,
                                                               new KeyValue(rotate.angleProperty(),
                                                                            - 45)));

        // Make symbols rotate around stage center

        Rotate wolfCenterRt = new Rotate();
        Rotate leafCenterRt = new Rotate();
        Rotate mushroomCenterRt = new Rotate();
        Rotate butterflyCenterRt = new Rotate();

        leafCenterRt.setPivotX(123.0 * size / 512.0);
        leafCenterRt.setPivotY(121.0 * size / 512.0);
        leaf.getTransforms().add(leafCenterRt);

        mushroomCenterRt.setPivotX(- 119.0 * size / 512.0);
        mushroomCenterRt.setPivotY(- 117.0 * size / 512.0);
        mushroom.getTransforms().add(mushroomCenterRt);

        wolfCenterRt.setPivotX(- 117.0 * size / 512.0);
        wolfCenterRt.setPivotY(121.0 * size / 512.0);
        wolf.getTransforms().add(wolfCenterRt);

        butterflyCenterRt.setPivotX(122.0 * size / 512.0);
        butterflyCenterRt.setPivotY(- 118.0 * size / 512.0);
        butterfly.getTransforms().add(butterflyCenterRt);


        Timeline leafTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                    new KeyValue(leafCenterRt.angleProperty(), 0)),
                                       new KeyFrame(RotationTime,
                                                    new KeyValue(leafCenterRt.angleProperty(),
                                                                 45)));
        Timeline mushroomTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                        new KeyValue(
                                                                mushroomCenterRt.angleProperty(),
                                                                0)),
                                           new KeyFrame(RotationTime,
                                                        new KeyValue(
                                                                mushroomCenterRt.angleProperty(),
                                                                45)));
        Timeline butterflyTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                         new KeyValue(
                                                                 butterflyCenterRt.angleProperty(),
                                                                 0)),
                                            new KeyFrame(RotationTime,
                                                         new KeyValue(
                                                                 butterflyCenterRt.angleProperty(),
                                                                 45)));
        Timeline wolfTl = new Timeline(new KeyFrame(Duration.ZERO,
                                                    new KeyValue(wolfCenterRt.angleProperty(), 0)),
                                       new KeyFrame(RotationTime,
                                                    new KeyValue(wolfCenterRt.angleProperty(),
                                                                 45)));

        mushroom.getTransforms().add(rotate);
        wolf.getTransforms().add(rotate);
        leaf.getTransforms().add(rotate);
        butterfly.getTransforms().add(rotate);

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
        connectionType.setBackground(Background.EMPTY);
        connectionType.setFont(font);
        connectionType.setTranslateY(- 6 * halfButtonSize);

        root.getChildren().add(connectionType);

        // Label: connection failed

        Label connectionFailed = new Label();
        connectionFailed.setBackground(Background.EMPTY);
        connectionFailed.setTranslateY(6 * halfButtonSize);
        connectionFailed.setText("Server not found");
        connectionFailed.setTextFill(Color.RED);
        connectionFailed.setVisible(false);

        root.getChildren().add(connectionFailed);

        // TextField : Nickname and Label: Nickname and Label: Name already took

        TextField writeNick = new TextField();
        writeNick.setAlignment(Pos.CENTER);
        writeNick.setPromptText("Nickname");
        writeNick.setFont(font);
        writeNick.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                           " -fx-max-width: " + 20 * halfButtonSize);

        Label yourName = new Label("Your Name:");
        yourName.setBackground(Background.EMPTY);
        yourName.setFont(fontBig);
        yourName.setTranslateY(- 4 * halfButtonSize);


        Label nameAlreadyTaken = new Label();
        nameAlreadyTaken.setBackground(Background.EMPTY);
        nameAlreadyTaken.setTranslateY(6 * halfButtonSize);
        nameAlreadyTaken.setText("Name already taken");
        nameAlreadyTaken.setTextFill(Color.RED);

        yourName.setVisible(false);
        writeNick.setVisible(false);
        nameAlreadyTaken.setVisible(false);

        root.getChildren().addAll(yourName, writeNick, nameAlreadyTaken);

        // Buttons declarations
        Button goToNetwork = new Button("_Cancel");
        Button chooseNick = new Button("_Cancel");
        Button chooseSocket = new Button("_Cancel");
        Button chooseRMI = new Button("_Cancel");
        Button joinButton = new Button("_Cancel");

        // Choose Nickname and go back buttons

        goToNetwork.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        goToNetwork.setTranslateX(- 5 * halfButtonSize);
        goToNetwork.setTranslateY(10 * halfButtonSize - distanceToBorder);
        goToNetwork.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        goToNetwork.setText("go Back");
        goToNetwork.setFont(font);
        goToNetwork.setTextFill(Color.web("#351F17"));
        goToNetwork.setOnMousePressed(
                event -> goToNetwork.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        goToNetwork.setOnMouseReleased(event -> goToNetwork.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
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

        });


        chooseNick.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        chooseNick.setTranslateX(5 * halfButtonSize);
        chooseNick.setTranslateY(10 * halfButtonSize - distanceToBorder);
        chooseNick.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chooseNick.setText("Choose");
        chooseNick.setFont(font);
        chooseNick.setTextFill(Color.web("#351F17"));
        chooseNick.setOnMousePressed(
                event -> chooseNick.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chooseNick.setOnMouseReleased(event -> chooseNick.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
        chooseNick.setOnAction(event -> {
            nameAlreadyTaken.setVisible(true);
        });

        chooseNick.setVisible(false);
        goToNetwork.setVisible(false);
        root.getChildren().addAll(chooseNick, goToNetwork);

        // Socket, Rmi and Join buttons


        chooseSocket.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        chooseSocket.setTranslateX(- 5 * halfButtonSize);
        chooseSocket.setTranslateY(- 10 * halfButtonSize);
        chooseSocket.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chooseSocket.setText("Socket");
        chooseSocket.setFont(font);
        chooseSocket.setTextFill(Color.web("#351F17"));
        chooseSocket.setOnMousePressed(
                event -> chooseSocket.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chooseSocket.setOnMouseReleased(event -> chooseSocket.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
        chooseSocket.setOnAction(event -> {
            connectionType.setText("Socket");
        });


        chooseRMI.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        chooseRMI.setTranslateX(5 * halfButtonSize);
        chooseRMI.setTranslateY(- 10 * halfButtonSize);
        chooseRMI.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chooseRMI.setText("Rmi");
        chooseRMI.setFont(font);
        chooseRMI.setTextFill(Color.web("#351F17"));
        chooseRMI.setOnMousePressed(
                event -> chooseRMI.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chooseRMI.setOnMouseReleased(event -> chooseRMI.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
        chooseRMI.setOnAction(event -> {
            connectionType.setText("Rmi");
        });


        joinButton.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        joinButton.setTranslateY(10 * halfButtonSize - distanceToBorder);
        joinButton.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        joinButton.setText("Join");
        joinButton.setFont(font);
        joinButton.setTextFill(Color.web("#351F17"));
        joinButton.setOnMousePressed(
                event -> joinButton.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        joinButton.setOnMouseReleased(event -> joinButton.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
        joinButton.setOnAction(event -> {
            //TODO
            if (ipAddress.getCharacters().toString().equals("Fail")) {
                connectionFailed.setVisible(true);
            } else {

                chooseRMI.setVisible(false);
                chooseSocket.setVisible(false);
                theBox.setVisible(false);
                connectionFailed.setVisible(false);
                connectionType.setVisible(false);
                joinButton.setVisible(false);

                writeNick.setVisible(true);
                goToNetwork.setVisible(true);
                chooseNick.setVisible(true);
                yourName.setVisible(true);


            }


        });


        chooseRMI.setVisible(false);
        chooseSocket.setVisible(false);
        joinButton.setVisible(false);
        root.getChildren().addAll(chooseRMI, chooseSocket, joinButton);


        // Order of animation
        SequentialTransition sqT = new SequentialTransition();
        ParallelTransition prT = new ParallelTransition();

        prT.getChildren().addAll(lbRotationTl, symbolsSelfRotate, leafTl, mushroomTl,
                                 butterflyTl, wolfTl);
        sqT.getChildren().addAll(WritingsFt, prT);

        lDSquare.setOnMouseClicked(event -> {
            sqT.play();
            lDBackground.setOnMouseClicked(e -> {
                sqT.jumpTo("end");
            });
            lDSquare.setDisable(true);
            lDWritings.setDisable(true);

        });
        lDWritings.setOnMouseClicked(event -> {
            sqT.play();
            lDBackground.setOnMouseClicked(e -> {
                sqT.jumpTo("end");
            });
            lDWritings.setDisable(true);
            lDSquare.setDisable(true);
        });

        sqT.onFinishedProperty().set(event -> {
            chooseRMI.setVisible(true);
            chooseSocket.setVisible(true);
            chooseSocket.fire();
            joinButton.setVisible(true);
            theBox.setVisible(true);
        });

        // Let's set the frame of the stage and the icons and add it
        FrameHandler frameHandler = new FrameHandler(guiResources, stage, root);

        // Set scene and stage

        Scene scene = new Scene(root, size, size, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

}

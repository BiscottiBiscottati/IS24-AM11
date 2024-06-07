package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SetNickPage {
    private final CodexNaturalis codexNaturalis;
    MiniGameModel miniGameModel;
    GuiActuator guiActuator;
    TextField writeNick;
    Label yourName, nameAlreadyTaken;
    Font font, fontBig;
    Button chooseNick;
    Button goToNetwork;
    int halfButtonSize, distanceToBorder;


    public SetNickPage(CodexNaturalis codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createSettingNickPage() {
        guiActuator = codexNaturalis.getGuiActuator();
        StackPane root = codexNaturalis.getRoot();
        miniGameModel = codexNaturalis.getMiniGameModel();
        font = codexNaturalis.getFont();
        fontBig = codexNaturalis.getFontBig();
        halfButtonSize = codexNaturalis.getHalfButtonSize();
        distanceToBorder = codexNaturalis.getDistanceToBorder();
        writeNick = new TextField();

        writeNick.setAlignment(Pos.CENTER);
        writeNick.setPromptText("Nickname");
        writeNick.setFont(font);
        writeNick.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                           " -fx-max-width: " + 20 * halfButtonSize);

        yourName = new Label("Your Name:");
        nameAlreadyTaken = new Label();

        yourName.setBackground(Background.EMPTY);
        yourName.setFont(fontBig);
        yourName.setTranslateY(- 4 * halfButtonSize);

        nameAlreadyTaken.setBackground(Background.EMPTY);
        nameAlreadyTaken.setTranslateY(6 * halfButtonSize);
        nameAlreadyTaken.setText("Name already taken");
        nameAlreadyTaken.setTextFill(Color.RED);

        root.getChildren().addAll(yourName, writeNick, nameAlreadyTaken);
        chooseNick = new Button("_Cancel");
        goToNetwork = new Button("_Cancel");

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

        root.getChildren().addAll(chooseNick, goToNetwork);

        writeNick.setVisible(false);
        yourName.setVisible(false);
        nameAlreadyTaken.setVisible(false);
        chooseNick.setVisible(false);
        goToNetwork.setVisible(false);
    }

    public void showSettingNickPage() {

        yourName.setVisible(true);
        writeNick.setVisible(true);
        nameAlreadyTaken.setVisible(false);
        chooseNick.setVisible(true);
        goToNetwork.setVisible(true);

        chooseNick.setOnMouseClicked(event -> {
            String nick = writeNick.getCharacters().toString();
            if (nick.isEmpty()) {
                writeNick.setPromptText("Fail");
                nameAlreadyTaken.setText("Name cannot be empty");
                nameAlreadyTaken.setVisible(true);
            } else {
                miniGameModel.setMyName(nick);
                guiActuator.setName(nick);
                chooseNick.setVisible(false);
                writeNick.setVisible(false);
                yourName.setVisible(false);
                nameAlreadyTaken.setVisible(false);
                goToNetwork.setVisible(false);
                codexNaturalis.showWaitingRoomPage();
            }
        });

        goToNetwork.setOnMouseClicked(event -> {
            chooseNick.setVisible(false);
            writeNick.setVisible(false);
            yourName.setVisible(false);
            nameAlreadyTaken.setVisible(false);
            goToNetwork.setVisible(false);

            codexNaturalis.showNetworkPage();
        });
    }
}

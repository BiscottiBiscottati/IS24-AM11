package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class SettingNick {

    public void createSettingNick(Font font, int halfButtonSize, Font fontBig, List<Label> labels,
                                  List<TextField> textFields, List<Button> buttonList,
                                  GuiActuator guiActuator, VBox theBox,
                                  ProgressIndicator loadingWheel,
                                  MiniGameModel miniGameModel) {

        TextField writeNick = textFields.getFirst();
        Label yourName = labels.get(2);
        Label nameAlreadyTaken = labels.get(3);

        writeNick.setAlignment(Pos.CENTER);
        writeNick.setPromptText("Nickname");
        writeNick.setFont(font);
        writeNick.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                           " -fx-max-width: " + 20 * halfButtonSize);

        yourName.setBackground(Background.EMPTY);
        yourName.setFont(fontBig);
        yourName.setTranslateY(- 4 * halfButtonSize);

        nameAlreadyTaken.setBackground(Background.EMPTY);
        nameAlreadyTaken.setTranslateY(6 * halfButtonSize);
        nameAlreadyTaken.setText("Name already taken");
        nameAlreadyTaken.setTextFill(Color.RED);

        yourName.setVisible(false);
        writeNick.setVisible(false);
        nameAlreadyTaken.setVisible(false);

        Button goToNetwork = buttonList.get(0);
        Button chooseNick = buttonList.get(1);
        Button goBack = buttonList.get(6);
        Button enterNumOfPlayers = buttonList.get(5);
        Button chooseRMI = buttonList.get(3);
        Button chooseSocket = buttonList.get(2);
        Button joinButton = buttonList.get(4);
        Label connectionType = labels.get(4);

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

        Label waitingForPlayers = labels.get(6);

        chooseNick.setOnMouseClicked(event -> {
            String nick = writeNick.getCharacters().toString();
            if (nick.equals("")) {
                writeNick.setPromptText("Fail");
                nameAlreadyTaken.setText("Name cannot be empty");
                nameAlreadyTaken.setVisible(true);
            }
            miniGameModel.setMyName(nick);
            guiActuator.setName(nick);
            chooseNick.setVisible(false);
            goToNetwork.setVisible(false);
            writeNick.setVisible(false);
            yourName.setVisible(false);
            nameAlreadyTaken.setVisible(false);

            waitingForPlayers.setVisible(true);
            loadingWheel.setVisible(true);
        });
    }
}

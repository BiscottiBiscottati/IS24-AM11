package it.polimi.ingsw.am11.view.client.GUI.window;

import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class SettingNumOfPlayers {

    public void createNumOfPlayersPage(Font font, int halfButtonSize, List<Label> labels,
                                       List<TextField> textFields,
                                       List<Button> buttonList, GuiActuator guiActuator,
                                       MiniGameModel miniGameModel) {
        Label numOfPlayers = labels.get(0);
        TextField writeNumOfPlayers = textFields.get(3);
        Label invalidNumOfPlayers = labels.get(1);

        numOfPlayers.setBackground(Background.EMPTY);
        numOfPlayers.setFont(font);
        numOfPlayers.setTranslateY(- 4 * halfButtonSize);

        writeNumOfPlayers.setAlignment(Pos.CENTER);
        writeNumOfPlayers.setPromptText("Number of players");
        writeNumOfPlayers.setFont(font);
        writeNumOfPlayers.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                                   " -fx-max-width: " + 20 * halfButtonSize);

        invalidNumOfPlayers.setBackground(Background.EMPTY);
        invalidNumOfPlayers.setTranslateY(6 * halfButtonSize);
        invalidNumOfPlayers.setText("Invalid number of players");
        invalidNumOfPlayers.setTextFill(Color.RED);
        invalidNumOfPlayers.setVisible(false);

        numOfPlayers.setVisible(false);
        writeNumOfPlayers.setVisible(false);
        invalidNumOfPlayers.setVisible(false);

        Button goToNetwork = buttonList.get(0);
        Button chooseNick = buttonList.get(1);
        Button goBack = buttonList.get(6);
        Button enterNumOfPlayers = buttonList.get(5);
        TextField writeNick = textFields.getFirst();
        Label yourName = labels.get(2);
        Label nameAlreadyTaken = labels.get(3);

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

        enterNumOfPlayers.setOnMouseClicked(event -> {
            if (miniGameModel.myName().equals(miniGameModel.getGodPlayer())) {
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
                    enterNumOfPlayers.setVisible(false);
                    goBack.setVisible(false);
                    numOfPlayers.setVisible(false);
                    writeNumOfPlayers.setVisible(false);
                }
            }
        });
    }
}

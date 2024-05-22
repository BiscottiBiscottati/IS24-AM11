package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SettingNumOfPlayers {

    public void createNumOfPlayersPage(int halfButtonSize, Font font,
                                       Label numOfPlayers, TextField writeNumOfPlayers,
                                       Label invalidNumOfPlayers) {

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
    }
}

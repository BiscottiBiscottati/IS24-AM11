package it.polimi.ingsw.am11.view.client.GUI.window;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SettingNick {

    public void createSettingNick(TextField writeNick, Font font, int halfButtonSize,
                                  Label yourName, Font fontBig, Label nameAlreadyTaken) {
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
    }
}

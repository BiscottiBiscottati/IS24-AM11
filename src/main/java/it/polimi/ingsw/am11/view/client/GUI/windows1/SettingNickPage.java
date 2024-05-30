package it.polimi.ingsw.am11.view.client.GUI.windows1;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis1;
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

public class SettingNickPage {
    private final CodexNaturalis1 codexNaturalis;
    MiniGameModel miniGameModel;
    GuiActuator guiActuator;
    TextField writeNick;
    Label yourName, nameAlreadyTaken;
    Font font, fontBig;
    Button chooseNick;
    int halfButtonSize;


    public SettingNickPage(CodexNaturalis1 codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createSettingNickPage() {
        StackPane root = codexNaturalis.getRoot();
        miniGameModel = codexNaturalis.getMiniGameModel();
        font = codexNaturalis.getFont();
        fontBig = codexNaturalis.getFontBig();
        halfButtonSize = codexNaturalis.getHalfButtonSize();
        writeNick = new TextField();
        yourName = new Label("Your Name:");
        nameAlreadyTaken = new Label();
        root.getChildren().addAll(yourName, writeNick, nameAlreadyTaken);
        chooseNick = new Button("_Cancel");
        root.getChildren().addAll(chooseNick);
    }

    public void showSettingNickPage() {
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

        yourName.setVisible(true);
        writeNick.setVisible(true);
        nameAlreadyTaken.setVisible(false);
        chooseNick.setVisible(true);
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

                //goToWaitingRommPage();
            }
        });
    }
}

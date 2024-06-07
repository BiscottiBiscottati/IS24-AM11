package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SetNumOfPlayersPage {
    private final CodexNaturalis codexNaturalis;
    GuiActuator guiActuator;
    Label numOfPlayers, invalidNumOfPlayers;
    Font font;
    int halfButtonSize, distanceToBorder;
    TextField writeNumOfPlayers;
    Button enterNumOfPlayers;
    StackPane root;

    public SetNumOfPlayersPage(CodexNaturalis codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createNumOfPlayersPage() {
        font = codexNaturalis.getFont();
        halfButtonSize = codexNaturalis.getHalfButtonSize();
        guiActuator = codexNaturalis.getGuiActuator();
        distanceToBorder = codexNaturalis.getDistanceToBorder();
        root = codexNaturalis.getRoot();

        numOfPlayers = new Label("Number of players:");
        numOfPlayers.setBackground(Background.EMPTY);
        numOfPlayers.setFont(font);
        numOfPlayers.setTranslateY(- 4 * halfButtonSize);

        writeNumOfPlayers = new TextField();
        writeNumOfPlayers.setAlignment(Pos.CENTER);
        writeNumOfPlayers.setPromptText("Number of players");
        writeNumOfPlayers.setFont(font);
        writeNumOfPlayers.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5;" +
                                   " -fx-max-width: " + 20 * halfButtonSize);

        invalidNumOfPlayers = new Label("Invalid number of players");
        invalidNumOfPlayers.setBackground(Background.EMPTY);
        invalidNumOfPlayers.setTranslateY(6 * halfButtonSize);
        invalidNumOfPlayers.setText("Invalid number of players");
        invalidNumOfPlayers.setTextFill(Color.RED);
        invalidNumOfPlayers.setVisible(false);

        numOfPlayers.setVisible(false);
        writeNumOfPlayers.setVisible(false);
        invalidNumOfPlayers.setVisible(false);

        enterNumOfPlayers = new Button();
        enterNumOfPlayers.setPrefSize(halfButtonSize << 3, halfButtonSize << 1);
        enterNumOfPlayers.setTranslateY(100);
        enterNumOfPlayers.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        enterNumOfPlayers.setText("Enter");
        enterNumOfPlayers.setFont(font);
        enterNumOfPlayers.setTextFill(Color.web("#351F17"));
        enterNumOfPlayers.setOnMousePressed(
                event -> enterNumOfPlayers.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        enterNumOfPlayers.setOnMouseReleased(event -> enterNumOfPlayers.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
        enterNumOfPlayers.setVisible(false);

        root.getChildren().addAll(numOfPlayers, writeNumOfPlayers, invalidNumOfPlayers,
                                  enterNumOfPlayers);
    }

    public void showSetNumOfPlayersPage() {
        codexNaturalis.hideWaitingRoomPage();
        numOfPlayers.setVisible(true);
        writeNumOfPlayers.setVisible(true);
        enterNumOfPlayers.setVisible(true);
        enterNumOfPlayers.setOnMouseClicked(event -> {
            int num = Integer.parseInt(writeNumOfPlayers.getCharacters().toString());
            if (num < 2 || num > 4) {
                invalidNumOfPlayers.setVisible(true);
            } else {
                guiActuator.setNumOfPlayers(num);
                enterNumOfPlayers.setVisible(false);
                numOfPlayers.setVisible(false);
                writeNumOfPlayers.setVisible(false);
                codexNaturalis.showWaitingRoomPage();
                invalidNumOfPlayers.setVisible(false);
            }
        });
    }
}

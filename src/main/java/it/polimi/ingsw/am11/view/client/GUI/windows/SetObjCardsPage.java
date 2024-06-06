package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.Set;

public class SetObjCardsPage {

    private final CodexNaturalis codexNaturalis;
    Label chooseObj;
    ImageView obj1;
    ImageView obj2;
    HBox objBox;

    public SetObjCardsPage(CodexNaturalis codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
    }

    public void createObjCardsWindow(Set<Integer> cardId) {

        Font font = codexNaturalis.getFont();

        chooseObj.setText("Choose your objective cards:");
        chooseObj.setTranslateY(- 60);
        chooseObj.setTranslateX(80);
        chooseObj.setPrefSize(400, 50);
        chooseObj.setFont(font);
        chooseObj.setStyle("-fx-font-size: 30");
        chooseObj.setStyle("-fx-text-fill: #D7BC49");
        chooseObj.setStyle("-fx-background-color: #351F17");
        chooseObj.setStyle("-fx-background-radius: 5");

        obj1.setFitHeight(200);
        obj1.setFitWidth(150);

        obj2.setFitHeight(200);
        obj2.setFitWidth(150);

        objBox.getChildren().addAll(obj1, obj2, chooseObj);

        StackPane root = codexNaturalis.getRoot();

        root.getChildren().add(objBox);
        objBox.setVisible(false);


    }

    public void showObjCardsWindow() {
        // Show the objective cards
    }
}

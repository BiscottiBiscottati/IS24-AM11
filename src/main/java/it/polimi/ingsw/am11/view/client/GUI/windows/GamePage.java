package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResEnum;
import it.polimi.ingsw.am11.view.client.GUI.utils.GuiResources;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class GamePage {

    private static final Logger log = LoggerFactory.getLogger(GamePage.class);
    GuiResources guiResources;
    MiniGameModel miniGameModel;
    CodexNaturalis codexNaturalis;

    @FXML
    Label decksLabel;
    @FXML
    StackPane gamePage;
    @FXML
    ImageView GPBackground;
    @FXML
    Label player1;
    @FXML
    Label player2;
    @FXML
    Label player3;
    @FXML
    Label player4;
    @FXML
    Label pointsPl1;
    @FXML
    Label pointsPl2;
    @FXML
    Label pointsPl3;
    @FXML
    Label pointsPl4;
    @FXML
    Label visiblesLabel;
    @FXML
    Label handLabel;
    @FXML
    Button chatButton;
    @FXML
    Label objLabel;
    @FXML
    Label personalObjLabel;
    @FXML
    ImageView goldDeck;
    @FXML
    ImageView resourceDeck;
    @FXML
    ImageView personalObj;
    @FXML
    ImageView handCard1;
    @FXML
    ImageView handCard2;
    @FXML
    ImageView handCard3;
    @FXML
    ImageView goldVis1;
    @FXML
    ImageView goldVis2;
    @FXML
    ImageView resVis1;
    @FXML
    ImageView resVis2;
    @FXML
    Pane cardField;


    public GamePage() throws IOException {
    }

    public static void showGamePage(@NotNull Parent root1) {
        root1.setVisible(true);
    }

    public void createGamePage(@NotNull CodexNaturalis codexNaturalis) throws IOException {

        this.codexNaturalis = codexNaturalis;
        this.miniGameModel = codexNaturalis.getMiniGameModel();
        Font font = codexNaturalis.getFont();
        guiResources = codexNaturalis.getGuiResources();

        GPBackground = guiResources.getTheImageView(GuiResEnum.GAME_BACKGROUND);


        List<Label> labels = List.of(decksLabel, visiblesLabel, handLabel, objLabel,
                                     personalObjLabel);
        for (Label label : labels) {
            label.setFont(font);
            label.setStyle("-fx-font-size: 30");
            label.setStyle("-fx-text-fill: #D7BC49");
            label.setStyle("-fx-background-color: #351F17");
            label.setStyle("-fx-background-radius: 5");
        }

        // Get the stream of player names
        Stream<String> playerStream = codexNaturalis.getMiniGameModel().getPlayers().stream();

        List<Label> playerLabels = List.of(player1, player2, player3, player4);

        List<String> playerNames = playerStream.toList();

        int size = playerNames.size();

        for (int i = 0; i < size; i++) {
            System.out.println("Player name: " + playerNames.get(i));
            playerLabels.get(i).setText(playerNames.get(i));
            playerLabels.get(i).setFont(font);
            playerLabels.get(i).setStyle("-fx-font-size: 30");
            playerLabels.get(i).setStyle("-fx-text-fill: #D7BC49");
            playerLabels.get(i).setStyle("-fx-background-color: #351F17");
            playerLabels.get(i).setStyle("-fx-background-radius: 5");
        }

        List<Label> pointsLabels = List.of(pointsPl1, pointsPl2, pointsPl3, pointsPl4);

        for (int i = 0; i < size; i++) {
            pointsLabels.get(i).setText("0");
            pointsLabels.get(i).setFont(font);
            pointsLabels.get(i).setStyle("-fx-font-size: 30");
            pointsLabels.get(i).setStyle("-fx-text-fill: #D7BC49");
            pointsLabels.get(i).setStyle("-fx-background-color: #351F17");
            pointsLabels.get(i).setStyle("-fx-background-radius: 5");
        }

        chatButton.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chatButton.setFont(font);
        chatButton.setTextFill(Color.web("#351F17"));
        chatButton.setOnMousePressed(
                event -> chatButton.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chatButton.setOnMouseReleased(event -> chatButton.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
    }


    public void updateDeckTop() {
        Platform.runLater(() -> {
            log.info("Deck top updated");
            it.polimi.ingsw.am11.model.cards.utils.enums.Color color1 =
                    miniGameModel.table().getDeckTop(PlayableCardType.RESOURCE);
            System.out.println(color1 + "_res");
            Image resourceDeckImage = GuiResources.getTopDeck(PlayableCardType.RESOURCE, color1);
            if (resourceDeckImage != null) {
                resourceDeck.setImage(resourceDeckImage);
            }
            it.polimi.ingsw.am11.model.cards.utils.enums.Color color2 =
                    miniGameModel.table().getDeckTop(PlayableCardType.GOLD);
            System.out.println(color2 + "_gold");
            Image goldDeckImage = GuiResources.getTopDeck(PlayableCardType.GOLD, color2);
            if (goldDeckImage != null) {
                goldDeck.setImage(goldDeckImage);
            }
            resourceDeck.getParent().layout();
            goldDeck.getParent().layout();
        });
    }

    public void updatePersonalObjective() {
        Platform.runLater(() -> {
            log.info("Personal objective updated");
            Set<Integer> cardIdSet =
                    miniGameModel.getCliPlayer(
                            miniGameModel.myName()).getSpace().getPlayerObjective();
            int cardId = cardIdSet.iterator().next();
            System.out.println("Card id: " + cardId);
            Image personalObjImage = guiResources.getCardImage(cardId);

            if (personalObjImage != null) {
                personalObj.setImage(personalObjImage);
                personalObj.getParent().layout();
            }
        });
    }

    public void updateHand() {
        Platform.runLater(() -> {
            log.info("Hand updated");
            Set<Integer> hand = miniGameModel.getCliPlayer(
                    miniGameModel.myName()).getSpace().getPlayerHand();
            for (int cardId : hand) {
                Image cardImage = guiResources.getCardImage(cardId);
                if (cardImage != null) {
                    if (handCard1.getImage() == null) {
                        handCard1.setImage(cardImage);
                    } else if (handCard2.getImage() == null) {
                        handCard2.setImage(cardImage);
                    } else if (handCard3.getImage() == null) {
                        handCard3.setImage(cardImage);
                    }
                }
            }
        });
    }

    public void updateShownPlayable() {
        Platform.runLater(() -> {
            log.info("Shown playable updated");
            Set<Integer> shownPlayable = miniGameModel.table().getShownCards();
            System.out.println("Shown playable: " + shownPlayable);
            for (int cardId : shownPlayable) {
                Image cardImage = guiResources.getCardImage(cardId);
                if (cardImage != null) {
                    if (goldVis1.getImage() == null) {
                        goldVis1.setImage(cardImage);
                    } else if (goldVis2.getImage() == null) {
                        goldVis2.setImage(cardImage);
                    } else if (resVis1.getImage() == null) {
                        resVis1.setImage(cardImage);
                    } else if (resVis2.getImage() == null) {
                        resVis2.setImage(cardImage);
                    }
                }
            }
        });
    }

    public void placeStarterCard() {
        int cardId = miniGameModel.getCliPlayer(miniGameModel.myName()).getSpace().getStarterCard();
        boolean isRetro = miniGameModel.getCliPlayer(miniGameModel.myName()).getSpace().getStarterIsRetro();
        if (!isRetro) {
            Image cardImage = guiResources.getCardImage(cardId);
            ImageView starterCard = new ImageView(cardImage);
            starterCard.setFitHeight(100);
            starterCard.setFitWidth(150);
            cardField.getChildren().add(starterCard);
            if (cardImage != null) {
                cardField.getChildren().add(starterCard);
                cardField.getParent().layout();


            }
        } else if (isRetro) {
            ImageView starterRetro = guiResources.getCardImageRetro(cardId);
            starterRetro.setFitHeight(100);
            starterRetro.setFitWidth(150);
            cardField.getChildren().add(starterRetro);
            if (starterRetro != null) {
                cardField.getChildren().add(starterRetro);
                cardField.getParent().layout();
            }
        }
    }
}


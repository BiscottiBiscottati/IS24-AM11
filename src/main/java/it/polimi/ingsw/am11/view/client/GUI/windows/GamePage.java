package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.*;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import it.polimi.ingsw.am11.view.client.miniModel.utils.CardInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    GuiActuator guiActuator;
    Set<Integer> shownPlayable;
    Set<Integer> hand;
    Set<Position> availablePositions;
    Position selectedPosition;
    int centreX;
    int centreY;

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
    ImageView vis1;
    @FXML
    ImageView vis2;
    @FXML
    ImageView vis3;
    @FXML
    ImageView vis4;
    @FXML
    StackPane cardField;
    @FXML
    ImageView commonObj1;
    @FXML
    ImageView commonObj2;


    public GamePage() throws IOException {
    }

    public static void showGamePage(@NotNull Parent root1) {
        root1.setVisible(true);
    }

    public void createGamePage(@NotNull CodexNaturalis codexNaturalis) throws IOException {

        this.codexNaturalis = codexNaturalis;
        this.miniGameModel = codexNaturalis.getMiniGameModel();
        Font font = FontManager.getFont(FontsEnum.CLOISTER_BLACK, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));

        guiActuator = codexNaturalis.getGuiActuator();

        GPBackground = GuiResources.getTheImageView(GuiResEnum.GAME_BACKGROUND);


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

        centreX = (int) (cardField.getWidth() / 2);
        centreY = (int) (cardField.getHeight() / 2);

        chatButton.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        chatButton.setFont(font);
        chatButton.setTextFill(Color.web("#351F17"));
        chatButton.setOnMousePressed(
                event -> chatButton.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        chatButton.setOnMouseReleased(event -> chatButton.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
    }


    public void updateDeckTop(PlayableCardType type,
                              it.polimi.ingsw.am11.model.cards.utils.enums.Color color) {
        Platform.runLater(() -> {
            if (type == PlayableCardType.RESOURCE) {
                Image resourceDeckImage = GuiResources.getTopDeck(PlayableCardType.RESOURCE, color);
                if (resourceDeckImage != null) {
                    resourceDeck.setImage(resourceDeckImage);
                }
            } else if (type == PlayableCardType.GOLD) {
                Image goldDeckImage = GuiResources.getTopDeck(PlayableCardType.GOLD, color);
                if (goldDeckImage != null) {
                    goldDeck.setImage(goldDeckImage);
                }
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
            handCard1.setImage(null);
            handCard2.setImage(null);
            handCard3.setImage(null);
            List<ImageView> handCards = List.of(handCard1, handCard2, handCard3);
            hand = miniGameModel.getCliPlayer(
                    miniGameModel.myName()).getSpace().getPlayerHand();
            for (int cardId : hand) {
                Image cardImage = guiResources.getCardImage(cardId);
                if (cardImage != null) {
                    for (ImageView handCard : handCards) {
                        if (handCard.getImage() == null) {
                            handCard.setImage(cardImage);
                            handCard.getParent().layout();
                            break;
                        }
                    }
                }
            }
        });
    }

    public void updateShownPlayable() {
        Platform.runLater(() -> {
            List<ImageView> shownCards = List.of(vis1, vis2, vis3, vis4);
            for (ImageView shownCard : shownCards) {
                shownCard.setImage(null);
            }
            shownPlayable = miniGameModel.table().getShownCards();
            for (int cardId : shownPlayable) {
                Image cardImage = guiResources.getCardImage(cardId);
                if (cardImage != null) {
                    for (ImageView shownCard : shownCards) {
                        if (shownCard.getImage() == null) {
                            shownCard.setImage(cardImage);
                            shownCard.getParent().layout();
                            break;
                        }
                    }
                }
            }
        });
    }

    public void placeStarterCard() {
        Platform.runLater(() -> {
            int cardId = miniGameModel.getCliPlayer(
                    miniGameModel.myName()).getSpace().getStarterCard();
            boolean isRetro = miniGameModel.getCliPlayer(miniGameModel.myName())
                                           .getField()
                                           .getCardsPositioned()
                                           .get(Position.of(0,0)).isRetro();
            if (! isRetro) {
                Image cardImage = GuiResources.getCardImage(cardId);
                ImageView starterCard = new ImageView(cardImage);
                starterCard.setFitHeight(100);
                starterCard.setFitWidth(150);
                cardField.getChildren().add(starterCard);
            } else {
                ImageView starterRetro = GuiResources.getCardImageRetro(cardId);
                starterRetro.setFitHeight(100);
                starterRetro.setFitWidth(150);
                cardField.getChildren().add(starterRetro);
            }
        });
    }

    public void updateTurnChange(String nickname) {
        Platform.runLater(() -> {
            for (Label label : List.of(player1, player2, player3, player4)) {
                if (label.getText().equals(nickname)) {
                    label.setStyle("-fx-background-color: #D7BC49");
                }
            }
        });
    }

    public void updateCommonObj() {
        Platform.runLater(() -> {
            log.info("Common objective updated");
            Set<Integer> cardIdSet = miniGameModel.table().getCommonObjectives();
            for (int cardId : cardIdSet) {
                Image cardImage = guiResources.getCardImage(cardId);
                if (cardImage != null) {
                    if (commonObj1.getImage() == null) {
                        commonObj1.setImage(cardImage);
                    } else if (commonObj2.getImage() == null) {
                        commonObj2.setImage(cardImage);
                    }
                }
            }

        });
    }

    public void updatePlayerPoints(String nickname, int points) {
        Platform.runLater(() -> {
            List<Label> pointsLabels = List.of(pointsPl1, pointsPl2, pointsPl3, pointsPl4);
            List<Label> playerLabels = List.of(player1, player2, player3, player4);
            int size = playerLabels.size();
            for (int i = 0; i < size; i++) {
                if (playerLabels.get(i).getText().equals(nickname)) {
                    pointsLabels.get(i).setText(String.valueOf(points));
                }
            }
        });
    }

    public void showFieldPl1(MouseEvent mouseEvent) {
    }

    public void showFieldPl2(MouseEvent mouseEvent) {
    }

    public void showFieldPl3(MouseEvent mouseEvent) {
    }

    public void showFieldPl4(MouseEvent mouseEvent) {
    }

    public void pickFromGoldDeck(MouseEvent mouseEvent) {
        guiActuator.drawCard(false, PlayableCardType.GOLD, 0);
    }

    public void pickFromResDeck(MouseEvent mouseEvent) {
        guiActuator.drawCard(false, PlayableCardType.RESOURCE, 0);
    }

    public void chooseVis1(MouseEvent mouseEvent) {
        guiActuator.drawCard(true, PlayableCardType.GOLD,
                             shownPlayable.stream().findFirst().orElse(0));
    }

    public void chooseVis3(MouseEvent mouseEvent) {
        guiActuator.drawCard(true, PlayableCardType.GOLD,
                             shownPlayable.stream().skip(2).findFirst().orElse(0));
    }

    public void chooseVis2(MouseEvent mouseEvent) {
        guiActuator.drawCard(true, PlayableCardType.GOLD,
                             shownPlayable.stream().skip(1).findFirst().orElse(0));
    }

    public void chooseVis4(MouseEvent mouseEvent) {
        guiActuator.drawCard(true, PlayableCardType.GOLD,
                             shownPlayable.stream().skip(3).findFirst().orElse(0));
    }

    public void createButtonsForAvailablePositions() {
        // Rimuovi tutti i bottoni esistenti dal cardField
        cardField.getChildren().removeIf(Button.class::isInstance);
        // Crea un nuovo bottone per ogni posizione disponibile
        for (Position position : availablePositions) {
            Button newButton = new Button();
            int realX = position.x() * 150 + centreX;
            int realY = position.y() * 100 + centreY;
            newButton.setTranslateX(realX);
            newButton.setTranslateY(realY);

            newButton.setOnMouseClicked(event -> {
                selectedPosition = position;
                System.out.println("Selected position: " + selectedPosition);
            });

            cardField.getChildren().add(newButton);
        }
    }

    public void card1Selected(MouseEvent mouseEvent) {
        System.out.println("Card 1 selected");
        int id = miniGameModel.getCliPlayer(
                miniGameModel.myName()).getSpace().getPlayerHand().stream().findFirst().orElse(
                0);
        handCard1.setOnMouseClicked(event -> {
            System.out.println("Card id: " + id);
            boolean isRetro = handCard1.getImage().getUrl().contains(
                    "retro"); // Verifica se è già retro

            Image cardImage;
            if (isRetro) {
                // Mostra il fronte se è già retro
                cardImage = guiResources.getCardImage(id);
            } else {
                // Mostra il retro altrimenti
                try {
                    PlayableCardType type = CardInfo.getPlayableCardType(id);
                    it.polimi.ingsw.am11.model.cards.utils.enums.Color color =
                            CardInfo.getPlayabelCardColor(id);
                    cardImage = guiResources.getRetro(type, color);
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
            }

            handCard1.setImage(cardImage);
            handCard1.getParent().layout();
        });

    }

    public void card2Selected(MouseEvent mouseEvent) {
        System.out.println("Card 2 selected");
        int id = miniGameModel.getCliPlayer(
                miniGameModel.myName()).getSpace().getPlayerHand().stream().skip(
                1).findFirst().orElse(
                0);
        handCard2.setOnMouseClicked(event -> {
            System.out.println("Card id: " + id);
            boolean isRetro = handCard2.getImage().getUrl().contains(
                    "retro"); // Verifica se è già retro

            Image cardImage;
            if (isRetro) {
                // Mostra il fronte se è già retro
                cardImage = guiResources.getCardImage(id);
            } else {
                // Mostra il retro altrimenti
                try {
                    PlayableCardType type = CardInfo.getPlayableCardType(id);
                    it.polimi.ingsw.am11.model.cards.utils.enums.Color color =
                            CardInfo.getPlayabelCardColor(id);
                    cardImage = guiResources.getRetro(type, color);
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
            }

            handCard2.setImage(cardImage);
            handCard2.getParent().layout();
        });

        cardField.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            boolean isRetro = handCard2.getImage().getUrl().contains("retro");
            guiActuator.placeCard((int) x, (int) y, id, isRetro);
        });
    }

    public void card3Selected(MouseEvent mouseEvent) {
        System.out.println("Card 3 selected");
        int id = miniGameModel.getCliPlayer(
                miniGameModel.myName()).getSpace().getPlayerHand().stream().skip(
                2).findFirst().orElse(
                0);
        handCard3.setOnMouseClicked(event -> {
            System.out.println("Card id: " + id);
            boolean isRetro = handCard3.getImage().getUrl().contains(
                    "retro"); // Verifica se è già retro

            Image cardImage;
            if (isRetro) {
                // Mostra il fronte se è già retro
                cardImage = guiResources.getCardImage(id);
            } else {
                // Mostra il retro altrimenti
                try {
                    PlayableCardType type = CardInfo.getPlayableCardType(id);
                    it.polimi.ingsw.am11.model.cards.utils.enums.Color color =
                            CardInfo.getPlayabelCardColor(id);
                    cardImage = guiResources.getRetro(type, color);
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
            }

            handCard3.setImage(cardImage);
            handCard3.getParent().layout();
        });

        cardField.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            boolean isRetro = handCard3.getImage().getUrl().contains("retro");
            guiActuator.placeCard((int) x, (int) y, id, isRetro);
        });
    }

    public void openChatBox(ActionEvent actionEvent) {
    }
}


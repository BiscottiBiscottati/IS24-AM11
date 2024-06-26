package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.client.GUI.CodexNaturalis;
import it.polimi.ingsw.am11.view.client.GUI.GuiActuator;
import it.polimi.ingsw.am11.view.client.GUI.utils.*;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import it.polimi.ingsw.am11.view.client.miniModel.utils.CardInfo;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class GamePage {

    private final Popup popup = new Popup();
    private MiniGameModel miniGameModel;
    private GuiActuator guiActuator;
    private List<Integer> handIDs;
    private List<Boolean> handRetro;
    private List<Integer> shownPlayable;
    private Set<Position> availablePositions;
    private Position selectedPosition;
    private Integer selectedHandPose;
    private String currentSeenField;

    private int centreX;
    private int centreY;
    private double xOffset;
    private double yOffset;
    private VBox commentsBox;
    @FXML
    Label errorLabel;
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
    @FXML
    Label yourName;
    @FXML
    Label lastTurnLabel;


    public GamePage() {
    }

    public void showErrorMessage(String message) {
        Platform.runLater(() -> {
            errorLabel.setText(message);
            errorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> errorLabel.setVisible(false));
            pause.play();
        });
    }

    public void showLastTurnMessage(String msg) {
        Platform.runLater(() -> {
            lastTurnLabel.setText(msg);
            lastTurnLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> lastTurnLabel.setVisible(false));
            pause.play();
        });
    }

    public void createGamePage(@NotNull CodexNaturalis codexNaturalis) throws IOException {
        miniGameModel = codexNaturalis.getMiniGameModel();
        guiActuator = codexNaturalis.getGuiActuator();
        currentSeenField = miniGameModel.myName();

        errorLabel.setVisible(false);
        lastTurnLabel.setVisible(false);

        Font font = FontManager.getFont(FontsEnum.VINQUE, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));

        guiActuator = codexNaturalis.getGuiActuator();

        GPBackground = GuiResources.getTheImageView(GuiResEnum.GAME_BACKGROUND);


        List<Label> labels = List.of(decksLabel, visiblesLabel, handLabel, objLabel,
                                     personalObjLabel, errorLabel, yourName, lastTurnLabel);
        for (Label label : labels) {
            label.setFont(font);
            label.setStyle("-fx-font-size: 30");
            label.setStyle("-fx-text-fill: #D7BC49");
            label.setStyle("-fx-background-color: #351F17");
            label.setStyle("-fx-background-radius: 5");
        }

        yourName.setText("YOUR NAME:\n" + miniGameModel.myName());

        // Get the stream of player names
        Stream<String> playerStream = codexNaturalis.getMiniGameModel().getPlayers().stream();
        List<String> playerNames = playerStream.toList();

        List<Label> playerLabels = List.of(player1, player2, player3, player4);


        int size = playerNames.size();

        for (int i = 0; i < size; i++) {
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
        handIDs = new ArrayList<>(3);

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
            Set<Integer> cardIdSet =
                    miniGameModel.getCliPlayer(
                            miniGameModel.myName()).getSpace().getPlayerObjective();
            int cardId = cardIdSet.iterator().next();
            System.out.println("Card id: " + cardId);
            Image personalObjImage = GuiResources.getCardImage(cardId);

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
            handIDs.clear();
            handRetro = new ArrayList<>(3);

            List<ImageView> handCards = List.of(handCard1, handCard2, handCard3);
            handIDs = new ArrayList<>(miniGameModel.getCliPlayer(miniGameModel.myName())
                                                   .getSpace().getPlayerHand());
            int size = handIDs.size();
            for (int i = 0; i < size; i++) {
                Image cardImage = GuiResources.getCardImage(handIDs.get(i));
                if (cardImage != null) {
                    handCards.get(i).setImage(cardImage);
                    handCards.get(i).getParent().layout();
                    handRetro.add(false);
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
            shownPlayable = new ArrayList<>(miniGameModel.table().getShownCards());
            int size = shownPlayable.size();
            for (int i = 0; i < size; i++) {
                Image cardImage = GuiResources.getCardImage(shownPlayable.get(i));
                if (cardImage != null) {
                    shownCards.get(i).setImage(cardImage);
                    shownCards.get(i).getParent().layout();
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
                                           .get(Position.of(0, 0)).isRetro();
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

    public void createButtonsForAvailablePositions() {
        Platform.runLater(() -> {
            availablePositions = miniGameModel.getCliPlayer(
                    miniGameModel.myName()).getField().getAvailablePositions();
            // Remove all buttons from card field
            cardField.getChildren().removeIf(Rectangle.class::isInstance);
            Rectangle signal = new Rectangle();
            signal.setWidth(150);
            signal.setHeight(100);
            signal.setArcWidth(20);
            signal.setArcHeight(20);
            signal.setFill(Color.BLACK);
            signal.setOpacity(0.8);
            signal.setVisible(false);
            signal.setOnMouseClicked(event -> {
                if (selectedPosition != null) {
                    guiActuator.placeCard(selectedPosition.x(), selectedPosition.y(),
                                          handIDs.get(selectedHandPose),
                                          handRetro.get(selectedHandPose));
                    signal.setVisible(false);
                }
            });
            // New buttons for available positions
            for (Position pos : availablePositions) {
                Rectangle newRectangle = new Rectangle();
                newRectangle.setWidth(150);
                newRectangle.setHeight(100);
                newRectangle.setArcWidth(20);
                newRectangle.setArcHeight(20);
                newRectangle.setFill(Color.BROWN);
                newRectangle.setTranslateX(pos.x() * 117 + centreX);
                newRectangle.setTranslateY(- pos.y() * 60 + centreY);
                newRectangle.setOnMouseClicked(event -> {
                    System.out.println(pos.toString() + " clicked");
                    if (selectedPosition != pos) {
                        selectedPosition = pos;
                        signal.setVisible(true);
                        signal.setTranslateX(pos.x() * 117 + centreX);
                        signal.setTranslateY(- pos.y() * 60 + centreY);
                    }
                });
                cardField.getChildren().add(newRectangle);
                newRectangle.toBack();
            }
            cardField.getChildren().add(signal);
            signal.toFront();
        });
    }

    private void recursivePrinter(Map<Position, ImageView> placedCards,
                                  Map<Position, CardContainer> cardsPositioned,
                                  Position pos) {

        CardContainer cardContainer = cardsPositioned.get(pos);
        if (placedCards.containsKey(pos)) {
            //if we already placed that card we simply put it to the front
            placedCards.get(pos).toFront();
        } else {
            //if the card has not been placed yet we place it and save it in the placedCards Map
            int cardId = cardContainer.getCard().getId();
            boolean isRetro = cardContainer.isRetro();
            ImageView cardImageView;
            if (! isRetro) {
                Image cardImage = GuiResources.getCardImage(cardId);
                cardImageView = new ImageView(cardImage);
            } else {
                cardImageView = GuiResources.getCardImageRetro(cardId);
            }
            cardImageView.setFitHeight(100);
            cardImageView.setFitWidth(150);
            cardImageView.setTranslateX(pos.x() * 117 + centreX);
            cardImageView.setTranslateY(- pos.y() * 60 + centreY);
            placedCards.put(pos, cardImageView);
            cardField.getChildren().add(cardImageView);
            cardImageView.toFront();
        }

        //now let's check the neighbouring cards
        for (Corner corner : Corner.values()) {
            if (cardsPositioned.get(pos).isCornerCovered(corner)) {
                recursivePrinter(placedCards, cardsPositioned, pos.getPositionOn(corner));
            }
        }
    }

    public void printCardsOnField(String nickname) {
        Platform.runLater(() -> {
            if (currentSeenField.equals(nickname)) {
                if (currentSeenField.equals(miniGameModel.myName()) &&
                    miniGameModel.myName().equals(miniGameModel.getCurrentTurn())) {
                    createButtonsForAvailablePositions();
                } else {
                    cardField.getChildren().removeIf(Rectangle.class::isInstance);
                }
                cardField.getChildren().removeIf(ImageView.class::isInstance);
                Map<Position, ImageView> placedCards = new HashMap<>(8);
                Map<Position, CardContainer> cardsPositioned =
                        miniGameModel.getCliPlayer(
                                nickname).getField().getCardsPositioned();

                recursivePrinter(placedCards, cardsPositioned, new Position(0, 0));
            }
        });
    }


    public void updateTurnChange(String nickname) {
        Platform.runLater(() -> {
            for (Label label : List.of(player1, player2, player3, player4)) {
                if (label.getText().equals(nickname)) {
                    label.setStyle("-fx-background-color: #D7BC49");
                } else {
                    label.setStyle("--fx-background-image: transparent");
                }
            }
        });
    }

    public void updateCommonObj() {
        Platform.runLater(() -> {
            Set<Integer> cardIdSet = miniGameModel.table().getCommonObjectives();
            for (int cardId : cardIdSet) {
                Image cardImage = GuiResources.getCardImage(cardId);
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
        Platform.runLater(() -> {
            currentSeenField = player1.getText();
            printCardsOnField(player1.getText());
        });
    }

    public void showFieldPl2(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            currentSeenField = player2.getText();
            printCardsOnField(player2.getText());
        });
    }

    public void showFieldPl3(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            currentSeenField = player3.getText();
            printCardsOnField(player3.getText());
        });
    }

    public void showFieldPl4(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            currentSeenField = player4.getText();
            printCardsOnField(player4.getText());
        });
    }

    public void pickFromGoldDeck(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(false, PlayableCardType.GOLD, 0);
        });
    }

    public void pickFromResDeck(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(false, PlayableCardType.RESOURCE, 0);
        });
    }

    public void chooseVis1(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.getFirst());
        });
    }

    public void chooseVis3(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.get(2));
        });
    }

    public void chooseVis2(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.get(1));
        });
    }

    public void chooseVis4(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.get(3));
        });
    }

    private void handSelect(MouseEvent mouseEvent, int handPos) {
        boolean isRetro = false;
        if (selectedHandPose == null || selectedHandPose != handPos) {
            switch (selectedHandPose) {
                case 0 -> handCard1.setTranslateY(0);
                case 1 -> handCard2.setTranslateY(0);
                case 2 -> handCard3.setTranslateY(0);
                case null, default -> {}
            }
            selectedHandPose = handPos;
            switch (handPos) {
                case 0 -> handCard1.setTranslateY(- 20);
                case 1 -> handCard2.setTranslateY(- 20);
                case 2 -> handCard3.setTranslateY(- 20);

                default -> {}
            }
        }
        switch (handPos) {
            case 0 -> isRetro = handCard1.getImage().getUrl().contains("retro");
            case 1 -> isRetro = handCard2.getImage().getUrl().contains("retro");
            case 2 -> isRetro = handCard3.getImage().getUrl().contains("retro");
            default -> {}
        }
        int id = handIDs.get(selectedHandPose);
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            Image cardImage;
            if (isRetro) {
                cardImage = GuiResources.getCardImage(id);
                handRetro.set(handPos, false);
            } else {
                try {
                    PlayableCardType type = CardInfo.getPlayableCardType(id);
                    it.polimi.ingsw.am11.model.cards.utils.enums.Color color =
                            CardInfo.getPlayabelCardColor(
                                    handIDs.get(handPos));
                    cardImage = GuiResources.getRetro(type, color);
                    handRetro.set(handPos, true);
                } catch (IllegalCardBuildException e) {
                    throw new RuntimeException(e);
                }
            }
            switch (handPos) {
                case 0 -> {
                    handCard1.setImage(cardImage);
                    handCard1.getParent().layout();
                }
                case 1 -> {
                    handCard2.setImage(cardImage);
                    handCard2.getParent().layout();
                }
                case 2 -> {
                    handCard3.setImage(cardImage);
                    handCard3.getParent().layout();
                }
            }
        }

    }

    public void card1Selected(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            handSelect(mouseEvent, 0);
        });

    }

    public void card2Selected(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            handSelect(mouseEvent, 1);
        });
    }

    public void card3Selected(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            handSelect(mouseEvent, 2);
        });
    }

    public void openChatBox(ActionEvent actionEvent) {
        System.out.println("Chat button pressed");
        VBox chatBox = new VBox();

        ScrollPane scrollPane = new ScrollPane();
        commentsBox = new VBox();
        chatBox.setPrefWidth(500);
        chatBox.setPrefHeight(400);
        chatBox.setStyle("-fx-background-color: #8B0000; -fx-padding: 10px;");
        miniGameModel.getChatMessages().forEach(comment -> {
            Label commentLabel = new Label(comment);
            commentsBox.getChildren().add(commentLabel);
            commentsBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        });
        scrollPane.setContent(commentsBox);
        scrollPane.setPrefHeight(300);

        TextField commentField = new TextField();

        Button goBackButton = new Button("Close");
        goBackButton.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        goBackButton.setFont(FontManager.getFont(FontsEnum.VINQUE, 20));
        goBackButton.setTextFill(Color.web("#351F17"));
        goBackButton.setOnMousePressed(
                event -> goBackButton.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        goBackButton.setOnMouseReleased(event -> goBackButton.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        sendButton.setFont(FontManager.getFont(FontsEnum.VINQUE, 20));
        sendButton.setTextFill(Color.web("#351F17"));
        sendButton.setOnMousePressed(
                event -> sendButton.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        sendButton.setOnMouseReleased(event -> sendButton.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

        sendButton.setOnAction(e -> {
            String comment = commentField.getText();
            Stream<String> playerStream = miniGameModel.getPlayers().stream();
            List<String> playerNames = playerStream.toList();
            int i = playerNames.size();
            boolean isPrivate = false;
            for (int j = 0; j < i; j++) {
                if (comment.contains("/" + playerNames.get(j)) &&
                    ! Objects.equals(miniGameModel.myName(), playerNames.get(j))) {
                    comment = comment.replace("/" + playerNames.get(j), "");
                    guiActuator.sendPrivateMessage(playerNames.get(j), comment);
                    isPrivate = true;
                }
            }
            if (! isPrivate) {
                guiActuator.sendChatMessage(comment);
            }
            commentField.clear();
        });

        HBox chatInput = new HBox();
        chatInput.getChildren().addAll(commentField, sendButton, goBackButton);

        chatBox.getChildren().addAll(scrollPane, chatInput);

        popup.getContent().add(chatBox);
        popup.setAutoHide(false); // Impedisce al popup di chiudersi automaticamente


        popup.show(chatButton,
                   chatButton.localToScreen(chatButton.getBoundsInLocal()).getMinX() + 20,
                   chatButton.localToScreen(chatButton.getBoundsInLocal()).getMinY() - 100);

        goBackButton.setOnAction(e -> {
            popup.hide();
        });


        chatBox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        chatBox.setOnMouseDragged(event -> {
            popup.setX(event.getScreenX() - xOffset);
            popup.setY(event.getScreenY() - yOffset);
        });

    }

    public void updateChat() {
        Platform.runLater(() -> {
            if (popup.isShowing()) {
                commentsBox.getChildren().clear();
                miniGameModel.getChatMessages().forEach(comment -> {
                    Label commentLabel = new Label(comment);
                    commentsBox.getChildren().add(commentLabel);
                    commentsBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
                });
            }
        });
    }

    public void gameEnded() {

        Platform.runLater(() -> {
            VBox endGameBox = new VBox();
            endGameBox.setPrefWidth(500);
            endGameBox.setPrefHeight(400);
            endGameBox.setStyle("-fx-background-color: #8B0000; -fx-padding: 10px;");

            Label endGameLabel = new Label("The game has ended!");
            endGameLabel.setFont(FontManager.getFont(FontsEnum.CLOISTER_BLACK, 20));
            endGameLabel.setTextFill(Color.web("#D7BC49"));

            Label finalLeaderboardLabel = new Label("Final Leaderboard:");
            finalLeaderboardLabel.setFont(FontManager.getFont(FontsEnum.CLOISTER_BLACK, 20));
            finalLeaderboardLabel.setTextFill(Color.web("#D7BC49"));

            Map<String, Integer> finalLeaderboard = miniGameModel.getFinalLeaderboard();
            finalLeaderboard.forEach((player, points) -> {
                Label playerLabel = new Label(player + ": " + points + " points");
                playerLabel.setFont(FontManager.getFont(FontsEnum.CLOISTER_BLACK, 20));
                playerLabel.setTextFill(Color.web("#D7BC49"));
                endGameBox.getChildren().add(playerLabel);
            });

            Button closeBtn = new Button("Go back to the main menu");
            closeBtn.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
            closeBtn.setFont(FontManager.getFont(FontsEnum.CLOISTER_BLACK, 20));
            closeBtn.setTextFill(Color.web("#351F17"));
            closeBtn.setOnMousePressed(
                    event -> closeBtn.setStyle(
                            "-fx-background-color: #685C19; -fx-background-radius: 5"));
            closeBtn.setOnMouseReleased(event -> closeBtn.setStyle(
                    "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

            closeBtn.setOnAction(e -> {

            });

            endGameBox.getChildren().addAll(endGameLabel, finalLeaderboardLabel, closeBtn);

            popup.getContent().add(endGameBox);
            popup.setAutoHide(false); // Impedisce al popup di chiudersi automaticamente


            popup.centerOnScreen();
        });

    }
}


package it.polimi.ingsw.am11.view.client.GUI.windows;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.field.PositionManager;
import it.polimi.ingsw.am11.model.players.utils.CardContainer;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class is used to create the page where the game is played.
 */
public class GamePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(GamePage.class);

    private final Popup popup = new Popup();
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
    Label playerColor1;
    @FXML
    Label playerColor2;
    @FXML
    Label playerColor3;
    @FXML
    Label playerColor4;
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
    @FXML
    VBox finalLB;
    @FXML
    Label finalLBLabel;
    @FXML
    Label first;
    @FXML
    Label second;
    @FXML
    Label third;
    @FXML
    Label fourth;
    @FXML
    Button closeBtn;
    @FXML
    Button audioBtn;
    Media soundtrack;
    MediaPlayer soundtrack_p;
    private MiniGameModel miniGameModel;
    private GuiActuator guiActuator;
    private List<Integer> handIDs;
    private List<Boolean> handRetro;
    private List<Integer> shownPlayable;
    private Set<Position> availablePositions;
    private @Nullable Position selectedPosition;
    private Integer selectedHandPose;
    private String currentSeenField;
    private int centreX;
    private int centreY;
    private double xOffset;
    private double yOffset;
    private VBox commentsBox;


    /**
     * This constructor initializes the handIDs list.
     */
    public GamePage() {
        this.handIDs = new ArrayList<>(3);
    }

    /**
     * This method is used to show an error message on the screen.
     *
     * @param message The message to be shown.
     */
    public void showErrorMessage(String message) {
        Platform.runLater(() -> {
            errorLabel.setText(message);
            errorLabel.setVisible(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> errorLabel.setVisible(false));
            pause.play();
        });
    }

    /**
     * This method is used to show a message on the screen.
     *
     * @param msg The message to be shown.
     */
    public void showLastTurnMessage(String msg) {
        Platform.runLater(() -> {
            lastTurnLabel.setText(msg);
            lastTurnLabel.setVisible(true);
        });
    }

    /**
     * This method is used to create the game page in the GUI. It initializes the necessary
     * components, sets their properties, and prepares the game page for interaction. It also sets
     * up the layout for the page and adds it to the root of the GUI.
     *
     * @param codexNaturalis The GUI instance that the game page is a part of.
     */
    public void createGamePage(@NotNull CodexNaturalis codexNaturalis) {
        miniGameModel = codexNaturalis.getMiniGameModel();
        guiActuator = codexNaturalis.getGuiActuator();
        currentSeenField = miniGameModel.myName();
        errorLabel.setVisible(false);
        lastTurnLabel.setVisible(false);
        finalLB.setVisible(false);
        try {
            soundtrack = new Media(Objects.requireNonNull(getClass()
                                                                  .getResource("/soundtrack.mp3"))
                                          .toExternalForm());
            soundtrack_p = new MediaPlayer(soundtrack);
            soundtrack_p.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Exception e) {
            LOGGER.warn("Error while loading sounds files");
        }

        Font font = FontManager.getFont(FontsEnum.VINQUE, (int) (
                Proportions.HALF_BUTTON_SIZE.getValue() * 1.5));

        guiActuator = codexNaturalis.getGuiActuator();

        GPBackground = GuiResources.getTheImageView(GuiResEnum.GAME_BACKGROUND);


        List<Label> labels = List.of(decksLabel, visiblesLabel, handLabel, objLabel,
                                     personalObjLabel, errorLabel, yourName, lastTurnLabel, first
                , second, third, fourth, finalLBLabel);
        for (Label label : labels) {
            label.setFont(font);
            label.setStyle("-fx-font-size: 30");
            label.setStyle("-fx-text-fill: #D7BC49");
            label.setStyle("-fx-background-color: #351F17");
            label.setStyle("-fx-background-radius: 5");
        }

        yourName.setText("YOUR NAME:\n" + miniGameModel.myName());

        // Get the stream of player names
        List<String> playerNames = codexNaturalis.getMiniGameModel()
                                                 .getPlayers()
                                                 .stream()
                                                 .toList()
                                                 .reversed();

        List<Label> playerLabels = List.of(player4, player3, player2, player1);
        List<Label> pointsLabels = List.of(pointsPl4, pointsPl3, pointsPl2, pointsPl1);
        List<Label> colorsLabels = List.of(playerColor4, playerColor3, playerColor2, playerColor1);


        int size = playerNames.size();

        for (int i = 0; i < size; i++) {
            String name = playerNames.get(i);
            playerLabels.get(i).setText(name);
            playerLabels.get(i).setFont(font);
            playerLabels.get(i).setStyle("-fx-font-size: 30");
            playerLabels.get(i).setStyle("-fx-text-fill: #D7BC49");
            playerLabels.get(i).setStyle("-fx-background-color: #351F17");
            playerLabels.get(i).setStyle("-fx-background-radius: 5");

            pointsLabels.get(i).setText("0");
            pointsLabels.get(i).setFont(font);
            pointsLabels.get(i).setStyle("-fx-font-size: 30");
            pointsLabels.get(i).setStyle("-fx-text-fill: #D7BC49");
            pointsLabels.get(i).setStyle("-fx-background-color: #351F17");
            pointsLabels.get(i).setStyle("-fx-background-radius: 5");

            PlayerColor color = codexNaturalis.getMiniGameModel().getCliPlayer(name).getColor();
            colorsLabels.get(i).setText(color.toString());
            colorsLabels.get(i).setFont(font);
            colorsLabels.get(i).setStyle("-fx-font-size: 30");
            colorsLabels.get(i).setStyle("-fx-text-fill: " + color.toString().toLowerCase());
            colorsLabels.get(i).setStyle("-fx-background-color: #351F17");
            colorsLabels.get(i).setStyle("-fx-background-radius: 5");
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

        audioBtn.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
        audioBtn.setFont(font);
        audioBtn.setTextFill(Color.web("#351F17"));
        audioBtn.setOnMousePressed(
                event -> audioBtn.setStyle(
                        "-fx-background-color: #685C19; -fx-background-radius: 5"));
        audioBtn.setOnMouseReleased(event -> audioBtn.setStyle(
                "-fx-background-color: #D7BC49; -fx-background-radius: 5"));
        audioBtn.setText("Unmute");

        audioBtn.setOnMouseClicked(event -> {
            Platform.runLater(() -> {
                if (audioBtn.getText().equals("Mute")) {
                    audioBtn.setText("Unmute");
                    if (soundtrack_p != null) soundtrack_p.pause();
                } else {
                    audioBtn.setText("Mute");
                    if (soundtrack_p != null) soundtrack_p.play();
                }
            });
        });

    }


    /**
     * This method is used to update the top card of the deck in the GUI. It checks the type of the
     * deck (resource or gold) and updates the image of the top card accordingly.
     *
     * @param type  The type of the deck (resource or gold).
     * @param color The color of the card to be displayed on the top of the deck.
     */
    public void updateDeckTop(@NotNull PlayableCardType type,
                              it.polimi.ingsw.am11.model.cards.utils.enums.GameColor color) {
        Platform.runLater(() -> {
            switch (type) {
                case RESOURCE -> {
                    resourceDeck.setVisible(true);
                    Optional.ofNullable(color)
                            .map(gameColor -> GuiResources.getTopDeck(type, gameColor))
                            .ifPresentOrElse(
                                    image -> resourceDeck.setImage(image),
                                    () -> resourceDeck.setVisible(false));
                }
                case GOLD -> {
                    goldDeck.setVisible(true);
                    Optional.ofNullable(color)
                            .map(gameColor -> GuiResources.getTopDeck(type, gameColor))
                            .ifPresentOrElse(
                                    image -> goldDeck.setImage(image),
                                    () -> goldDeck.setVisible(false));
                }
            }
            resourceDeck.getParent().layout();
            goldDeck.getParent().layout();
        });
    }

    /**
     * This method is used to update the personal objective card in the GUI. It retrieves the
     * personal objective card from the game model and updates the corresponding image in the GUI.
     */
    public void updatePersonalObjective() {
        Platform.runLater(() -> {
            Set<Integer> cardIdSet =
                    miniGameModel.getCliPlayer(
                            miniGameModel.myName()).getSpace().getPlayerObjective();
            int cardId = cardIdSet.iterator().next();
            LOGGER.debug("Card id: {}", cardId);
            Image personalObjImage = GuiResources.getCardImage(cardId);

            if (personalObjImage != null) {
                personalObj.setImage(personalObjImage);
                personalObj.getParent().layout();
            }
        });
    }

    /**
     * This method is used to update the hand of cards in the GUI. It clears the current hand of
     * cards and retrieves the new hand from the game model. It then updates the corresponding
     * images in the GUI for each card in the hand.
     */
    public void updateHand() {
        Platform.runLater(() -> {
            handCard1.setImage(null);
            handCard2.setImage(null);
            handCard3.setImage(null);
            handIDs.clear();
            handRetro = new ArrayList<>(3);

            List<ImageView> handCards = List.of(handCard1, handCard2, handCard3);
            handIDs.addAll(miniGameModel.getCliPlayer(miniGameModel.myName())
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

    /**
     * This method is used to update the visible playable cards in the GUI. It clears the current
     * visible playable cards and retrieves the new visible cards from the game model. It then
     * updates the corresponding images in the GUI for each visible card.
     */
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

    /**
     * This method is used to place the starter card on the game field in the GUI. It retrieves the
     * starter card from the game model and updates the corresponding image in the GUI.
     */
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

    /**
     * This method is used to create buttons for available positions on the game field. It retrieves
     * the available positions from the game model and creates a new button for each position. These
     * buttons are then added to the game field in the GUI.
     */
    private void createButtonsForAvailablePositions() {
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
                    selectedPosition = null;
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
                    LOGGER.debug("{} clicked", pos);
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

    /**
     * This method is used to recursively print the cards on the game field in the GUI. It checks if
     * a card has already been placed at a position, if not, it places the card and saves it in the
     * placedCards Map. It then checks the neighbouring positions and calls itself recursively for
     * each neighbouring card.
     *
     * @param placedCards     A map that keeps track of the cards that have already been placed on
     *                        the game field.
     * @param cardsPositioned A map that contains the positions of the cards on the game field.
     * @param pos             The current position being checked.
     */
    private void recursivePrinter(@NotNull Map<Position, ImageView> placedCards,
                                  @NotNull Map<Position, CardContainer> cardsPositioned,
                                  @NotNull Position pos) {

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
                recursivePrinter(placedCards, cardsPositioned,
                                 PositionManager.getPositionIn(pos, corner));
            }
        }
    }

    /**
     * This method is used to print the cards on the game field in the GUI. It retrieves the field
     * of the player with the given nickname and updates the GUI accordingly. If the current player
     * is viewing their own field and it's their turn, it also creates buttons for available
     * positions.
     *
     * @param nickname The nickname of the player whose field is to be printed.
     */
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
            } else {
                if (currentSeenField.equals(miniGameModel.myName())) {
                    assert miniGameModel.getCurrentTurn() != null;
                    if (! miniGameModel.getCurrentTurn().equals(miniGameModel.myName())) {
                        cardField.getChildren().removeIf(Rectangle.class::isInstance);
                    }
                }
            }
        });
    }

    /**
     * This method is used to handle the change of turn in the game. It updates the GUI to reflect
     * the player whose turn it is currently.
     *
     * @param nickname The nickname of the player whose turn it is.
     */
    public void updateTurnChange(String nickname) {
        Platform.runLater(() -> {
            if (miniGameModel.myName().equals(nickname)) {
                try {
                    Media turn_sound =
                            new Media(Objects.requireNonNull(getClass()
                                                                     .getResource(
                                                                             "/turn_change.mp3"))
                                             .toExternalForm());
                    // Crea un MediaPlayer
                    MediaPlayer turn_sound_p = new MediaPlayer(turn_sound);
                    turn_sound_p.play();
                } catch (Exception e) {
                    LOGGER.warn("Error while playing turn sound");
                }
            }
            for (Label label : List.of(player1, player2, player3, player4)) {
                if (label.getText().equals(nickname)) {
                    label.setStyle("-fx-background-color: #D7BC49");
                } else {
                    label.setStyle("--fx-background-image: transparent");
                }
            }
        });
    }

    /**
     * This method is used to update the common objective cards in the GUI. It retrieves the common
     * objective cards from the game model and updates the corresponding images in the GUI.
     */
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

    /**
     * This method is used to update the points of a player in the game. It updates the GUI to
     * reflect the current points of the player.
     *
     * @param nickname The nickname of the player whose points are to be updated.
     * @param points   The new points of the player.
     */
    public void updatePlayerPoints(String nickname, int points) {
        Platform.runLater(() -> {
            List<Label> pointsLabels = List.of(pointsPl4, pointsPl3, pointsPl2, pointsPl1);
            List<Label> playerLabels = List.of(player4, player3, player2, player1);
            int size = playerLabels.size();
            for (int i = 0; i < size; i++) {
                if (playerLabels.get(i).getText().equals(nickname)) {
                    pointsLabels.get(i).setText(String.valueOf(points));
                }
            }
        });
    }

    /**
     * This method is triggered when player 1's field is clicked in the GUI. It updates the current
     * field being viewed to player 1's field and prints the cards on player 1's field.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void showFieldPl1(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            currentSeenField = player1.getText();
            printCardsOnField(player1.getText());
        });
    }

    /**
     * This method is triggered when player 2's field is clicked in the GUI. It updates the current
     * field being viewed to player 2's field and prints the cards on player 2's field.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void showFieldPl2(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            currentSeenField = player2.getText();
            printCardsOnField(player2.getText());
        });
    }

    /**
     * This method is triggered when player 3's field is clicked in the GUI. It updates the current
     * field being viewed to player 3's field and prints the cards on player 3's field.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void showFieldPl3(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            currentSeenField = player3.getText();
            printCardsOnField(player3.getText());
        });
    }

    /**
     * This method is triggered when player 4's field is clicked in the GUI. It updates the current
     * field being viewed to player 4's field and prints the cards on player 4's field.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void showFieldPl4(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            currentSeenField = player4.getText();
            printCardsOnField(player4.getText());
        });
    }

    /**
     * This method is triggered when the gold deck is clicked in the GUI. It sends a request to the
     * game model to draw a card from the gold deck.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void pickFromGoldDeck(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(false, PlayableCardType.GOLD, 0);
        });
    }

    /**
     * This method is triggered when the resource deck is clicked in the GUI. It sends a request to
     * the game model to draw a card from the resource deck.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void pickFromResDeck(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(false, PlayableCardType.RESOURCE, 0);
        });
    }

    /**
     * This method is triggered when the first visible card is clicked in the GUI. It sends a
     * request to the game model to draw a card from the first visible card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void chooseVis1(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.getFirst());
        });
    }

    /**
     * This method is triggered when the third visible card is clicked in the GUI. It sends a
     * request to the game model to draw a card from the third visible card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void chooseVis3(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.get(2));
        });
    }

    /**
     * This method is triggered when the second visible card is clicked in the GUI. It sends a
     * request to the game model to draw a card from the second visible card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void chooseVis2(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.get(1));
        });
    }

    /**
     * This method is triggered when the fourth visible card is clicked in the GUI. It sends a
     * request to the game model to draw a card from the fourth visible card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void chooseVis4(MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            guiActuator.drawCard(true, PlayableCardType.GOLD,
                                 shownPlayable.get(3));
        });
    }

    /**
     * This method is triggered when the first card in the hand is selected in the GUI. It handles
     * the selection and flipping of the card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void card1Selected(@NotNull MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            handSelect(mouseEvent, 0);
        });

    }

    /**
     * This method handles the selection and flipping of a card in the player's hand. It is
     * triggered when a card in the hand is selected in the GUI. If the selected card is different
     * from the previously selected card, it moves the selected card upwards. If the right mouse
     * button is clicked, it flips the card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     * @param handPos    The position of the selected card in the hand (0 for the first card, 1 for
     *                   the second card, etc.).
     */
    private void handSelect(@NotNull MouseEvent mouseEvent, int handPos) {
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
            try {
                Media flip_sound =
                        new Media(Objects.requireNonNull(getClass()
                                                                 .getResource("/flip_card.mp3"))
                                         .toExternalForm());
                MediaPlayer flip_sound_p = new MediaPlayer(flip_sound);
                flip_sound_p.play();
            } catch (Exception e) {
                LOGGER.warn("Error while playing flip sound");
            }
            if (isRetro) {
                cardImage = GuiResources.getCardImage(id);
                handRetro.set(handPos, false);
            } else {
                try {
                    PlayableCardType type = CardInfo.getPlayableCardType(id);
                    GameColor color =
                            CardInfo.getPlayableCardColor(
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

    /**
     * This method is triggered when the second card in the hand is selected in the GUI. It handles
     * the selection and flipping of the card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void card2Selected(@NotNull MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            handSelect(mouseEvent, 1);
        });
    }

    /**
     * This method is triggered when the third card in the hand is selected in the GUI. It handles
     * the selection and flipping of the card.
     *
     * @param mouseEvent The MouseEvent object representing the details of the click event.
     */
    public void card3Selected(@NotNull MouseEvent mouseEvent) {
        Platform.runLater(() -> {
            handSelect(mouseEvent, 2);
        });
    }

    /**
     * This method is triggered when the chat button is clicked in the GUI. It opens a chat box
     * where the user can send and receive messages. The chat box includes a text field for entering
     * messages, a sent button for sending messages, and a close button for closing the chat box.
     * The chat box also displays the chat history.
     *
     * @param actionEvent The ActionEvent object representing the details of the click event.
     */
    public void openChatBox(ActionEvent actionEvent) {
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

        sendButton.setOnAction(e -> handleComment(commentField));

        commentField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleComment(commentField);
            }
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

    /**
     * This method is used to handle the sending of a message in the chat box. It sends the message
     * to the game model, which then broadcasts it to all players in the game.
     *
     * @param commentField The TextField object representing the text field where the message is
     *                     entered.
     */
    public void handleComment(@NotNull TextField commentField) {
        Platform.runLater(() -> {
            String comment = commentField.getText().strip();
            if (comment.isEmpty() || comment.isBlank()) {
                commentField.clear();
                return;
            }
            List<String> playerNames = new ArrayList<>(miniGameModel.getPlayers());
            List<String> toPriv = new ArrayList<>();
            boolean isPrivate = false;
            for (String player : playerNames) {
                if (comment.contains("/" + player) &&
                    ! Objects.equals(miniGameModel.myName(), player)) {
                    comment = comment.replace("/" + player, "").strip();
                    toPriv.add(player);
                    if (comment.isEmpty() || comment.isBlank()) {
                        commentField.clear();
                        return;
                    }
                    isPrivate = true;
                }
            }


            if (! isPrivate) {
                guiActuator.sendChatMessage(comment);
            } else {
                for (String player : toPriv) {
                    guiActuator.sendPrivateMessage(player, comment);

                }
            }
            commentField.clear();
        });
    }

    /**
     * This method is used to update the chat box in the GUI. It retrieves the chat messages from
     * the game model and updates the chat box in the GUI to display the messages. If a new message
     * is received, it also plays a sound notification.
     */
    public void updateChat() {
        Platform.runLater(() -> {
            try {
                Media message_sound =
                        new Media(Objects.requireNonNull(getClass()
                                                                 .getResource("/new_message.mp3"))
                                         .toExternalForm());
                MediaPlayer message_sound_p = new MediaPlayer(message_sound);
                message_sound_p.play();
            } catch (Exception e) {
                LOGGER.warn("Error while playing message sound");
            }
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

    /**
     * This method is triggered when the game ends. It updates the GUI to display the final
     * leaderboard with the players' names and their corresponding scores. It also sets up a close
     * button for the user to exit the game.
     */
    public void gameEnded() {

        Platform.runLater(() -> {
            finalLB.setVisible(true);

            Map<String, Integer> finalLeaderboard = miniGameModel.getFinalLeaderboard();
            assert finalLeaderboard != null;
            finalLeaderboard.forEach((key, value) -> {
                switch (value) {
                    case 1 -> first.setText(key + " - " + value);
                    case 2 -> second.setText(key + " - " + value);
                    case 3 -> third.setText(key + " - " + value);
                    case 4 -> fourth.setText(key + " - " + value);
                }
            });

            closeBtn.setStyle("-fx-background-color: #D7BC49; -fx-background-radius: 5");
            closeBtn.setFont(FontManager.getFont(FontsEnum.CLOISTER_BLACK, 20));
            closeBtn.setTextFill(Color.web("#351F17"));
            closeBtn.setOnMousePressed(
                    event -> closeBtn.setStyle(
                            "-fx-background-color: #685C19; -fx-background-radius: 5"));
            closeBtn.setOnMouseReleased(event -> closeBtn.setStyle(
                    "-fx-background-color: #D7BC49; -fx-background-radius: 5"));

            closeBtn.setOnMouseClicked(event -> CodexNaturalis.restart());
        });

    }

    /**
     * This method is used to stop the music in the game. It checks if the soundtrack player is not
     * null and then stops the music.
     */
    public void stopMusic() {
        if (soundtrack_p != null) {
            soundtrack_p.stop();
        }
    }
}


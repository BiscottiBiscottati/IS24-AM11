package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.GUI.windows.SetNickPage;
import it.polimi.ingsw.am11.view.client.GUI.windows.SetNumOfPlayersPage;
import it.polimi.ingsw.am11.view.client.GUI.windows.WaitingRoomPage;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiExceptionReceiver implements ExceptionThrower {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiExceptionReceiver.class);

    private final MiniGameModel model;
    private final CodexNaturalis codexNaturalis;

    public GuiExceptionReceiver(MiniGameModel model, CodexNaturalis codexNaturalis) {
        this.codexNaturalis = codexNaturalis;
        this.model = model;
    }

    @Override
    public void throwException(@NotNull IllegalPlayerSpaceActionException ex) {
        LOGGER.debug("IllegalPlayerSpaceActionException {}", ex.getMessage());
        guiObserver.showErrorGamePage("You can't place a worker in that space");
    }

    @Override
    public void throwException(@NotNull TurnsOrderException ex) {
        LOGGER.debug("TurnsOrderException {}", ex.getMessage());
        codexNaturalis.showErrorGamePage("It's not your turn, pls stop!");
    }

    @Override
    public void throwException(@NotNull PlayerInitException ex) {
        LOGGER.debug("PlayerInitException {}", ex.getMessage());
        WaitingRoomPage.hideWaitingRoomPage();
        SetNickPage.showSettingNickPage();
        SetNickPage.showErrorMesssage("Nickname already taken, please choose another one");

    }

    @Override
    public void throwException(@NotNull IllegalCardPlacingException ex) {
        LOGGER.debug("IllegalCardPlacingException {}", ex.getMessage());
        codexNaturalis.showErrorGamePage("You don't have the requirements to place the card");

    }

    @Override
    public void throwException(@NotNull IllegalPickActionException ex) {
        LOGGER.debug("IllegalPickActionException {}", ex.getMessage());
        guiObserver.showErrorGamePage("You can't pick a card from the deck");
    }

    @Override
    public void throwException(@NotNull NotInHandException ex) {
        LOGGER.debug("NotInHandException {}", ex.getMessage());
        codexNaturalis.showErrorGamePage("You can't place a card that you don't have in your hand");

    }

    @Override
    public void throwException(@NotNull EmptyDeckException ex) {
        LOGGER.debug("EmptyDeckException {}", ex.getMessage());
        codexNaturalis.showErrorGamePage("Deck is empty, you can't pick a card");

    }

    @Override
    public void throwException(@NotNull NumOfPlayersException ex) {
        LOGGER.debug("NumOfPlayersException {}", ex.getMessage());
        if (model.getGodPlayer().equals(model.myName())) {
            SetNumOfPlayersPage.showErrorMesssage();
        } else {
            codexNaturalis.disconnectedFromServer();
        }

    }

    @Override
    public void throwException(@NotNull NotGodPlayerException ex) {
        LOGGER.debug("NotGodPlayerException {}", ex.getMessage());
        codexNaturalis.disconnectedFromServer();
    }

    @Override
    public void throwException(@NotNull GameStatusException ex) {
        LOGGER.debug("GameStatusException received: {}", ex.getMessage());
        guiObserver.showErrorGamePage("Game is not started yet");
    }

    @Override
    public void throwException(@NotNull NotSetNumOfPlayerException ex) {
        LOGGER.debug("NotSetNumOfPlayerException received: {}", ex.getMessage());
        WaitingRoomPage.hideWaitingRoomPage();
        SetNickPage.showSettingNickPage();
        SetNickPage.showErrorMesssage("The moderator has not set the number of players yet");
    }

    @Override
    public void throwException(@NotNull IllegalPlateauActionException ex) {
        LOGGER.debug("IllegalPlateauActionException {}", ex.getMessage());
        guiObserver.showErrorGamePage("You can't place a worker in that space");
    }

    @Override
    public void throwException(@NotNull MaxHandSizeException ex) {
        LOGGER.debug("MaxHandSizeException {}", ex.getMessage());
        codexNaturalis.showErrorGamePage("It's not your turn, pls stop!");

    }

}

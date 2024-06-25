package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.TUI.TuiExceptionReceiver;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiExceptionReceiver implements ExceptionThrower {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiExceptionReceiver.class);

    private final MiniGameModel model;
    private final GuiObserver guiObserver;

    public GuiExceptionReceiver(MiniGameModel model, GuiObserver guiObserver) {
        this.guiObserver = guiObserver;
        this.model = model;
    }

    //TODO everything
    @Override
    public void throwException(@NotNull IllegalPlayerSpaceActionException ex) {
        LOGGER.debug("IllegalPlayerSpaceActionException {}", ex.getMessage());
    }

    @Override
    public void throwException(@NotNull TurnsOrderException ex) {
        LOGGER.debug("TurnsOrderException {}", ex.getMessage());
    }

    @Override
    public void throwException(@NotNull PlayerInitException ex) {
        LOGGER.debug("PlayerInitException {}", ex.getMessage());

    }

    @Override
    public void throwException(@NotNull IllegalCardPlacingException ex) {
        LOGGER.debug("IllegalCardPlacingException {}", ex.getMessage());

    }

    @Override
    public void throwException(@NotNull IllegalPickActionException ex) {
        LOGGER.debug("IllegalPickActionException {}", ex.getMessage());

    }

    @Override
    public void throwException(@NotNull NotInHandException ex) {
        LOGGER.debug("NotInHandException {}", ex.getMessage());

    }

    @Override
    public void throwException(@NotNull EmptyDeckException ex) {
        LOGGER.debug("EmptyDeckException {}", ex.getMessage());

    }

    @Override
    public void throwException(@NotNull NumOfPlayersException ex) {
        LOGGER.debug("NumOfPlayersException {}", ex.getMessage());

    }

    @Override
    public void throwException(@NotNull NotGodPlayerException ex) {
        LOGGER.debug("NotGodPlayerException {}", ex.getMessage());
    }

    @Override
    public void throwException(@NotNull GameStatusException ex) {
        LOGGER.debug("GameStatusException received: {}", ex.getMessage());

    }

    @Override
    public void throwException(@NotNull NotSetNumOfPlayerException ex) {
        LOGGER.debug("NotSetNumOfPlayerException received: {}", ex.getMessage());
        guiObserver.throwException(ex);
    }

    @Override
    public void throwException(@NotNull IllegalPlateauActionException ex) {
        LOGGER.debug("IllegalPlateauActionException {}", ex.getMessage());
    }

    @Override
    public void throwException(@NotNull MaxHandSizeException ex) {
        LOGGER.debug("MaxHandSizeException {}", ex.getMessage());

    }

}

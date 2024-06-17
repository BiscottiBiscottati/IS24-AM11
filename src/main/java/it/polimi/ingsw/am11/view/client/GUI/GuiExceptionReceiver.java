package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import it.polimi.ingsw.am11.view.client.TUI.TuiExceptionReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuiExceptionReceiver implements ExceptionThrower {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiExceptionReceiver.class);
    final GuiObserver guiObserver;

    public GuiExceptionReceiver(GuiObserver guiObserver) {
        this.guiObserver = guiObserver;
    }

    @Override
    public void throwException(IllegalPlayerSpaceActionException ex) {
        LOGGER.debug("IllegalPlayerSpaceActionException {}", ex.getMessage());
    }

    @Override
    public void throwException(TurnsOrderException ex) {
        LOGGER.debug("TurnsOrderException {}", ex.getMessage());

    }

    @Override
    public void throwException(PlayerInitException ex) {
        LOGGER.debug("PlayerInitException {}", ex.getMessage());

    }

    @Override
    public void throwException(IllegalCardPlacingException ex) {
        LOGGER.debug("IllegalCardPlacingException {}", ex.getMessage());

    }

    @Override
    public void throwException(IllegalPickActionException ex) {
        LOGGER.debug("IllegalPickActionException {}", ex.getMessage());

    }

    @Override
    public void throwException(NotInHandException ex) {
        LOGGER.debug("NotInHandException {}", ex.getMessage());

    }

    @Override
    public void throwException(EmptyDeckException ex) {
        LOGGER.debug("EmptyDeckException {}", ex.getMessage());

    }

    @Override
    public void throwException(NumOfPlayersException ex) {
        LOGGER.debug("NumOfPlayersException {}", ex.getMessage());

    }

    @Override
    public void throwException(NotGodPlayerException ex) {
        LOGGER.debug("NotGodPlayerException {}", ex.getMessage());
    }

    @Override
    public void throwException(GameStatusException ex) {
        LOGGER.debug("GameStatusException received: {}", ex.getMessage());

    }

    @Override
    public void throwException(NotSetNumOfPlayerException ex) {
        LOGGER.debug("NotSetNumOfPlayerException received: {}", ex.getMessage());

    }

    @Override
    public void throwException(IllegalPlateauActionException ex) {
        LOGGER.debug("IllegalPlateauActionException {}", ex.getMessage());
    }

    @Override
    public void throwException(MaxHandSizeException ex) {
        LOGGER.debug("MaxHandSizeException {}", ex.getMessage());

    }

}

package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;

public class GuiExceptionReceiver implements ExceptionConnector {
    final GuiObserver guiObserver;

    public GuiExceptionReceiver(GuiObserver guiObserver) {
        this.guiObserver = guiObserver;
    }

    @Override
    public void throwException(IllegalPlayerSpaceActionException ex) {

    }

    @Override
    public void throwException(TurnsOrderException ex) {

    }

    @Override
    public void throwException(PlayerInitException ex) {

    }

    @Override
    public void throwException(IllegalCardPlacingException ex) {

    }

    @Override
    public void throwException(IllegalPickActionException ex) {

    }

    @Override
    public void throwException(NotInHandException ex) {

    }

    @Override
    public void throwException(EmptyDeckException ex) {

    }

    @Override
    public void throwException(NumOfPlayersException ex) {

    }

    @Override
    public void throwException(NotGodPlayerException ex) {

    }

    @Override
    public void throwException(GameStatusException ex) {

    }

    @Override
    public void throwException(NotSetNumOfPlayerException ex) {

    }

    @Override
    public void throwException(IllegalPlateauActionException description) {

    }

    @Override
    public void throwException(MaxHandSizeException description) {

    }

    @Override
    public void throwException(LostConnectionException description) {

    }
}

package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.model.exceptions.*;

public interface ExceptionConnector {

    void throwException(IllegalPlayerSpaceActionException ex);

    void throwException(TurnsOrderException ex);

    void throwException(PlayerInitException ex);

    void throwException(IllegalCardPlacingException ex);

    void throwException(IllegalPickActionException ex);

    void throwException(NotInHandException ex);

    void throwException(EmptyDeckException ex);

    void throwException(NumOfPlayersException ex);

    void throwException(NotGodPlayerException ex);

    void throwException(GameStatusException ex);

    void throwException(NotSetNumOfPlayerException ex);

}

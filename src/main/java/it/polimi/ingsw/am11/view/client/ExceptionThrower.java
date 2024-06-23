package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.*;

public interface ExceptionThrower {

    /**
     * Handles the IllegalPlayerSpaceActionException, thrown when a player tries to perform an
     * action on a space that is not allowed
     *
     * @param ex the exception thrown
     */
    void throwException(IllegalPlayerSpaceActionException ex);

    /**
     * Handles the TurnsOrderException, thrown when the player tries to perform an action while it's
     * not his turn
     *
     * @param ex the exception thrown
     */
    void throwException(TurnsOrderException ex);

    /**
     * Handles the PlayerInitException, thrown when the player tries to initialize the game with an
     * invalid number of players
     *
     * @param ex the exception thrown
     */
    void throwException(PlayerInitException ex);

    /**
     * Handles the IllegalCardPlacingException, thrown when the player tries to place a card in an
     * illegal position
     *
     * @param ex the exception thrown
     */
    void throwException(IllegalCardPlacingException ex);

    /**
     * Handles the IllegalPickActionException, thrown when the player tries to pick that is not
     * allowed
     *
     * @param ex the exception thrown
     */
    void throwException(IllegalPickActionException ex);

    /**
     * Handles the NotInHandException, thrown when the player tries to use a card that is not in his
     * hand
     *
     * @param ex the exception thrown
     */
    void throwException(NotInHandException ex);

    /**
     * Handles the EmptyDeckException, thrown when the player tries to draw a card from an empty
     * deck
     *
     * @param ex the exception thrown
     */
    void throwException(EmptyDeckException ex);

    /**
     * Handles the NumOfPlayersException, thrown when the player tries to set an invalid number of
     * players
     *
     * @param ex the exception thrown
     */
    void throwException(NumOfPlayersException ex);

    /**
     * Handles the NotGodPlayerException, thrown when the player tries to perform an action that is
     * allowed only to the god player
     *
     * @param ex the exception thrown
     */
    void throwException(NotGodPlayerException ex);

    /**
     * Handles the GameStatusException, thrown when the player tries to perform an action that is
     * not allowed in the current game status
     *
     * @param ex the exception thrown
     */
    void throwException(GameStatusException ex);

    /**
     * Handles the NotSetNumOfPlayerException, thrown when the player tries to connect before the
     * god player has set the number of players
     *
     * @param ex the exception thrown
     */
    void throwException(NotSetNumOfPlayerException ex);

    /**
     * Handles the IllegalPlateauActionException, thrown when the player tries to perform an action
     * that is not allowed on the plateau
     *
     * @param ex the exception thrown
     */
    void throwException(IllegalPlateauActionException ex);

    /**
     * Handles the MaxHandSizeException, thrown when the player tries to draw a card when his hand
     * is full
     *
     * @param ex the exception thrown
     */
    void throwException(MaxHandSizeException ex);
}

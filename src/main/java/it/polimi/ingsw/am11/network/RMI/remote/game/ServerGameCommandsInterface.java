package it.polimi.ingsw.am11.network.RMI.remote.game;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The server uses this interface to receive commands from the clients.
 */
public interface ServerGameCommandsInterface extends Remote {
    /**
     * This method is used to place the starter card.
     *
     * @param nick    The nickname of the player who is setting the starter card.
     * @param isRetro True if the card placed on its retro, false otherwise.
     * @throws RemoteException     If a network error occurs.
     * @throws GameStatusException If the game is not in the right status to place the starter
     *                             card.
     */
    void setStarterCard(@NotNull String nick, boolean isRetro)
    throws RemoteException, GameStatusException;

    /**
     * This method is used to set the personal Objective card.
     *
     * @param nick   The nickname of the player who is setting the personal objective card.
     * @param cardId The id of the card to set.
     * @throws RemoteException                   If a network error occurs.
     * @throws IllegalPlayerSpaceActionException If the player tries to choose a card, that is not
     *                                           in the candidate objective list.
     * @throws GameStatusException               If the game is not in the right status to set the
     *                                           personal objective card.
     */
    void setObjectiveCard(@NotNull String nick, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException,
           GameStatusException;

    /**
     * This method is used to place a card on the player field
     *
     * @param nick    The nickname of the player who is placing the card
     * @param cardId  The id of the card to place
     * @param x       The x coordinate of the card
     * @param y       The y coordinate of the card
     * @param isRetro True if the card placed on its retro, false otherwise
     * @throws RemoteException             If a network error occurs
     * @throws TurnsOrderException         If the player is trying to place a card in the wrong
     *                                     turn
     * @throws IllegalCardPlacingException If the player is trying to place a card in an illegal
     *                                     position
     * @throws NotInHandException          If the player is trying to place a card that is not in
     *                                     his hand
     * @throws GameStatusException         If the game is not in the right status to place the card
     */
    void placeCard(@NotNull String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException, TurnsOrderException, IllegalCardPlacingException,
           NotInHandException, GameStatusException;

    /**
     * This method is used to draw a card from the table
     *
     * @param nick        The nickname of the player who is drawing the card
     * @param fromVisible True if the card is drawn from the visibles, false otherwise
     * @param type        The type of the card to draw
     * @param cardId      The id of the card to draw
     * @throws RemoteException                   If a network error occurs
     * @throws IllegalPlayerSpaceActionException If the player can't draw a card
     * @throws TurnsOrderException               If the player is trying to draw a card in the wrong
     *                                           turn
     * @throws IllegalPickActionException        If the player is trying to draw a card that is not
     *                                           available to pick
     * @throws EmptyDeckException                If the deck is empty
     * @throws MaxHandSizeException              If the player has reached the maximum hand size
     * @throws GameStatusException               If the game is not in the right status to draw the
     *                                           card
     */
    void drawCard(@NotNull String nick, boolean fromVisible, @NotNull PlayableCardType type,
                  int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, TurnsOrderException,
           IllegalPickActionException, EmptyDeckException,
           MaxHandSizeException, GameStatusException;

    /**
     * This method is used to set the number of players in the game at the beginning
     *
     * @param nick         The nickname of the player who is setting the number of players
     * @param numOfPlayers The number of players in the game
     * @throws RemoteException       If a network error occurs
     * @throws NumOfPlayersException If the number of players is not valid
     * @throws NotGodPlayerException If the player is not the god player
     * @throws GameStatusException   If the game is not in the right status to set the number of
     *                               players
     */
    void setNumOfPlayers(@NotNull String nick, int numOfPlayers)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException;

    /**
     * This method notifies that the client needs to resync with the server; the player will expect
     * all the information about the game.
     *
     * @param nick The nickname of the player who needs to resync
     * @throws RemoteException If a network error occurs
     */
    void syncMeUp(@NotNull String nick) throws RemoteException;
}

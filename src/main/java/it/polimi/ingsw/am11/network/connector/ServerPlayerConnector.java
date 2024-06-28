package it.polimi.ingsw.am11.network.connector;

import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import it.polimi.ingsw.am11.network.RMI.server.game.ServerConnectorImpl;
import it.polimi.ingsw.am11.network.socket.server.game.ServerGameSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * This interface is used by the server to send updates to player private data.
 * <p>
 * Each player should have an implementation of this interface to receive updates from the server.
 *
 * @see ServerConnectorImpl
 * @see ServerGameSender
 */
public interface ServerPlayerConnector {

    /**
     * This method is used to notify the player of a change in his hand.
     *
     * @param cardId     the id of the card that has been added or removed
     * @param removeMode true if the card has been removed, false if it has been added
     */
    void updateHand(int cardId, boolean removeMode);

    /**
     * This method is used to notify the player of a change in his personal objectives.
     *
     * @param cardId     the id of the card that has been added or removed
     * @param removeMode true if the card has been removed, false if it has been added
     */
    void updatePersonalObjective(int cardId, boolean removeMode);

    /**
     * This method is used to notify the player with his starter cars.
     *
     * @param cardId the id of the starterCard
     */
    void sendStarterCard(int cardId);

    /**
     * This method is used to notify the player with the candidate objectives that he can choose
     * from.
     *
     * @param cardsId the ids of the candidate objectives
     */
    void sendCandidateObjective(@NotNull Set<Integer> cardsId);

    /**
     * This method is used to notify the player that will have the responsibility to choose the
     * number of players in the game
     */
    void notifyGodPlayer();

    /**
     * This method is used to send all the information about the game needed by the player to
     * reconnect to the game.
     *
     * @param memento the memento containing all the information about the game
     */
    void sendReconnection(@NotNull ReconnectionModelMemento memento);
}

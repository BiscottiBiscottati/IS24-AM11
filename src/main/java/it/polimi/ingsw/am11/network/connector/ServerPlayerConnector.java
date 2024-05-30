package it.polimi.ingsw.am11.network.connector;

import it.polimi.ingsw.am11.network.socket.server.game.ServerGameSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * This interface is used by the server to send updates to player private data.
 * <p>
 * Each player should have an implementation of this interface to receive updates from the server.
 *
 * @see it.polimi.ingsw.am11.network.RMI.server.ServerConnectorImpl
 * @see ServerGameSender
 */
public interface ServerPlayerConnector {
    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);

    void sendStarterCard(int cardId);

    void sendCandidateObjective(@NotNull Set<Integer> cardsId);

    void notifyGodPlayer();
}

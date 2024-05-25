package it.polimi.ingsw.am11.network;

import java.util.Set;

/**
 * This interface is used by the server to send updates to player private data.
 * <p>
 * Each player should have an implementation of this interface to receive updates from the server.
 *
 * @see it.polimi.ingsw.am11.network.RMI.Server.ServerConnectorImpl
 * @see it.polimi.ingsw.am11.network.Socket.Server.ServerMessageSender
 */
public interface ServerPlayerConnector {
    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);

    void sendStarterCard(int cardId);

    void sendCandidateObjective(Set<Integer> cardsId);

    void notifyGodPlayer();
}

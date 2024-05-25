package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;


/**
 * This interface is used by the UI to send commands to the server
 * <p>
 * To get this connector, the UI should call the getConnector method of the
 * <code>ClientNetworkHandler</code>.
 * <p>
 * Each network implementation will provide an implementation of this interface.
 *
 * @see it.polimi.ingsw.am11.network.RMI.Client.NetworkConnector
 * @see it.polimi.ingsw.am11.network.Socket.Client.ClientMessageSender
 */
public interface ClientGameConnector {

    void setStarterCard(boolean isRetro);

    void setPersonalObjective(int cardId);

    void placeCard(Position pos, int cardId, boolean isRetro);

    void drawCard(boolean fromVisible, PlayableCardType type, int cardId);

    void setNumOfPlayers(int numOfPlayers);

    void setNickname(String nickname);
}

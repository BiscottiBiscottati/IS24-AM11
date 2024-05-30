package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.Socket.Client.ClientGameSender;
import org.jetbrains.annotations.NotNull;


/**
 * This interface is used by the UI to send commands to the server
 * <p>
 * To get this connector, the UI should call the getConnector method of the
 * <code>ClientNetworkHandler</code>.
 * <p>
 * Each network implementation will provide an implementation of this interface.
 *
 * @see it.polimi.ingsw.am11.network.RMI.Client.NetworkConnector
 * @see ClientGameSender
 */
public interface ClientGameConnector {

    void setStarterCard(boolean isRetro);

    void setPersonalObjective(int cardId);

    void placeCard(@NotNull Position pos, int cardId, boolean isRetro);

    void drawCard(boolean fromVisible, @NotNull PlayableCardType type, int cardId);

    void setNumOfPlayers(int numOfPlayers);

    void setNickname(@NotNull String nickname);
}

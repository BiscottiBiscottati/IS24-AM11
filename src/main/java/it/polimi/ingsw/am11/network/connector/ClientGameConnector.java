package it.polimi.ingsw.am11.network.connector;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.RMI.client.game.ClientConnectorImpl;
import it.polimi.ingsw.am11.network.socket.client.game.ClientGameSender;
import org.jetbrains.annotations.NotNull;


/**
 * The UI uses this interface to send commands to the server
 * <p>
 * To get this connector, the UI should call the getConnector method of the
 * <code>ClientNetworkHandler</code>.
 * <p>
 * Each network implementation will provide an implementation of this interface.
 *
 * @see ClientConnectorImpl
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

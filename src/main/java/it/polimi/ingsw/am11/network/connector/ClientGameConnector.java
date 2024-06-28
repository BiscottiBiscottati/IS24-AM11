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
 * Each network implementation will provide an implementation of this interface.
 * </p>
 *
 * @see ClientConnectorImpl
 * @see ClientGameSender
 */
public interface ClientGameConnector {

    /**
     * Sends a command to the server to place the starter card ont the field on the specified
     * position
     *
     * @param isRetro true if the card is placed face down, false otherwise
     */
    void setStarterCard(boolean isRetro);

    /**
     * Sends a command to the server to place choose a personal objective card
     *
     * @param cardId the id of the card to choose
     */
    void setPersonalObjective(int cardId);

    /**
     * Sends a command to the server to place a card ont the field on the specified position and
     * orientation
     *
     * @param pos     the position where to place the card
     * @param cardId  the id of the card to place
     * @param isRetro true if the card is placed face down, false otherwise
     */
    void placeCard(@NotNull Position pos, int cardId, boolean isRetro);

    /**
     * @param fromVisible
     * @param type
     * @param cardId
     */
    void drawCard(boolean fromVisible, @NotNull PlayableCardType type, int cardId);

    void setNumOfPlayers(int numOfPlayers);

    void setNickname(@NotNull String nickname);

    void syncMeUp();
}

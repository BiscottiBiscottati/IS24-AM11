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
     * Sends a command to the server to place the starter card onto the field on the specified
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
     * Sends a command to the server to place a card onto the field on the specified position and
     * orientation
     *
     * @param pos     the position where to place the card
     * @param cardId  the id of the card to place
     * @param isRetro true if the card is placed face down, false otherwise
     */
    void placeCard(@NotNull Position pos, int cardId, boolean isRetro);

    /**
     * Sends a command to the server to draw a card from the table
     *
     * @param fromVisible true if the card is drawn from the visible cards, false otherwise
     * @param type        the type of the card to draw
     * @param cardId      the id of the card to drawn, ignored if the card is drawn from the visible
     *                    decks
     */
    void drawCard(boolean fromVisible, @NotNull PlayableCardType type, int cardId);

    /**
     * Used by the godPlayer to specify the number of players in the game
     *
     * @param numOfPlayers the number of players that will play the game
     */
    void setNumOfPlayers(int numOfPlayers);

    /**
     * Used to send to the server the chosen nickname
     *
     * @param nickname the nickname chosen by the player
     */
    void setNickname(@NotNull String nickname);

    /**
     * Used in case the client model has discrepancies with the server model. It is a request to
     * receive the complete game state from the server
     */
    void syncMeUp();
}

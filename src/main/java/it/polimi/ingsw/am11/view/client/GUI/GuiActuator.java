package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.ConnectionType;
import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is used to interact with the GUI, it is used to send messages to the server and to
 * update the GUI
 */
public class GuiActuator {

    private final @NotNull GuiUpdater guiUpdater;
    private @Nullable ClientNetworkHandler connection;
    private @Nullable ClientGameConnector connector;
    private @Nullable ClientChatConnector chatConnector;

    /**
     * Creates a new GuiActuator with the given gui updater and sets the connection to null
     *
     * @param guiUpdater the gui updater to use to update the GUI state and the GUI elements
     */
    public GuiActuator(@NotNull GuiUpdater guiUpdater) {
        this.guiUpdater = guiUpdater;
        this.connector = null;
        this.chatConnector = null;
        this.connection = null;
    }

    /**
     * Connects to the server using the given parameters and sets the TUI state to SETTING_NAME
     *
     * @param type "rmi" or "socket"
     * @param ip   the server ip
     * @param port the server port
     */
    public void connect(@NotNull String type, @NotNull String ip, int port) throws Exception {
        connection = ConnectionType.fromString(type)
                                   .orElseThrow(() -> new RuntimeException(
                                           "Type is set neither to rmi nor " +
                                           "to socket"))
                                   .create(ip, port, guiUpdater);
        connector = connection.getGameConnector();
        chatConnector = connection.getChatConnector();
    }

    /**
     * Sets the nickname of the player
     *
     * @param nick the nickname
     */
    public void setName(@NotNull String nick) {
        assert connector != null;
        connector.setNickname(nick);
        guiUpdater.setCandidateNick(nick);
    }

    /**
     * Sets the number of players, the god player should set it
     *
     * @param numOfPlayers the number of players
     */
    public void setNumOfPlayers(int numOfPlayers) {
        assert connector != null;
        connector.setNumOfPlayers(numOfPlayers);
    }

    /**
     * Used by the players to place the starter card on the front or on the back
     *
     * @param isRetro true if the card is placed on the back
     */
    public void setStarterCard(boolean isRetro) {
        assert connector != null;
        connector.setStarterCard(isRetro);
    }

    /**
     * Used by the player to set the personal objective
     *
     * @param cardId the id of the card
     */
    public void setPersonalObjective(int cardId) {
        assert connector != null;
        connector.setPersonalObjective(cardId);
    }


    /**
     * Used by the player to set the god card
     *
     * @param x       the x position
     * @param y       the y position
     * @param cardId  the id of the card
     * @param isRetro true if the card is placed on the back
     */
    public void placeCard(int x, int y, int cardId, boolean isRetro) {
        assert connector != null;
        connector.placeCard(new Position(x, y), cardId, isRetro);
    }


    /**
     * Used by the player to draw a card from the deck
     *
     * @param fromVisible true if the card is drawn from the visibles
     * @param type        the type of the card
     * @param cardId      the id of the card
     */
    public void drawCard(boolean fromVisible, @NotNull PlayableCardType type, int cardId) {
        if (fromVisible) {
            assert connector != null;
            connector.drawCard(true, type, cardId);
        } else {
            assert connector != null;
            connector.drawCard(false, type, 0);
        }
    }

    /**
     * Send a message to the chat
     *
     * @param message the message
     */
    public void sendChatMessage(@NotNull String message) {
        assert chatConnector != null;
        chatConnector.pubMsg(message);
    }

    /**
     * Send a private message to a player
     *
     * @param recipient the recipient
     * @param message   the message
     */
    public void sendPrivateMessage(@NotNull String recipient, @NotNull String message) {
        assert chatConnector != null;
        chatConnector.pubPrivateMsg(recipient, message);
    }
}

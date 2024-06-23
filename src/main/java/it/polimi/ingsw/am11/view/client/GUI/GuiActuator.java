package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.ConnectionType;
import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.TooManyRequestsException;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

public class GuiActuator {

    private static ClientNetworkHandler connection;
    private final GuiUpdater guiUpdater;
    private ClientGameConnector connector;
    private ClientChatConnector chatConnector;

    public GuiActuator(@NotNull GuiUpdater guiUpdater) {
        this.guiUpdater = guiUpdater;
        this.connector = null;
        this.chatConnector = null;
    }

    /**
     * Connects to the server using the given parameters and sets the TUI state to SETTING_NAME
     *
     * @param type "rmi" or "socket"
     * @param ip   the server ip
     * @param port the server port
     */
    public void connect(@NotNull String type, String ip, int port) throws Exception {
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
     * @throws TooManyRequestsException if the player already sent a nickname
     */
    public void setName(String nick) {
        connector.setNickname(nick);
        guiUpdater.setCandidateNick(nick);
    }

    /**
     * Sets the number of players, the god player should set it
     *
     * @param numOfPlayers the number of players
     */
    public void setNumOfPlayers(int numOfPlayers) {
        connector.setNumOfPlayers(numOfPlayers);
    }

    /**
     * Used by the players to place the starter card on the front or on the back
     *
     * @param isRetro true if the card is placed on the back
     */
    public void setStarterCard(boolean isRetro) {
        connector.setStarterCard(isRetro);
    }

    /**
     * Used by the player to set the personal objective
     *
     * @param cardId the id of the card
     */
    public void setPersonalObjective(int cardId) {
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
        connector.placeCard(new Position(x, y), cardId, isRetro);
    }


    /**
     * Used by the player to draw a card from the deck
     *
     * @param fromVisible true if the card is drawn from the visibles
     * @param type        the type of the card
     * @param cardId      the id of the card
     */
    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId) {
        if (fromVisible) {connector.drawCard(true, type, cardId);} else {
            connector.drawCard(false, type, 0);
        }
    }

    /**
     * Send a message to the chat
     *
     * @param message the message
     */
    public void sendChatMessage(String message) {
        chatConnector.pubMsg(message);
    }

    /**
     * Send a private message to a player
     *
     * @param recipient the recipient
     * @param message   the message
     */
    public void sendPrivateMessage(String recipient, String message) {
        chatConnector.pubPrivateMsg(recipient, message);
    }
}

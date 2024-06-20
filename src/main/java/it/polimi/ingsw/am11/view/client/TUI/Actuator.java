package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.ConnectionType;
import it.polimi.ingsw.am11.network.connector.ClientChatConnector;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.TooManyRequestsException;
import it.polimi.ingsw.am11.view.client.TUI.printers.CardPrinter;
import it.polimi.ingsw.am11.view.client.TUI.states.TUIState;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Its purpose is to effectively actuate the commands parsed by the reader. This is an
// "intermediate class" between the classes that read input and the interface of the network
// (CltToNetConnector). It's needed because the connector is not initialized at the beginning of
// the communication.

public class Actuator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TuiUpdater.class);

    private final TuiUpdater tuiUpdater;
    private ClientGameConnector connector;
    private ClientChatConnector chatConnector;

    public Actuator(@NotNull TuiUpdater tuiUpdater) {
        this.tuiUpdater = tuiUpdater;
        this.connector = null;
        this.chatConnector = null;
    }

    /**
     * Closes the application
     */
    public static void close() {
        System.exit(0);
    }

    /**
     * Shows the help message
     */
    public static void help() {
        //TODO
    }

    /**
     * Connects to the server using the given parameters and sets the TUI state to SETTING_NAME
     *
     * @param type "rmi" or "socket"
     * @param ip   the server ip
     * @param port the server port
     */
    public void connect(@NotNull String type, String ip, int port) {
        try {
            ClientNetworkHandler connection = ConnectionType.fromString(type)
                                                            .orElseThrow(() -> new RuntimeException(
                                                                    "Type is set neither to rmi " +
                                                                    "nor to socket"))
                                                            .create(ip, port, tuiUpdater);
            connector = connection.getGameConnector();
            chatConnector = connection.getChatConnector();
        } catch (Exception e) {
            tuiUpdater.getCurrentTuiState().restart(true, e);
            return;
        }
        tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
        tuiUpdater.setHomeState(TuiStates.SETTING_NAME);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    /**
     * Sets the nickname of the player
     *
     * @param nick the nickname
     * @throws TooManyRequestsException if the player already sent a nickname
     */
    public void setName(String nick)
    throws TooManyRequestsException {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.setHomeState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        if (! tuiUpdater.getCandidateNick().isEmpty()) {
            throw new TooManyRequestsException("You already sent a nickname, wait for results");
        }
        tuiUpdater.setCandidateNick(nick);
        connector.setNickname(nick);
    }

    /**
     * Sets the number of players, the god player should set it
     *
     * @param num the number of players
     */
    public void setNumOfPlayers(int num) {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.setHomeState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        connector.setNumOfPlayers(num);
    }

    /**
     * Used by the players to place the starter card on the front or on the back
     *
     * @param isRetro true if the card is placed on the back
     */
    public void setStarter(boolean isRetro) {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.setHomeState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        connector.setStarterCard(isRetro);
    }

    /**
     * Used by the player to set the personal objective
     *
     * @param cardId the id of the card
     */
    public void setObjective(int cardId) {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.setHomeState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
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
    public void place(int x, int y, int cardId, boolean isRetro) {
        tuiUpdater.setTuiState(TuiStates.WATCHING_TABLE);
        tuiUpdater.setHomeState(TuiStates.WATCHING_TABLE);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        connector.placeCard(new Position(x, y), cardId, isRetro);
    }

    /**
     * Used by the player to draw a card from the deck
     *
     * @param cardId the id of the card
     * @param deck   the deck from which the card is drawn
     * @throws IllegalCardBuildException if the card cannot be built by the CardPrinter
     */
    public void draw(Integer cardId, PlayableCardType deck) throws IllegalCardBuildException {
        tuiUpdater.getCurrentTuiState().restart(false, null);
        if (cardId == null) {
            connector.drawCard(false, deck, 0);
        } else {
            connector.drawCard(true, CardPrinter.getPlayableCard(cardId).getType(), cardId);
        }
    }

    /**
     * Set the current state of the TuiUpdater and restart it
     *
     * @param state
     */
    public void setTuiState(TuiStates state) {
        tuiUpdater.setTuiState(state);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    /**
     * Send a message to the chat
     *
     * @param message
     */
    public void sendChatMessage(String message) {
        chatConnector.pubMsg(message);
    }

    /**
     * Send a private message to a player
     *
     * @param recipient
     * @param message
     */
    public void sendPrivateMessage(String recipient, String message) {
        chatConnector.pubPrivateMsg(recipient, message);
    }

    /**
     * Go back to the home state, the home state is the state that is considered default for a
     * specific phase of the game
     */
    public void goBack() {
        tuiUpdater.goBack();
    }

    /**
     * Get the current TUI state
     *
     * @return the current TUI state
     */
    public TUIState getCurrentTuiState() {
        return tuiUpdater.getCurrentTuiState();
    }


}

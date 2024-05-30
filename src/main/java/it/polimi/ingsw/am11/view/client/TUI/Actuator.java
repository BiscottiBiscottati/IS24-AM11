package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.connector.ClientGameConnector;
import it.polimi.ingsw.am11.network.factory.ConnectionType;
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

    public Actuator(@NotNull TuiUpdater tuiUpdater) {
        this.tuiUpdater = tuiUpdater;
        this.connector = null;
    }

    public static void close() {
        System.exit(0);
    }

    public static void help() {
        //TODO
    }

    public void connect(@NotNull String type, String ip, int port) {
        try {
            connector = ConnectionType.fromString(type)
                                      .orElseThrow(() -> new RuntimeException(
                                              "Type is set neither to rmi nor to socket"))
                                      .create(ip, port, tuiUpdater)
                                      .getGameConnector();
        } catch (Exception e) {
            tuiUpdater.getCurrentTuiState().restart(true, e);
            return;
        }
        tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    public void setName(String nick)
    throws TooManyRequestsException {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        if (! tuiUpdater.getCandidateNick().isEmpty()) {
            throw new TooManyRequestsException("You already sent a nickname, wait for results");
        }
        tuiUpdater.setCandidateNick(nick);
        connector.setNickname(nick);
    }

    public void setNumOfPlayers(int num) {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        connector.setNumOfPlayers(num);
    }

    public void setStarter(boolean isRetro) {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        connector.setStarterCard(isRetro);
    }

    public void setObjective(int cardId) {
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        connector.setPersonalObjective(cardId);
    }

    public void place(int x, int y, int cardId, boolean isRetro) {
        tuiUpdater.setTuiState(TuiStates.WATCHING_TABLE);
        tuiUpdater.getCurrentTuiState().restart(false, null);
        connector.placeCard(new Position(x, y), cardId, isRetro);
    }

    public void draw(Integer cardId, PlayableCardType deck) throws IllegalCardBuildException {
        tuiUpdater.getCurrentTuiState().restart(false, null);
        if (cardId == null) {
            connector.drawCard(false, deck, 0);
        } else {
            connector.drawCard(true, CardPrinter.getCard(cardId).getType(), cardId);
        }
    }

    public void setTuiState(TuiStates state) {
        tuiUpdater.setTuiState(state);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    public TUIState getCurrentTuiState() {
        return tuiUpdater.getCurrentTuiState();
    }

}

package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.network.RMI.Client.ClientMain;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.TooManyRequestsException;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

// Its purpose is to effectively actuate the commands parsed by the reader. This is an
// "intermediate class" between the classes that read input and the interface of the network
// (CltToNetConnector). It's needed because the connector is not initialized at the beginning of
// the communication.

public class Actuator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Actuator.class);

    private final TuiUpdater tuiUpdater;
    private CltToNetConnector connector;

    public Actuator(TuiUpdater tuiUpdater) {
        this.tuiUpdater = tuiUpdater;
        this.connector = null;
    }

    public static void close() {
        System.exit(0);
    }

    public static void help() {
        //TODO
    }

    public void connect(String type, String ip, int port) {
        //DONE
        switch (type) {
            case "rmi": {
                ClientNetworkHandler clientHandler = null;
                try {
                    clientHandler = new ClientMain(ip, port, tuiUpdater);
                } catch (RemoteException e) {
                    tuiUpdater.getCurrentTuiState().restart(true, e);
                    return;
                }
                connector = clientHandler.getConnector();
                tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
                tuiUpdater.getCurrentTuiState().restart(false, null);
                break;
            }
            case "socket": {
                ClientNetworkHandler clientHandler = null;
                try {
                    clientHandler = new ClientSocket(ip, port, tuiUpdater);
                } catch (IOException e) {
                    tuiUpdater.getCurrentTuiState().restart(true, e);
                    return;
                }
                connector = clientHandler.getConnector();
                tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
                tuiUpdater.getCurrentTuiState().restart(false, null);
                break;
            }
            default: {
                throw new RuntimeException("Type is set neither to rmi nor to socket");
            }
        }

    }

    public void setName(String nick)
    throws TooManyRequestsException {
        //DONE
        if (! tuiUpdater.getCandidateNick().isEmpty()) {
            throw new TooManyRequestsException("You already sent a nickname, wait for results");
        }
        connector.setNickname(nick);
        tuiUpdater.setCandidateNick(nick);
        //TOREMOVE
        if (nick.equals("god")) {
            tuiUpdater.setTuiState(TuiStates.SETTING_NUM);
            tuiUpdater.getCurrentTuiState().restart(false, null);
            return;
        }
        //----f
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    public void setNumOfPlayers(int num) {
        //DONE
        connector.setNumOfPlayers(num);
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    public void setStarter(boolean isRetro) {
        connector.setStarterCard(isRetro);
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    public void setObjective(int cardId) {
        connector.setPersonalObjective(cardId);
        tuiUpdater.setTuiState(TuiStates.WAITING);
        tuiUpdater.getCurrentTuiState().restart(false, null);
    }

    public void place(List<String> positionalArgs) throws InvalidArgumetsException {
        if (positionalArgs.size() != 5) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        int x;
        int y;
        int cardid;
        String frontOrRetro = positionalArgs.get(4);
        try {
            x = Integer.parseInt(positionalArgs.get(1));
            y = Integer.parseInt(positionalArgs.get(2));
            cardid = Integer.parseInt(positionalArgs.get(3));
        } catch (NumberFormatException e) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        switch (frontOrRetro) {
            case "front" -> connector.placeCard(new Position(x, y), cardid, false);
            case "retro" -> connector.placeCard(new Position(x, y), cardid, true);
            default -> throw new InvalidArgumetsException("Invalid Arguments");
        }
    }

    public void draw(List<String> positionalArgs) throws InvalidArgumetsException {
        if (positionalArgs.size() != 4 && positionalArgs.size() != 3) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        String visOrDeck = positionalArgs.get(1);
        String resOrGold = positionalArgs.get(2);
        int cardId = - 1;
        if (positionalArgs.size() == 4) {
            try {
                cardId = Integer.parseInt(positionalArgs.get(3));
            } catch (NumberFormatException e) {
                throw new InvalidArgumetsException("Invalid Arguments");
            }
        }

        switch (visOrDeck) {
            case "visible" -> {
                if (positionalArgs.size() != 4) {
                    throw new InvalidArgumetsException("Invalid Arguments");
                }
                switch (resOrGold) {
                    case "res" -> {
                        connector.drawCard(true, PlayableCardType.RESOURCE, cardId);
                    }
                    case "gold" -> {
                        connector.drawCard(true, PlayableCardType.GOLD, cardId);
                    }
                    default -> throw new InvalidArgumetsException("Invalid Arguments");
                }
            }
            case "deck" -> {
                switch (resOrGold) {
                    case "res" -> {
                        connector.drawCard(false, PlayableCardType.RESOURCE, cardId);
                    }
                    case "gold" -> {
                        connector.drawCard(false, PlayableCardType.GOLD, cardId);
                    }
                    default -> throw new InvalidArgumetsException("Invalid Arguments");
                }
            }
            default -> throw new InvalidArgumetsException("Invalid Arguments");
        }
    }

}

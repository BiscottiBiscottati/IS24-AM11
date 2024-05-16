package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.InvalidArgumetsException;
import it.polimi.ingsw.am11.view.client.TUI.exceptions.TooManyRequestsException;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;

import java.io.IOException;
import java.util.List;

//Its purpose is to effectively actuate the commands parsed by the reader
public class Actuator {
    private final TuiUpdater tuiUpdater;
    private CltToNetConnector connector;

    public Actuator(TuiUpdater tuiUpdater) {
        this.tuiUpdater = tuiUpdater;
    }

    public static void close() {
        System.exit(0);
    }

    public void connect(List<String> positionalArgs)
    throws InvalidArgumetsException, IOException {
        if (positionalArgs.size() != 4) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        String type = positionalArgs.get(1);
        String ip = positionalArgs.get(2);
        int port;
        try {
            port = Integer.parseInt(positionalArgs.get(3));
        } catch (NumberFormatException e) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        switch (type) {
            case "rmi": {
                //TODO
                break;
            }
            case "socket": {
                ClientSocket clientSocket = new ClientSocket(ip, port, tuiUpdater);
                connector = clientSocket.getConnector();
                tuiUpdater.setTuiState(TuiStates.SETTING_NAME);
                break;
            }
            default: {
                throw new InvalidArgumetsException("Invalid Arguments");
            }
        }
    }


    public void setName(List<String> positionalArgs)
    throws InvalidArgumetsException, TooManyRequestsException {
        if (tuiUpdater.getCandidateNick() != null) {
            throw new TooManyRequestsException("You already sent a nickname, wait for results");
        }
        if (positionalArgs.size() != 2) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        String nick = positionalArgs.get(1);
        connector.setNickname(nick);
        tuiUpdater.setCandidateNick(nick);
        tuiUpdater.setTuiState(TuiStates.WAITING);
    }

    public void setNumOfPlayers(List<String> positionalArgs) throws InvalidArgumetsException {
        if (positionalArgs.size() != 2) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        int num;
        try {
            num = Integer.parseInt(positionalArgs.get(1));
        } catch (NumberFormatException e) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        connector.setNumOfPlayers(num);
        tuiUpdater.setTuiState(TuiStates.WAITING);
    }

    public void setStarter(List<String> positionalArgs) throws InvalidArgumetsException {
        if (positionalArgs.size() != 2) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        String frontOrRetro = positionalArgs.get(1);
        switch (frontOrRetro) {
            case "front" -> connector.setStarterCard(false);
            case "retro" -> connector.setStarterCard(true);
            default -> throw new InvalidArgumetsException("Invalid Arguments");
        }
    }

    public void setObjective(List<String> positionalArgs) throws InvalidArgumetsException {
        if (positionalArgs.size() != 2) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        int cardId;
        try {
            cardId = Integer.parseInt(positionalArgs.get(1));
        } catch (NumberFormatException e) {
            throw new InvalidArgumetsException("Invalid Arguments");
        }
        connector.setPersonalObjective(cardId);
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

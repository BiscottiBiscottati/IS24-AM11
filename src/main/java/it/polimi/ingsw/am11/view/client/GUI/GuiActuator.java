package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.RMI.Client.ClientRMI;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.view.client.TUI.states.TuiStates;

import java.io.IOException;
import java.rmi.RemoteException;

public class GuiActuator {

    private ClientGameConnector connector = null;
    private GuiUpdater guiUpdater;

    public GuiActuator(GuiUpdater guiUpdater) {
        this.guiUpdater = guiUpdater;
    }

    public void connect(String type, String ip, int port) {
        ClientNetworkHandler clientHandler;
        switch (type) {
            case "rmi": {
                try {
                    clientHandler = new ClientRMI(ip, port, guiUpdater);
                } catch (RemoteException e) {
                    //TODO: handle exception
                    return;
                }
                connector = clientHandler.getConnector();
                break;
            }
            case "socket": {
                try {
                    clientHandler = new ClientSocket(ip, port, guiUpdater);
                } catch (IOException e) {
                    //TODO: handle exception
                    return;
                }
                connector = clientHandler.getConnector();
                break;
            }
            default: {
                throw new RuntimeException("Type is set neither to rmi nor to socket");
            }
        }
    }

    public void setName(String nick) {
        connector.setNickname(nick);
    }

    public void setNumOfPlayers(int numOfPlayers) {
        connector.setNumOfPlayers(numOfPlayers);
    }

    public void setStarterCard(boolean isRetro) {
        connector.setStarterCard(isRetro);
    }

    public void setPersonalObjective(int cardId) {
        connector.setPersonalObjective(cardId);
    }

    public void placeCard(int x, int y, int cardId, boolean isRetro) {
        connector.placeCard(new Position(x, y), cardId, isRetro);
    }

    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId) {
        connector.drawCard(fromVisible, type, cardId);
    }
}

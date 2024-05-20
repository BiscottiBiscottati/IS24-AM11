package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientMain implements ClientNetworkHandler {


    private final Registry registry;
    private final ClientViewUpdater updater;
    private final NetworkConnector nConnector;

    public ClientMain(String ip, int port, ClientViewUpdater updater) throws RemoteException {
        // Getting the registry
        registry = LocateRegistry.getRegistry(ip, port);
        // Check if connection is working
        registry.list();
        // Looking up the registry for the remote object
        System.out.println("Remote method invoked");
        this.updater = updater;
        this.nConnector = new NetworkConnector(this);
    }

    public static void main(String[] args) throws RemoteException {
        System.out.println("Hello from Client!");
    }

    public void login(String nick)
    throws RemoteException, NotBoundException {
        Loggable stub1 = (Loggable) registry.lookup("Loggable");
        ClientToServerConnector clientObject = new ClientToServerConnector(updater);
        ConnectorInterface connector = (ConnectorInterface) UnicastRemoteObject.exportObject(
                clientObject, 0);
        stub1.login(nick, connector);
    }

    public void setNumOfPlayers(String nick, int numOfPlayers) throws RemoteException {
        Loggable stub1;
        try {
            stub1 = (Loggable) registry.lookup("Loggable");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub1.setNumOfPlayers(nick, numOfPlayers);
    }

    public void logout(String nick) throws RemoteException {
        Loggable stub1;
        try {
            stub1 = (Loggable) registry.lookup("Loggable");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub1.logout(nick);
    }

    public void setStarterCard(String nick, boolean isRetro) throws RemoteException {
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub3.setStarterCard(nick, isRetro);
    }

    public void setObjectiveCard(String nick, int cardId) throws RemoteException {
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub3.setObjectiveCard(nick, cardId);
    }

    public void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException {
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub3.placeCard(nick, cardId, x, y, isRetro);
    }

    public void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws RemoteException {
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub3.drawCard(nick, fromVisible, type, cardId);
    }

    public NetworkConnector getConnector() {
        return nConnector;
    }

    public void reconnect(String nick) throws RemoteException {
        Loggable stub1;
        try {
            stub1 = (Loggable) registry.lookup("Loggable");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        stub1.reconnect(nick);
    }
}

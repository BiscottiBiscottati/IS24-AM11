package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.ClientNetworkHandler;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientMain implements ClientNetworkHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

    private final Registry registry;
    private final ConnectorInterface connector;
    private final NetworkConnector nConnector;
    private final ExceptionConnector exceptionConnector;

    public ClientMain(String ip, int port, ClientViewUpdater updater) throws RemoteException {

        // Getting the registry
        registry = LocateRegistry.getRegistry(ip, port);
        LOGGER.debug("CLIENT RMI: Registry located {}", registry.toString());
        // Check if the connection is working
        registry.list();
        // Looking up the registry for the remote object
        this.nConnector = new NetworkConnector(this);
        ClientToServerConnector clientObject = new ClientToServerConnector(updater);
        connector = (ConnectorInterface) UnicastRemoteObject.exportObject(clientObject, 0);

        exceptionConnector = updater.getExceptionConnector();
    }

    public void login(String nick)
    throws RemoteException {
        LOGGER.debug("CLIENT RMI: Sending login request to server");
        Loggable stub1;
        try {
            stub1 = (Loggable) registry.lookup("Loggable");
            stub1.login(nick, connector);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (NumOfPlayersException e) {
            exceptionConnector.throwException(e);
        } catch (NotSetNumOfPlayerException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        } catch (PlayerInitException e) {
            exceptionConnector.throwException(e);
        }
    }

    public void setNumOfPlayers(String nick, int numOfPlayers) throws RemoteException {
        Loggable stub1;
        try {
            stub1 = (Loggable) registry.lookup("Loggable");
            stub1.setNumOfPlayers(nick, numOfPlayers);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (NumOfPlayersException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        } catch (NotGodPlayerException e) {
            exceptionConnector.throwException(e);
        }
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
            stub3.setStarterCard(nick, isRetro);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (PlayerInitException e) {
            exceptionConnector.throwException(e);
        } catch (IllegalCardPlacingException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        }
    }

    public void setObjectiveCard(String nick, int cardId) throws RemoteException {
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
            stub3.setObjectiveCard(nick, cardId);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalPlayerSpaceActionException e) {
            exceptionConnector.throwException(e);
        } catch (PlayerInitException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        }
    }

    public void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException {
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
            stub3.placeCard(nick, cardId, x, y, isRetro);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (TurnsOrderException e) {
            exceptionConnector.throwException(e);
        } catch (PlayerInitException e) {
            exceptionConnector.throwException(e);
        } catch (IllegalCardPlacingException e) {
            exceptionConnector.throwException(e);
        } catch (NotInHandException e) {
            exceptionConnector.throwException(e);
        } catch (IllegalPlateauActionException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        }
    }

    public void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws RemoteException {
        PlayerViewInterface stub3;
        try {
            stub3 = (PlayerViewInterface) registry.lookup("PlayerView");
            stub3.drawCard(nick, fromVisible, type, cardId);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalPlayerSpaceActionException e) {
            exceptionConnector.throwException(e);
        } catch (TurnsOrderException e) {
            exceptionConnector.throwException(e);
        } catch (IllegalPickActionException e) {
            exceptionConnector.throwException(e);
        } catch (PlayerInitException e) {
            exceptionConnector.throwException(e);
        } catch (EmptyDeckException e) {
            exceptionConnector.throwException(e);
        } catch (MaxHandSizeException e) {
            exceptionConnector.throwException(e);
        } catch (GameStatusException e) {
            exceptionConnector.throwException(e);
        }
    }

    public NetworkConnector getConnector() {
        return nConnector;
    }

    @Override
    public void close() {
        try {
            UnicastRemoteObject.unexportObject(connector, true);
        } catch (NoSuchObjectException e) {
            LOGGER.error("CLIENT RMI: No Connector to un-export");
        }
        LOGGER.debug("CLIENT RMI: Client closed");
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

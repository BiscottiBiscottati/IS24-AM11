package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerMain implements Loggable, PlayerViewInterface, CentralControllerInterface {

    static int PORT = 1234;
    static
    VirtualPlayerView view;
    private static ServerMain obj = null;
    private final CentralControllerInterface centralController = null;
    List<String> registeredClients;
    ConnectorImplementation connector;

    public ServerMain() {
        super();
        this.connector = new ConnectorImplementation();
    }

    public synchronized static ServerMain getInstance() {
        if (obj == null) {
            obj = new ServerMain();
        }
        return obj;
    }

    public static void main(String[] args) {
        System.out.println("Hello from Server!");
        Loggable log = null;
        PlayerViewInterface view = null;
        obj = new ServerMain();
        try {
            log = (Loggable) UnicastRemoteObject.exportObject(obj, 0);
            view = (PlayerViewInterface) UnicastRemoteObject.exportObject(obj, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // Bind the remote object's stub in the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            registry.bind("Loggable", log);
            registry.bind("PlayerView", view);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("Server ready");
    }

    @Override
    public void login(String nick) throws RemoteException {
    }

    @Override
    public void logout(String nick) throws RemoteException {
        CentralController.INSTANCE.playerDisconnected(nick);
        registeredClients.remove(nick);
        System.out.println(nick + " disconnected");
    }


    @Override
    public void setStarterCard(String nick, boolean isRetro)
    throws ServerException {
        try {
            view.setStarterCard(isRetro);
        } catch (PlayerInitException e) {
            throw new ServerException("Invalid player init.");
        } catch (GameStatusException e) {
            throw new ServerException("Game status exception.");
        } catch (IllegalCardPlacingException e) {
            throw new ServerException("Illegal card placing.");
        }
    }

    @Override
    public void setObjectiveCard(String nick, int cardId)
    throws ServerException {
        try {
            view.setObjectiveCard(cardId);
        } catch (IllegalPlayerSpaceActionException e) {
            throw new ServerException("Illegal player space action.");
        } catch (PlayerInitException e) {
            throw new ServerException("Invalid player init.");
        } catch (GameStatusException e) {
            throw new ServerException("Game status exception.");
        }

    }

    @Override
    public void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws ServerException {
        try {
            view.placeCard(cardId, x, y, isRetro);
        } catch (TurnsOrderException e) {
            throw new ServerException("Turns order exception.");
        } catch (PlayerInitException e) {
            throw new ServerException("Invalid player init.");
        } catch (IllegalCardPlacingException e) {
            throw new ServerException("Illegal card placing.");
        } catch (NotInHandException e) {
            throw new ServerException("Not in hand.");
        } catch (IllegalPlateauActionException e) {
            throw new ServerException("Illegal plateau action.");
        } catch (GameStatusException e) {
            throw new ServerException("Game status exception.");
        }

    }

    @Override
    public void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws ServerException {
        try {
            view.drawCard(fromVisible, type, cardId);
        } catch (IllegalPlayerSpaceActionException e) {
            throw new ServerException("Illegal player space action.");
        } catch (TurnsOrderException e) {
            throw new ServerException("Turns order exception.");
        } catch (IllegalPickActionException e) {
            throw new ServerException("Illegal pick action.");
        } catch (PlayerInitException e) {
            throw new ServerException("Invalid player init.");
        } catch (EmptyDeckException e) {
            throw new ServerException("Empty deck.");
        } catch (MaxHandSizeException e) {
            throw new ServerException("Max hand size.");
        } catch (GameStatusException e) {
            throw new ServerException("Game status exception.");
        }
    }

    @Override
    public void setNumofPlayers(String nick, int numOfPlayers) throws ServerException {
        try {
            CentralController.INSTANCE.setNumOfPlayers(nick, numOfPlayers);
        } catch (NotGodPlayerException e) {
            throw new ServerException("Not god player.");
        } catch (GameStatusException e) {
            throw new ServerException("Game status exception.");
        } catch (NumOfPlayersException e) {
            throw new ServerException("Max players reached.");
        }
    }

    public CentralControllerInterface playerDisconnected() throws RemoteException {
        return null;
    }

    public CentralControllerInterface playerReconnected(String nickname) throws RemoteException {
        return null;
    }

    public CentralControllerInterface connectPlayer(String nickname) throws RemoteException {
        return null;
    }

}

package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain implements Loggable, PlayerViewInterface {

    static int PORT = 1234;

    VirtualPlayerView view = new VirtualPlayerView(null, null);

    public static void main(String[] args) {
        System.out.println("Hello from Server!");
        Loggable stub = null;
        ServerMain obj = new ServerMain();
        try {
            stub = (Loggable) UnicastRemoteObject.exportObject(obj, PORT);
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
            registry.bind("Loggable", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.err.println("Server ready");
    }

    @Override
    public void login(String nick)
    throws RemoteException, PlayerInitException, GameStatusException, NumOfPlayersException {
        try {
            MessageManager messageManager = new MessageManager();
            view = CentralController.INSTANCE
                    .connectPlayer(nick, messageManager, messageManager);
            System.out.println(nick + " connected");
        } catch (PlayerInitException e) {
            throw new PlayerInitException("Invalid nickname. Please try again.");
        } catch (GameStatusException e) {
            throw new GameStatusException("Game status exception.");
        } catch (NumOfPlayersException e) {
            throw new NumOfPlayersException("Max players reached.");
        }
    }

    @Override
    public void logout(String nick) throws RemoteException {

    }


    @Override
    public void setStarterCard(boolean isRetro)
    throws PlayerInitException, IllegalCardPlacingException, GameStatusException {
        try {
            view.setStarterCard(isRetro);
        } catch (PlayerInitException e) {
            throw new PlayerInitException("Invalid player init.");
        } catch (IllegalCardPlacingException e) {
            throw new IllegalCardPlacingException("Illegal card placing.");
        } catch (GameStatusException e) {
            throw new GameStatusException("Game status exception.");
        }
    }

    @Override
    public void setObjectiveCard(int cardId)
    throws IllegalPlayerSpaceActionException, PlayerInitException, GameStatusException {
        try {
            view.setObjectiveCard(cardId);
        } catch (IllegalPlayerSpaceActionException e) {
            throw new IllegalPlayerSpaceActionException("Illegal player space action.");
        } catch (PlayerInitException e) {
            throw new PlayerInitException("Invalid player init.");
        } catch (GameStatusException e) {
            throw new GameStatusException("Game status exception.");
        }

    }

    @Override
    public void placeCard(int cardId, int x, int y, boolean isRetro)
    throws TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException {
        try {
            view.placeCard(cardId, x, y, isRetro);
        } catch (TurnsOrderException e) {
            throw new TurnsOrderException("Turns order exception.");
        } catch (PlayerInitException e) {
            throw new PlayerInitException("Invalid player init.");
        } catch (IllegalCardPlacingException e) {
            throw new IllegalCardPlacingException("Illegal card placing.");
        } catch (NotInHandException e) {
            throw new NotInHandException("Card not in hand.");
        } catch (IllegalPlateauActionException e) {
            throw new IllegalPlateauActionException("Illegal plateau action.");
        } catch (GameStatusException e) {
            throw new GameStatusException("Game status exception.");
        }

    }

    @Override
    public void drawCard(boolean fromVisible, PlayableCardType type, int cardId)
    throws IllegalPlayerSpaceActionException, TurnsOrderException, IllegalPickActionException,
           PlayerInitException, EmptyDeckException, MaxHandSizeException, GameStatusException {
        try {
            view.drawCard(fromVisible, type, cardId);
        } catch (IllegalPlayerSpaceActionException e) {
            throw new IllegalPlayerSpaceActionException("Illegal player space action.");
        } catch (TurnsOrderException e) {
            throw new TurnsOrderException("Turns order exception.");
        } catch (IllegalPickActionException e) {
            throw new IllegalPickActionException("Illegal pick action.");
        } catch (PlayerInitException e) {
            throw new PlayerInitException("Invalid player init.");
        } catch (EmptyDeckException e) {
            throw new EmptyDeckException("Empty deck.");
        } catch (MaxHandSizeException e) {
            throw new MaxHandSizeException("Max hand size reached.");
        } catch (GameStatusException e) {
            throw new GameStatusException("Game status exception.");
        }

    }

    @Override
    public void setNumofPlayers(String nick, int numOfPlayers) throws RemoteException {
        try {
            CentralController.INSTANCE.setNumOfPlayers(nick, numOfPlayers);
        } catch (NotGodPlayerException e) {
            throw new RemoteException("Not god player.");
        } catch (GameStatusException e) {
            throw new RemoteException("Game status exception.");
        } catch (NumOfPlayersException e) {
            throw new RemoteException("Max players reached.");
        }
    }
}

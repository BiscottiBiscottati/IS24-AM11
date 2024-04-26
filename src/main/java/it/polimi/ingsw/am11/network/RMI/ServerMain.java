package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CardController;
import it.polimi.ingsw.am11.controller.GameController;
import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.PlayerViewInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain implements Loggable, PlayerViewInterface {

    static int PORT = 1234;
    private final GameController gameController = new GameController(new GameLogic());
    private final CardController cardController = new CardController(new GameLogic());

    public static void main(String[] args) {
        System.out.println("Hello from Server!");

        ServerMain obj = new ServerMain();
        try {
            Loggable stub = (Loggable) UnicastRemoteObject.exportObject(
                    obj, PORT);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("Loggable", stub);
            System.err.println("Server ready");
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean login(String nick) throws RemoteException {
        System.out.println(nick + " is logging...");
        return false;
    }

    @Override
    public void logout(String nick) throws RemoteException {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean addPlayer(String nickname) throws RemoteException {
        try {
            gameController.addPlayer(nickname);
            return true;
        } catch (Exception e) {
            throw new RemoteException("Failed to add player", e);
        }
    }

    @Override
    public boolean initGame() throws RemoteException {
        try {
            gameController.initGame();
            return true;
        } catch (Exception e) {
            throw new RemoteException("Failed to initialize game", e);
        }
    }

    @Override
    public boolean setObjFor(String nickname, int cardID) throws RemoteException {
        try {
            cardController.setObjectiveFor(nickname, cardID);
            return true;
        } catch (Exception e) {
            throw new RemoteException("Failed to set objective for player", e);
        }
    }

    @Override
    public boolean setStarterFor(String nickname, boolean isRetro) throws RemoteException {
        try {
            cardController.setStarterFor(nickname, isRetro);
            return true;
        } catch (Exception e) {
            throw new RemoteException("Failed to set starter for player", e);
        }
    }

    @Override
    public int drawCard(boolean fromVisible, PlayableCardType type, String nickname, int cardID)
    throws RemoteException {
        try {
            return cardController.drawCard(fromVisible, type, nickname, cardID);
        } catch (Exception e) {
            throw new RemoteException("Failed to draw card", e);
        }
    }

    @Override
    public boolean placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws RemoteException {
        try {
            cardController.placeCard(Nickname, ID, position, isRetro);
            return true;
        } catch (Exception e) {
            throw new RemoteException("Failed to place card", e);
        }
    }

    @Override
    public boolean forceEnd() throws RemoteException {
        try {
            gameController.forceEnd();
            return true;
        } catch (Exception e) {
            throw new RemoteException("Failed to force end", e);
        }
    }
}

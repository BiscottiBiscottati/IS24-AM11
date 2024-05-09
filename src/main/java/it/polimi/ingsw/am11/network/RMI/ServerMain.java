package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerMain implements Loggable, PlayerViewInterface {

    static int PORT = 1234;
    ConnectorServerInterface connector;
    VirtualPlayerView view;
    private List<String> registeredClients;

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
    throws ServerException {
        try {
            view = new VirtualPlayerView(connector, nick);
            view = CentralController.INSTANCE
                    .connectPlayer(nick, connector, connector);
            registeredClients.add(nick);
            System.out.println(nick + " connected");
        } catch (PlayerInitException e) {
            throw new ServerException("Invalid player init.");
        } catch (GameStatusException e) {
            throw new ServerException("Game status exception.");
        } catch (NumOfPlayersException e) {
            throw new ServerException("Max players reached.");
        }
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

    public void updateHand(int cardId, boolean removeMode) throws RemoteException {
        connector.updateHand(cardId, removeMode);
    }

    public void updatePersonalObjective(int cardId, boolean removeMode) throws RemoteException {
        connector.updatePersonalObjective(cardId, removeMode);
    }

    public void sendStarterCard(int cardId) throws RemoteException {
        connector.sendStarterCard(cardId);
    }

    public void sendCandidateObjective(Set<Integer> cardsId) throws RemoteException {
        connector.sendCandidateObjective(cardsId);
    }

    public void updateDeckTop(PlayableCardType type, Color color) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.updateDeckTop(type, color);
        });
    }

    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.updateField(nickname, x, y, cardId, isRetro, removeMode);
        });
    }

    public void updateShownPlayable(Integer previousId, Integer currentId) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.updateShownPlayable(previousId, currentId);
        });
    }

    public void updateTurnChange(String nickname) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.updateTurnChange(nickname);
        });
    }

    public void updatePlayerPoint(String nickname, int points) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.updatePlayerPoint(nickname, points);
        });
    }

    public void updateGameStatus(GameStatus status) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.updateGameStatus(status);
        });
    }

    public void updateCommonObjective(int cardId, boolean removeMode) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.updateCommonObjective(cardId, removeMode);
        });
    }

    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) throws RemoteException {
        registeredClients.forEach(nick -> {
            connector.sendFinalLeaderboard(finalLeaderboard);
        });
    }

}

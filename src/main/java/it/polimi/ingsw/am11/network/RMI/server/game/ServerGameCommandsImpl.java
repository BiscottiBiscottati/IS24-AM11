package it.polimi.ingsw.am11.network.RMI.server.game;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.RMI.remote.game.ServerGameCommandsInterface;
import it.polimi.ingsw.am11.network.RMI.server.chat.ServerChatConnectorImpl;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerGameCommandsImpl implements ServerGameCommandsInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerGameCommandsImpl.class);
    private final @NotNull Map<String, VirtualPlayerView> views;

    public ServerGameCommandsImpl() {
        this.views = new ConcurrentHashMap<>(8);
    }

    public void addPlayer(@NotNull String nick,
                          @NotNull ServerConnectorImpl connector,
                          @NotNull ServerChatConnectorImpl chatConnector)
    throws NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException {
        synchronized (views) {
            VirtualPlayerView view =
                    CentralController.INSTANCE.connectPlayer(nick, connector, connector,
                                                             chatConnector);
            views.put(nick, view);
        }
    }

    public void removePlayer(@NotNull String nick) {
        synchronized (views) {
            views.remove(nick);
        }
    }

    public void clearPlayers() {
        synchronized (views) {
            views.clear();
        }
    }

    @Override
    public void setStarterCard(@NotNull String nick, boolean isRetro)
    throws RemoteException, PlayerInitException, IllegalCardPlacingException, GameStatusException {
        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                view.setStarterCard(isRetro);
            } else {
                throw new RemoteException("Use login method to add player first.");
            }
        }
    }

    @Override
    public void setObjectiveCard(@NotNull String nick, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, PlayerInitException,
           GameStatusException {
        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                view.setObjectiveCard(cardId);
            } else {
                throw new RemoteException("Use login method to add player first.");
            }
        }

    }

    @Override
    public void placeCard(@NotNull String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException, TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException {

        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                view.placeCard(cardId, x, y, isRetro);
            } else {
                throw new RemoteException("Use login method to add player first.");
            }
        }
    }

    @Override
    public void drawCard(@NotNull String nick, boolean fromVisible, @NotNull PlayableCardType type,
                         int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, TurnsOrderException,
           IllegalPickActionException, PlayerInitException, EmptyDeckException,
           MaxHandSizeException, GameStatusException {

        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                view.drawCard(fromVisible, type, cardId);
            } else {
                throw new RemoteException("Use login method to add player first.");
            }
        }
    }

    @Override
    public void setNumOfPlayers(@NotNull String nick, int numOfPlayers)
    throws RemoteException, NumOfPlayersException, NotGodPlayerException, GameStatusException {
        CentralController.INSTANCE.setNumOfPlayers(nick, numOfPlayers);
    }

    @Override
    public void syncMeUp(@NotNull String nick) {
        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                view.syncMeUp();
            }
        }
    }
}

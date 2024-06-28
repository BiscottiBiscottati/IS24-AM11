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

/**
 * Implementation of the {@link ServerGameCommandsInterface} interface for RMI connections. This
 * class is responsible for receiving commands from the clients for game purposes. It is used to add
 * and remove players, set the starter card, set the objective card, place a card, draw a card, set
 * the number of players and synchronize the client's view.
 */
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
    throws RemoteException, GameStatusException {
        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                view.setStarterCard(isRetro);
            } else {
                LOGGER.warn("Player {} tried to set starter card without being logged in. Players" +
                            " {}",
                            nick, views.keySet());
                throw new RemoteException("Use login method to add player first.");
            }
        }
    }

    @Override
    public void setObjectiveCard(@NotNull String nick, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException,
           GameStatusException {
        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                try {
                    view.setObjectiveCard(cardId);
                } catch (PlayerInitException e) {
                    throw new GameStatusException("Player not initialized");
                }
            } else {
                LOGGER.warn("Player {} tried to set objective card without being logged in. " +
                            "Players {}",
                            nick, views.keySet());
                throw new RemoteException("Use login method to add player first.");
            }
        }

    }

    @Override
    public void placeCard(@NotNull String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException, TurnsOrderException, IllegalCardPlacingException,
           NotInHandException, GameStatusException {

        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                try {
                    view.placeCard(cardId, x, y, isRetro);
                } catch (PlayerInitException | IllegalPlateauActionException e) {
                    throw new GameBreakingException("Player not initialized");
                }
            } else {
                LOGGER.warn("Player {} tried to place card without being logged in. Players {}",
                            nick, views.keySet());
                throw new RemoteException("Use login method to add player first.");
            }
        }
    }

    @Override
    public void drawCard(@NotNull String nick, boolean fromVisible, @NotNull PlayableCardType type,
                         int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, TurnsOrderException,
           IllegalPickActionException, EmptyDeckException,
           MaxHandSizeException, GameStatusException {

        synchronized (views) {
            if (views.containsKey(nick)) {
                VirtualPlayerView view = views.get(nick);
                try {
                    view.drawCard(fromVisible, type, cardId);
                } catch (PlayerInitException e) {
                    throw new GameBreakingException("Player not initialized");
                }
            } else {
                LOGGER.warn("Player {} tried to draw card without being logged in. Players {}",
                            nick, views.keySet());
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

package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

public class PlayerViewImpl implements PlayerViewInterface {
    private final Map<String, VirtualPlayerView> views;

    public PlayerViewImpl() throws RemoteException {
        this.views = new HashMap<>(8);
    }

    public void addPlayer(String nick, VirtualPlayerView view) {
        views.put(nick, view);
    }

    @Override
    public void setStarterCard(String nick, boolean isRetro)
    throws RemoteException, PlayerInitException, IllegalCardPlacingException, GameStatusException {
        if (views.containsKey(nick)) {
            VirtualPlayerView view = views.get(nick);
            view.setStarterCard(isRetro);
        } else {
            throw new ServerException("Player not found");
        }
    }

    @Override
    public void setObjectiveCard(String nick, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, PlayerInitException,
           GameStatusException {
        if (views.containsKey(nick)) {
            VirtualPlayerView view = views.get(nick);
            view.setObjectiveCard(cardId);
        } else {
            throw new ServerException("Player not found");
        }

    }

    @Override
    public void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException, TurnsOrderException, PlayerInitException, IllegalCardPlacingException,
           NotInHandException, IllegalPlateauActionException, GameStatusException {

        if (views.containsKey(nick)) {
            VirtualPlayerView view = views.get(nick);
            view.placeCard(cardId, x, y, isRetro);
        } else {
            throw new ServerException("Player not found");
        }
    }

    @Override
    public void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws RemoteException, IllegalPlayerSpaceActionException, TurnsOrderException,
           IllegalPickActionException, PlayerInitException, EmptyDeckException,
           MaxHandSizeException, GameStatusException {

        if (views.containsKey(nick)) {
            VirtualPlayerView view = views.get(nick);
            view.drawCard(fromVisible, type, cardId);
        } else {
            throw new ServerException("Player not found");
        }
    }

    public boolean containsPlayer(String nick) {
        return views.containsKey(nick);
    }
}

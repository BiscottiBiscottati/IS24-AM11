package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.rmi.ServerException;

public class PlayerViewImpl implements PlayerViewInterface {
    private final VirtualPlayerView view;

    public PlayerViewImpl(@NotNull VirtualPlayerView view) {
        this.view = view;
    }

    @Override
    public void setStarterCard(String nick, boolean isRetro)
    throws RemoteException {
        try {
            view.setStarterCard(isRetro);
        } catch (PlayerInitException | GameStatusException |
                 IllegalCardPlacingException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }
    }

    @Override
    public void setObjectiveCard(String nick, int cardId)
    throws RemoteException {
        try {
            view.setObjectiveCard(cardId);
        } catch (IllegalPlayerSpaceActionException | PlayerInitException | GameStatusException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }

    }

    @Override
    public void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException {
        try {
            view.placeCard(cardId, x, y, isRetro);
        } catch (TurnsOrderException | PlayerInitException | IllegalCardPlacingException |
                 NotInHandException | IllegalPlateauActionException | GameStatusException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }

    }

    @Override
    public void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws RemoteException {
        try {
            view.drawCard(fromVisible, type, cardId);
        } catch (IllegalPlayerSpaceActionException | TurnsOrderException |
                 IllegalPickActionException |
                 PlayerInitException | EmptyDeckException | MaxHandSizeException |
                 GameStatusException e) {
            throw new ServerException(e.getClass().getCanonicalName(), e);
        }
    }
}

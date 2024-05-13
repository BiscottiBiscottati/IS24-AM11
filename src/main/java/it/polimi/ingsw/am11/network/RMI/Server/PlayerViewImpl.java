package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.view.server.VirtualPlayerView;

import java.rmi.RemoteException;

public class PlayerViewImpl implements PlayerViewInterface {
    private final VirtualPlayerView view;

    public PlayerViewImpl(VirtualPlayerView view) {
        this.view = view;
    }

    @Override
    public void setStarterCard(String nick, boolean isRetro)
    throws RemoteException {
        try {
            view.setStarterCard(isRetro);
        } catch (PlayerInitException e) {
            throw new RemoteException("PlayerInitException", e);
        } catch (IllegalCardPlacingException e) {
            throw new RemoteException("IllegalCardPlacingException", e);
        } catch (GameStatusException e) {
            throw new RemoteException("GameStatusException", e);
        }
    }

    @Override
    public void setObjectiveCard(String nick, int cardId)
    throws RemoteException {
        try {
            view.setObjectiveCard(cardId);
        } catch (IllegalPlayerSpaceActionException e) {
            throw new RemoteException("IllegalPlayerSpaceActionException", e);
        } catch (PlayerInitException e) {
            throw new RemoteException("PlayerInitException", e);
        } catch (GameStatusException e) {
            throw new RemoteException("GameStatusException", e);
        }

    }

    @Override
    public void placeCard(String nick, int cardId, int x, int y, boolean isRetro)
    throws RemoteException {
        try {
            view.placeCard(cardId, x, y, isRetro);
        } catch (TurnsOrderException e) {
            throw new RemoteException("TurnsOrderException", e);
        } catch (PlayerInitException e) {
            throw new RemoteException("PlayerInitException", e);
        } catch (IllegalCardPlacingException e) {
            throw new RemoteException("IllegalCardPlacingException", e);
        } catch (NotInHandException e) {
            throw new RemoteException("NotInHandException", e);
        } catch (IllegalPlateauActionException e) {
            throw new RemoteException("IllegalPlateauActionException", e);
        } catch (GameStatusException e) {
            throw new RemoteException("GameStatusException", e);
        }

    }

    @Override
    public void drawCard(String nick, boolean fromVisible, PlayableCardType type, int cardId)
    throws RemoteException {
        try {
            view.drawCard(fromVisible, type, cardId);
        } catch (IllegalPlayerSpaceActionException e) {
            throw new RemoteException("IllegalPlayerSpaceActionException", e);
        } catch (PlayerInitException e) {
            throw new RemoteException("PlayerInitException", e);
        } catch (IllegalPickActionException e) {
            throw new RemoteException("IllegalPickActionException", e);
        } catch (EmptyDeckException e) {
            throw new RemoteException("EmptyDeckException", e);
        } catch (GameStatusException e) {
            throw new RemoteException("GameStatusException", e);
        } catch (TurnsOrderException e) {
            throw new RemoteException("TurnsOrderException", e);
        } catch (MaxHandSizeException e) {
            throw new RemoteException("MaxHandSizeException", e);
        }
    }

    @Override
    public void setNumOfPlayers(String nick, int numOfPlayers)
    throws RemoteException {
        try {
            CentralController.INSTANCE.setNumOfPlayers(nick, numOfPlayers);
        } catch (NotGodPlayerException e) {
            throw new RemoteException("NotGodPlayerException", e);
        } catch (GameStatusException e) {
            throw new RemoteException("GameStatusException", e);
        } catch (NumOfPlayersException e) {
            throw new RemoteException("NumOfPlayersException", e);
        }
    }
}

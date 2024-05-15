package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.TableConnector;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public class ConnectorImplementation implements PlayerConnector, TableConnector {
    private final ConnectorInterface remoteConnector;

    public ConnectorImplementation(ConnectorInterface remoteConnector) {
        this.remoteConnector = remoteConnector;
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        try {
            remoteConnector.updateHand(cardId, removeMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        try {
            remoteConnector.updatePersonalObjective(cardId, removeMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendStarterCard(int cardId) {
        try {
            remoteConnector.receiveStarterCard(cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendCandidateObjective(Set<Integer> cardsId) {
        try {
            remoteConnector.receiveCandidateObjective(cardsId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        try {
            remoteConnector.updateDeckTop(type, color);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
        try {
            remoteConnector.updateField(nickname, x, y, cardId, isRetro, removeMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        try {
            remoteConnector.updateShownPlayable(previousId, currentId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTurnChange(String nickname) {
        try {
            remoteConnector.updateTurnChange(nickname);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        try {
            remoteConnector.updatePlayerPoint(nickname, points);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        try {
            remoteConnector.updateGameStatus(status);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardsId, boolean removeMode) {
        try {
            for (int cardId : cardsId) {
                remoteConnector.updateCommonObjective(cardId, removeMode);
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        try {
            remoteConnector.sendFinalLeaderboard(finalLeaderboard);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {

    }
}

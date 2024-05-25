package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.ServerTableConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

public class ServerConnectorImpl
        implements ServerPlayerConnector, ServerTableConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            ServerConnectorImpl.class);

    private final ClientGameUpdatesInterface remoteConnector;

    public ServerConnectorImpl(@NotNull ClientGameUpdatesInterface remoteConnector) {
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
    public void notifyGodPlayer() {
        LOGGER.info("SERVER RMI: Notifying god player");
        try {
            remoteConnector.notifyGodPlayer();
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while notifying god player");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        try {
            remoteConnector.updateDeckTop(type, color);
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while updating deck top");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
        try {
            remoteConnector.updateField(nickname, x, y, cardId, isRetro, removeMode);
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while updating field");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        try {
            remoteConnector.updateShownPlayable(previousId, currentId);
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while updating shown playable");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTurnChange(String nickname) {
        try {
            remoteConnector.updateTurnChange(nickname);
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while updating turn change");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        try {
            remoteConnector.updatePlayerPoint(nickname, points);
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while updating player point");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        try {
            remoteConnector.updateGameStatus(status);
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while updating game status");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardsId, boolean removeMode) {
        try {
            remoteConnector.updateCommonObjective(cardsId, removeMode);
        } catch (NoSuchObjectException e) {
            LOGGER.warn(
                    "SERVER RMI: Player disconnected or closed while updating common objective");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        try {
            remoteConnector.sendFinalLeaderboard(finalLeaderboard);
        } catch (NoSuchObjectException e) {
            LOGGER.warn(
                    "SERVER RMI: Player disconnected or closed while sending final leaderboard");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {
        try {
            remoteConnector.updatePlayers(currentPlayers);
        } catch (NoSuchObjectException e) {
            LOGGER.warn("SERVER RMI: Player disconnected or closed while updating players");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateNumOfPlayers(Integer numOfPlayers) {
        try {
            remoteConnector.updateNumOfPlayers(numOfPlayers);
        } catch (NoSuchObjectException e) {
            LOGGER.warn(
                    "SERVER RMI: Player disconnected or closed while updating number of players");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}

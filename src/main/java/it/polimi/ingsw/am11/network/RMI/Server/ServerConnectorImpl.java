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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnectorImpl
        implements ServerPlayerConnector, ServerTableConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectorImpl.class);

    private final ClientGameUpdatesInterface remoteConnector;
    private final ExecutorService executorService;

    public ServerConnectorImpl(@NotNull ClientGameUpdatesInterface remoteConnector) {
        this.remoteConnector = remoteConnector;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        executorService.submit(() -> {
            try {
                remoteConnector.updateHand(cardId, removeMode);
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while updating hand");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating hand", e);
            }
        });
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        executorService.submit(() -> {
            try {
                remoteConnector.updatePersonalObjective(cardId, removeMode);
            } catch (NoSuchObjectException e) {
                LOGGER.warn(
                        "SERVER RMI: Player disconnected or closed while updating personal " +
                        "objective");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating personal objective", e);
            }
        });
    }

    @Override
    public void sendStarterCard(int cardId) {
        executorService.submit(() -> {
            try {
                remoteConnector.receiveStarterCard(cardId);
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while sending starter card");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while sending starter card", e);
            }
        });
    }

    @Override
    public void sendCandidateObjective(Set<Integer> cardsId) {
        executorService.submit(() -> {
            try {
                remoteConnector.receiveCandidateObjective(cardsId);
            } catch (NoSuchObjectException e) {
                LOGGER.warn(
                        "SERVER RMI: Player disconnected or closed while sending candidate " +
                        "objective");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while sending candidate objective", e);
            }
        });
    }

    @Override
    public void notifyGodPlayer() {
        LOGGER.info("SERVER RMI: Notifying god player");
        executorService.submit(() -> {
            try {
                remoteConnector.notifyGodPlayer();
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while notifying god player");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while notifying god player", e);
            }
        });
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        executorService.submit(() -> {
            try {
                remoteConnector.updateDeckTop(type, color);
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while updating deck top");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating deck top", e);
            }
        });
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {

        executorService.submit(() -> {
            try {
                remoteConnector.updateField(nickname, x, y, cardId, isRetro, removeMode);
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while updating field");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating field", e);
            }
        });
    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        executorService.submit(() -> {
            try {
                remoteConnector.updateShownPlayable(previousId, currentId);
            } catch (NoSuchObjectException e) {
                LOGGER.warn(
                        "SERVER RMI: Player disconnected or closed while updating shown playable");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating shown playable", e);
            }
        });
    }

    @Override
    public void updateTurnChange(String nickname) {
        executorService.submit(() -> {
            try {
                remoteConnector.updateTurnChange(nickname);
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while updating turn change");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating turn change", e);
            }
        });
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        executorService.submit(() -> {
            try {
                remoteConnector.updatePlayerPoint(nickname, points);
            } catch (NoSuchObjectException e) {
                LOGGER.warn(
                        "SERVER RMI: Player disconnected or closed while updating player point");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating player point", e);
            }
        });
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        executorService.submit(() -> {
            try {
                remoteConnector.updateGameStatus(status);
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while updating game status");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating game status", e);
            }
        });
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardsId, boolean removeMode) {
        executorService.submit(() -> {
            try {
                remoteConnector.updateCommonObjective(cardsId, removeMode);
            } catch (NoSuchObjectException e) {
                LOGGER.warn(
                        "SERVER RMI: Player disconnected or closed while updating common " +
                        "objective");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating common objective", e);
            }
        });
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        executorService.submit(() -> {
            try {
                remoteConnector.sendFinalLeaderboard(finalLeaderboard);
            } catch (NoSuchObjectException e) {
                LOGGER.warn(
                        "SERVER RMI: Player disconnected or closed while sending final " +
                        "leaderboard");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while sending final leaderboard", e);
            }
        });
    }

    @Override
    public void updatePlayers(Map<PlayerColor, String> currentPlayers) {
        executorService.submit(() -> {
            try {
                remoteConnector.updatePlayers(currentPlayers);
            } catch (NoSuchObjectException e) {
                LOGGER.warn("SERVER RMI: Player disconnected or closed while updating players");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating players", e);
            }
        });
    }

    @Override
    public void updateNumOfPlayers(Integer numOfPlayers) {
        executorService.submit(() -> {
            try {
                remoteConnector.updateNumOfPlayers(numOfPlayers);
            } catch (NoSuchObjectException e) {
                LOGGER.warn(
                        "SERVER RMI: Player disconnected or closed while updating number of " +
                        "players");
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error while updating number of players", e);
            }
        });
    }
}

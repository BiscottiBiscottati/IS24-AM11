package it.polimi.ingsw.am11.network.RMI.server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.RMI.remote.heartbeat.HeartbeatInterface;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.concurrent.*;

/**
 * This class is used by the server to manage the heartbeat of the clients.
 * Implements the {@link HeartbeatInterface} interface.
 */
public class HeartbeatManager implements HeartbeatInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatManager.class);
    private static final int HEARTBEAT_INTERVAL = 1000;
    private static final int HEARTBEAT_TIMEOUT = 5000;

    private final @NotNull ConcurrentHashMap<String, Long> lastHeartbeat;
    private final @NotNull ScheduledExecutorService heartbeatsService;
    private final @NotNull ExecutorService disconnectService;
    private final @NotNull ServerRMI serverRMI;

    public HeartbeatManager(@NotNull ServerRMI serverRMI) {
        this.lastHeartbeat = new ConcurrentHashMap<>(8);
        this.heartbeatsService = Executors.newSingleThreadScheduledExecutor();
        this.disconnectService = Executors.newCachedThreadPool();
        this.serverRMI = serverRMI;

        this.heartbeatsService.scheduleAtFixedRate(this::checkHeartbeat,
                                                   HEARTBEAT_INTERVAL,
                                                   HEARTBEAT_INTERVAL,
                                                   TimeUnit.MILLISECONDS);
    }

    /**
     * Check the heartbeat of the clients.
     * If the heartbeat is missed, the client is disconnected.
     * If the heartbeat is timed out, the client is disconnected.
     * The client is disconnected by the {@link CentralController} and removed from the server.
     * The client is removed from the {@link #lastHeartbeat} map.
     * The client is removed from the {@link ServerRMI} object.
     */
    private void checkHeartbeat() {
        long now = System.currentTimeMillis();
        lastHeartbeat.forEach((nickname, last) -> {
            long interval = now - last;
            if (interval > HEARTBEAT_INTERVAL)
                LOGGER.debug("SERVER RMI: Ping interval missed {}ms for player {}",
                             interval, nickname);
            if (interval > HEARTBEAT_TIMEOUT) {
                LOGGER.info("SERVER RMI: Heartbeat timeout for player {} of {}ms",
                            nickname, interval);
                disconnectService.submit(() -> {
                    lastHeartbeat.remove(nickname);
                    CentralController.INSTANCE.disconnectPlayer(nickname);
                    serverRMI.removePlayer(nickname);
                });
            }
        });
    }

    @Override
    public void ping(@NotNull String nickname) throws RemoteException {
        lastHeartbeat.put(nickname, System.currentTimeMillis());
    }

    @Override
    public int getInterval() throws RemoteException {
        return HEARTBEAT_INTERVAL;
    }

    public void close() {
        clear();
        heartbeatsService.shutdown();
        disconnectService.shutdown();
    }

    public void clear() {
        lastHeartbeat.clear();
    }
}

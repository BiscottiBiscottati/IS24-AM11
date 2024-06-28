package it.polimi.ingsw.am11.network.RMI.client;

import it.polimi.ingsw.am11.network.RMI.remote.heartbeat.HeartbeatInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is the heartbeat sender.
 * It sends the heartbeat to the server.
 * <p>
 *     The heartbeat is sent every HEARTBEAT_INTERVAL milliseconds.
 *     The nickname is set by the client when the client is connected to the server.
 *     The nickname is used to send the heartbeat.
 * </p>
 */
public class HeartbeatSender implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatSender.class);
    private static int HEARTBEAT_INTERVAL = 1000;

    private final @NotNull ClientRMI clientRMI;
    private final @NotNull ScheduledExecutorService heartbeatsService;
    private final @NotNull HeartbeatInterface heartbeat;
    private final @NotNull ClientViewUpdater updater;
    private final @NotNull AtomicReference<String> nickname;
    private boolean isRunning;

    public HeartbeatSender(@NotNull HeartbeatInterface heartbeat,
                           @NotNull ClientViewUpdater updater,
                           @NotNull ClientRMI clientRMI) {
        this.heartbeatsService = Executors.newSingleThreadScheduledExecutor();
        this.heartbeat = heartbeat;
        this.updater = updater;
        this.nickname = new AtomicReference<>(null);
        this.isRunning = true;
        this.clientRMI = clientRMI;

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        isRunning = false;
        heartbeatsService.shutdown();
    }

    public static void setHeartbeatInterval(int interval) {
        if (interval <= 0) return;
        HEARTBEAT_INTERVAL = interval;
    }

    /**
     * Sends the heartbeat to the server
     * <p>
     *     The heartbeat is sent only if the client is connected to the server.
     *     If the client is not connected to the server, the client is disconnected from the server.
     * </p>
     */
    @Override
    public void run() {
        String tempNickname = nickname.get();
        if (! isRunning || tempNickname == null) {
            return;
        }
        try {
            heartbeat.ping(tempNickname);
        } catch (RemoteException e) {
            LOGGER.debug("Error while sending heartbeat: {}", e.getMessage());
            LOGGER.debug("Disconnecting from server");
            updater.disconnectedFromServer(e.getMessage());
            clientRMI.close();
        }

    }

    public void setNickname(@NotNull String nickname) {
        this.nickname.set(nickname);
        heartbeatsService.scheduleAtFixedRate(this,
                                              HEARTBEAT_INTERVAL,
                                              HEARTBEAT_INTERVAL,
                                              TimeUnit.MILLISECONDS);
    }
}

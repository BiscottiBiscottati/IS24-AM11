package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.HeartbeatInterface;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

public class HeartbeatSender implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatSender.class);

    private final ClientRMI clientRMI;
    private final HeartbeatInterface heartbeat;
    private final ClientViewUpdater updater;
    private final AtomicReference<String> nickname;
    private boolean isRunning;

    public HeartbeatSender(@NotNull HeartbeatInterface heartbeat,
                           @NotNull ClientViewUpdater updater,
                           @NotNull ClientRMI clientRMI) {
        this.heartbeat = heartbeat;
        this.updater = updater;
        this.nickname = new AtomicReference<>(null);
        this.isRunning = true;
        this.clientRMI = clientRMI;

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        if (! isRunning) {
            LOGGER.debug("HeartbeatSender is not running, skipping heartbeat");
            return;
        }

        String tempNickname = nickname.get();
        if (tempNickname == null) {
            LOGGER.debug("Nickname not set, skipping heartbeat");
            return;
        }
        try {
            heartbeat.ping(tempNickname);
        } catch (RemoteException e) {
            LOGGER.error("Error while sending heartbeat: {}", e.getMessage());
            LOGGER.debug("Disconnecting from server");
            updater.disconnectedFromServer();
            clientRMI.close();
            isRunning = false;
        }

    }

    public void setNickname(@NotNull String nickname) {
        this.nickname.set(nickname);
    }
}

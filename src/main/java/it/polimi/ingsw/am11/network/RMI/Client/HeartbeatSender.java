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

    private final @NotNull ClientRMI clientRMI;
    private final @NotNull HeartbeatInterface heartbeat;
    private final @NotNull ClientViewUpdater updater;
    private final @NotNull AtomicReference<String> nickname;
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
        String tempNickname = nickname.get();
        if (! isRunning || tempNickname == null) {
            return;
        }
        try {
            heartbeat.ping(tempNickname);
        } catch (RemoteException e) {
            LOGGER.debug("Error while sending heartbeat: {}", e.getMessage());
            LOGGER.debug("Disconnecting from server");
            updater.disconnectedFromServer();
            clientRMI.close();
        }

    }

    public void setNickname(@NotNull String nickname) {
        this.nickname.set(nickname);
    }
}

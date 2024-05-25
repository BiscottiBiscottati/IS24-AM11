package it.polimi.ingsw.am11.network.RMI.Server;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.HearbeatInterface;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.concurrent.*;

public class HeartbeatManager implements HearbeatInterface {
    private static final int HEARTBEAT_INTERVAL = 5000;
    private static final int HEARTBEAT_TIMEOUT = 20000;

    private final ConcurrentHashMap<String, Long> lastHeartbeat;
    private final ScheduledExecutorService heartbeatsService;
    private final ExecutorService disconnectService;

    public HeartbeatManager() {
        this.lastHeartbeat = new ConcurrentHashMap<>(8);
        this.heartbeatsService = Executors.newSingleThreadScheduledExecutor();
        this.disconnectService = Executors.newCachedThreadPool();

        this.heartbeatsService.scheduleAtFixedRate(this::checkHeartbeat,
                                                   HEARTBEAT_INTERVAL,
                                                   HEARTBEAT_INTERVAL,
                                                   TimeUnit.MILLISECONDS);
    }

    private void checkHeartbeat() {
        long now = System.currentTimeMillis();
        lastHeartbeat.forEach((nickname, last) -> {
            if (now - last > HEARTBEAT_TIMEOUT) {
                disconnectService.submit(() -> {
                    CentralController.INSTANCE.disconnectPlayer(nickname);
                    lastHeartbeat.remove(nickname);
                });
            }
        });
    }

    @Override
    public void ping(@NotNull String nickname) throws RemoteException {
        lastHeartbeat.put(nickname, System.currentTimeMillis());
    }

    public void close() {
        heartbeatsService.shutdown();
        lastHeartbeat.clear();
    }
}

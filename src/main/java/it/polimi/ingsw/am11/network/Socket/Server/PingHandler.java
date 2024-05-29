package it.polimi.ingsw.am11.network.Socket.Server;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PingHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingHandler.class);
    private static final int PING_INTERVAL = 1000;
    private static final int PING_TIMEOUT = 3000;

    private final Socket clientSocket;
    private final ScheduledExecutorService pingExecutor;
    private final AtomicLong lastPing;

    public PingHandler(@NotNull Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.lastPing = new AtomicLong(- 1);
        this.pingExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long last = lastPing.get();
        if (last == - 1) return;
        if (now - last > PING_TIMEOUT) {
            LOGGER.info("SERVER TCP: Ping timeout, closing connection");
            try {
                clientSocket.close();
            } catch (Exception e) {
                LOGGER.error("SERVER TCP: Error while closing socket: {}", e.getMessage());
            }
        }
    }

    public void ping() {
        if (lastPing.compareAndSet(- 1, System.currentTimeMillis())) {
            pingExecutor.scheduleAtFixedRate(this, PING_INTERVAL, PING_INTERVAL,
                                             TimeUnit.MILLISECONDS);
        } else lastPing.set(System.currentTimeMillis());
    }

    public void stop() {
        pingExecutor.shutdown();
    }
}

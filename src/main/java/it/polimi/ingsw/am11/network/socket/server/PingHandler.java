package it.polimi.ingsw.am11.network.socket.server;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.am11.network.socket.utils.ContextJSON;
import it.polimi.ingsw.am11.network.socket.utils.JsonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
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
    private final PrintWriter out;
    private final ObjectNode pongMessage;
    private final ScheduledExecutorService pingExecutor;
    private final AtomicLong lastPing;

    public PingHandler(@NotNull Socket clientSocket,
                       @NotNull PrintWriter out) {
        this.clientSocket = clientSocket;
        this.out = out;
        this.pongMessage = JsonFactory.createObjectNode(ContextJSON.PING);
        this.lastPing = new AtomicLong(- 1);
        this.pingExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long last = lastPing.get();
        if (last == - 1) return;
        if (now - last > PING_TIMEOUT) {
            LOGGER.info("SERVER TCP: A ping timeout occurred");
            try {
                clientSocket.close();
            } catch (Exception e) {
                LOGGER.error("SERVER TCP: Error while closing socket: {}", e.getMessage());
            } finally {
                pingExecutor.shutdown();
            }
        }
    }

    public void ping() {
        if (lastPing.compareAndSet(- 1, System.currentTimeMillis())) {
            pingExecutor.scheduleAtFixedRate(this, PING_INTERVAL, PING_INTERVAL,
                                             TimeUnit.MILLISECONDS);
        } else lastPing.set(System.currentTimeMillis());
        out.println(pongMessage);
    }

    public void close() {
        pingExecutor.shutdown();
    }
}

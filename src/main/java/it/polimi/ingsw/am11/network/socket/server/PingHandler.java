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

/**
 * The class handles the ping messages and checks if the ping messages are received It implements
 * the {@link Runnable} interface
 */
public class PingHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingHandler.class);
    private static final int PING_INTERVAL = 1000;
    private static final int PING_TIMEOUT = 5000;

    private final @NotNull Socket clientSocket;
    private final @NotNull PrintWriter out;
    private final @NotNull ObjectNode pongMessage;
    private final @NotNull ScheduledExecutorService pingExecutor;
    private final @NotNull AtomicLong lastPing;

    public PingHandler(@NotNull Socket clientSocket,
                       @NotNull PrintWriter out) {
        this.clientSocket = clientSocket;
        this.out = out;
        this.pongMessage = JsonFactory.createObjectNode(ContextJSON.PING);
        this.lastPing = new AtomicLong(- 1);
        this.pingExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * This method checks if the ping messages are received. If the interval between the ping
     * messages is greater than the {@link #PING_INTERVAL} it logs a message, if the interval
     * between the ping messages is greater than the {@link #PING_TIMEOUT} it logs a message and
     * closes the connection with the client.
     */
    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long last = lastPing.get();
        if (last == - 1) return;
        long interval = now - last;
        if (interval > PING_INTERVAL) LOGGER.debug("SERVER TCP: Ping interval missed {}ms from {}",
                                                   interval, clientSocket.getInetAddress());
        if (interval > PING_TIMEOUT) {
            LOGGER.info("SERVER TCP: A ping timeout occurred {}ms from {}",
                        interval, clientSocket.getInetAddress());
            try {
                clientSocket.close();
            } catch (Exception e) {
                LOGGER.error("SERVER TCP: Error while closing socket: {}", e.getMessage());
            } finally {
                pingExecutor.shutdown();
            }
        }
    }

    /**
     * This method is called when a ping message is received, it sends a pong message to the client.
     * It also schedules the check for the ping messages.
     */
    public void ping() {
        if (lastPing.compareAndSet(- 1, System.currentTimeMillis())) {
            pingExecutor.scheduleAtFixedRate(this, PING_INTERVAL, PING_INTERVAL,
                                             TimeUnit.MILLISECONDS);
        } else lastPing.set(System.currentTimeMillis());
        out.println(pongMessage);
    }

    /**
     * Used to shut down the ping handler and consequently the connection with the client.
     */
    public void close() {
        if (! pingExecutor.isShutdown()) pingExecutor.shutdown();
    }
}

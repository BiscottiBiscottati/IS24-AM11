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
 * The class that handles the ping messages
 * <p>
 *     The class that handles the ping messages and checks if the ping messages are received
 *     <br>
 *     It implements the {@link Runnable} interface
 *     <br>
 *     It uses a {@link Socket} to handle the connection
 *     <br>
 *     It uses a {@link PrintWriter} to send the messages to the client
 *     <br>
 *     It uses a {@link AtomicLong} to store the last ping message received
 *     <br>
 *     It uses a {@link ScheduledExecutorService} to schedule the ping messages
 *     <br>
 *     It uses a {@link ObjectNode} to store the pong message
 *     <br>
 *     It uses a {@link Logger} to log the messages
 *     <br>
 *     It uses the {@link #PING_INTERVAL} to set the interval between the ping messages
 *     <br>
 *     It uses the {@link #PING_TIMEOUT} to set the timeout for the ping messages
 *     <br>
 *     It uses the {@link #ping()} method to send the ping messages
 *     <br>
 *     It uses the {@link #close()} method to close the ping messages
 *     <br>
 *     It uses the {@link #run()} method to check if the ping messages are received
 * </p>
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

    public void ping() {
        if (lastPing.compareAndSet(- 1, System.currentTimeMillis())) {
            pingExecutor.scheduleAtFixedRate(this, PING_INTERVAL, PING_INTERVAL,
                                             TimeUnit.MILLISECONDS);
        } else lastPing.set(System.currentTimeMillis());
        out.println(pongMessage);
    }

    public void close() {
        if (! pingExecutor.isShutdown()) pingExecutor.shutdown();
    }
}

package it.polimi.ingsw.am11.network.socket.client;

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
 * The class that handles the pong messages
 * <p>
 *     The class that handles the pong messages and checks if the pong messages are received
 *     <br>
 *     It implements the {@link Runnable} interface
 *     <br>
 *     It uses a {@link Socket} to handle the connection
 *     <br>
 *     It uses a {@link PrintWriter} to send the messages to the server
 *     <br>
 *     It uses a {@link AtomicLong} to store the last pong message received
 *     <br>
 *     It uses a {@link ScheduledExecutorService} to schedule the pong messages
 *     <br>
 *     It uses a {@link ObjectNode} to store the ping message
 *     <br>
 *     It uses a {@link Logger} to log the messages
 *     <br>
 *     It uses the {@link #PONG_INTERVAL} to set the interval between the pong messages
 *     <br>
 *     It uses the {@link #PONG_TIMEOUT} to set the timeout for the pong messages
 *     <br>
 * </p>
 */
public class PongHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PongHandler.class);
    private static final int PONG_INTERVAL = 1000;
    private static final int PONG_TIMEOUT = 5000;

    private final @NotNull Socket socket;
    private final @NotNull PrintWriter out;
    private final @NotNull AtomicLong lastPong;
    private final @NotNull ScheduledExecutorService pongExecutor;
    private final @NotNull ObjectNode pingMessage;

    public PongHandler(@NotNull Socket socket,
                       @NotNull PrintWriter out) {
        this.socket = socket;
        this.out = out;
        this.lastPong = new AtomicLong(- 1);
        this.pongExecutor = Executors.newSingleThreadScheduledExecutor();
        this.pingMessage = JsonFactory.createObjectNode(ContextJSON.PING);
    }

    public void start() {
        out.println(pingMessage);
    }

    public void pong() {
        if (lastPong.compareAndSet(- 1, System.currentTimeMillis())) {
            pongExecutor.scheduleAtFixedRate(this, PONG_INTERVAL, PONG_INTERVAL,
                                             TimeUnit.MILLISECONDS);
        } else lastPong.set(System.currentTimeMillis());
    }

    /**
     * Checks if the pong messages are received
     * <p>
     *     The method that checks if the pong messages are received
     *     <br>
     *     If the pong messages are not received, it closes the connection
     *     <br>
     *     If the pong messages are received, it sends a ping message
     *     <br>
     * </p>
     */
    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long last = lastPong.get();
        if (last == - 1) return;
        if (now - last > PONG_TIMEOUT) {
            LOGGER.debug("CLIENT TCP: A pong timeout occurred: {} ms",
                         now - last);
            LOGGER.info("CLIENT TCP: Pong timeout, closing connection");
            try {
                socket.close();
            } catch (Exception e) {
                LOGGER.error("CLIENT TCP: Error while closing socket: {}", e.getMessage());
            } finally {
                pongExecutor.shutdown();
            }
        } else {
            out.println(pingMessage);
        }
    }

    public void close() {
        pongExecutor.shutdown();
    }
}

package it.polimi.ingsw.am11.network.Socket.Client;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PongHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PongHandler.class);
    private static final int PONG_INTERVAL = 1000;
    private static final int PONG_TIMEOUT = 3000;

    private final Socket socket;
    private final PrintWriter out;
    private final AtomicLong lastPong;
    private final ScheduledExecutorService pongExecutor;

    public PongHandler(@NotNull Socket socket,
                       @NotNull PrintWriter out) {
        this.socket = socket;
        this.out = out;
        this.lastPong = new AtomicLong(- 1);
        this.pongExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        lastPong.compareAndSet(- 1, System.currentTimeMillis());
        pongExecutor.scheduleAtFixedRate(this, PONG_INTERVAL, PONG_INTERVAL,
                                         TimeUnit.MILLISECONDS);
    }

    public void pong() {
        lastPong.set(System.currentTimeMillis());
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long last = lastPong.get();
        if (last == - 1) return;
        if (now - last > PONG_TIMEOUT) {
            LOGGER.info("CLIENT TCP: Pong timeout, closing connection");
            try {
                socket.close();
            } catch (Exception e) {
                LOGGER.error("CLIENT TCP: Error while closing socket: {}", e.getMessage());
            }
        } else {
            out.println("ping");
        }
    }

    public void close() {
        pongExecutor.shutdown();
    }
}

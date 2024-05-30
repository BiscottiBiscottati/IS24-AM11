package it.polimi.ingsw.am11.network.RMI.server.chat;

import it.polimi.ingsw.am11.network.RMI.remote.chat.ClientChatInterface;
import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public record ServerChatConnectorImpl(@NotNull ClientChatInterface chatInterface)
        implements ServerChatConnector {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ServerChatConnectorImpl.class.getName());
    private static ExecutorService executorService;

    public static void start() {
        executorService = Executors.newCachedThreadPool();
    }

    public static void stop() {
        executorService.shutdown();
    }

    @Override
    public void sendPublicMsg(@NotNull String sender, @NotNull String msg) {
        executorService.submit(() -> {
            try {
                LOGGER.info("SERVER RMI: Sending public message from {} to all clients", sender);
                chatInterface.receiveMsg(sender, msg);
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error sending public message: {}", e.getMessage());
            }
        });
    }

    @Override
    public void sendPrivateMsg(@NotNull String sender,
                               @NotNull String msg) {
        executorService.submit(() -> {
            try {
                LOGGER.info("SERVER RMI: Sending private message from {}", sender);
                chatInterface.receivePrivateMsg(sender, msg);
            } catch (RemoteException e) {
                LOGGER.error("SERVER RMI: Error sending private message: {}", e.getMessage());
            }
        });

    }
}

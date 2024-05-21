package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class TestServerToClient {
    static final Logger LOGGER = LoggerFactory.getLogger(TestServerToClient.class);
    SocketManager socketManager;
    Thread serverThread;

    @Mock
    ClientViewUpdater updater;

    @BeforeEach
    void setUp() {
        socketManager = new SocketManager(1234);
        serverThread = new Thread(socketManager::start);
        serverThread.start();
    }

    @Test
    void notifyGodPlayer() throws IOException, InterruptedException {
        ClientSocket clientSocket = new ClientSocket("localhost", 1234, updater);

        clientSocket.getConnector().setNickname("test");

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            LOGGER.debug("Notified god player");
            return null;
        }).when(updater).notifyGodPlayer();

        latch.await();

        CountDownLatch latch2 = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch2.countDown();
            LOGGER.debug("Notified num of players");
            return null;
        }).when(updater).updateNumOfPlayers(anyInt());

        clientSocket.getConnector().setNumOfPlayers(2);

        latch2.await();

        clientSocket.close();
    }

    @AfterEach
    void tearDown() {
        socketManager.stop();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package it.polimi.ingsw.am11.network.socket;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.socket.client.ClientSocket;
import it.polimi.ingsw.am11.network.socket.server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionThrower;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
public class TestServerToClient {
    static final Logger LOGGER = LoggerFactory.getLogger(TestServerToClient.class);
    SocketManager socketManager;
    Thread serverThread;

    ClientSocket clientSocket;

    @Mock
    ClientViewUpdater updater;
    @Mock
    ExceptionThrower exceptionThrower;

    @BeforeEach
    void setUp() throws IOException {
        when(updater.getExceptionThrower()).thenReturn(exceptionThrower);
        CentralController.INSTANCE.destroyGame();
        socketManager = new SocketManager(1234);
        serverThread = new Thread(socketManager::start);
        serverThread.start();
        clientSocket = new ClientSocket("localhost", 1234, updater);
    }

    @Test
    void notifyGodPlayer() throws InterruptedException {

        clientSocket.getGameConnector().setNickname("test");

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            LOGGER.debug("Notified god player");
            return 0;
        }).when(updater).notifyGodPlayer();

        latch.await();

        CountDownLatch latch2 = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch2.countDown();
            LOGGER.debug("Notified num of players");
            return 0;
        }).when(updater).updateNumOfPlayers(anyInt());

        clientSocket.getGameConnector().setNumOfPlayers(2);

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

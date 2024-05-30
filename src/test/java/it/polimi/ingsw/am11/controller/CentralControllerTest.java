package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.network.ServerChatConnector;
import it.polimi.ingsw.am11.network.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.ServerTableConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CentralControllerTest {

    CentralController centralController;

    @Mock
    ServerPlayerConnector serverPlayerConnector;

    @Mock
    ServerTableConnector serverTableConnector;

    @Mock
    ServerChatConnector chatConnector;

    @BeforeEach
    void setUp() {
        centralController = CentralController.INSTANCE;
        centralController.destroyGame();
    }

    @Test
    void connectPlayer() {
        assertDoesNotThrow(() -> centralController.connectPlayer("chen", serverPlayerConnector,
                                                                 serverTableConnector,
                                                                 chatConnector));
        assertThrows(NotSetNumOfPlayerException.class, () -> centralController.connectPlayer(
                "edo", serverPlayerConnector, serverTableConnector, chatConnector));

        assertDoesNotThrow(() -> centralController.setNumOfPlayers("chen", 3));

        assertDoesNotThrow(() -> centralController.connectPlayer("edo", serverPlayerConnector,
                                                                 serverTableConnector,
                                                                 chatConnector));

        assertDoesNotThrow(() -> centralController.connectPlayer("osama", serverPlayerConnector,
                                                                 serverTableConnector,
                                                                 chatConnector));
    }

    @Test
    void threadSafetyTest() {
        Set<String> nicknames = Set.of("edo", "osama", "ferdi");

        assertDoesNotThrow(() -> {
            centralController.connectPlayer("chen", serverPlayerConnector,
                                            serverTableConnector, chatConnector);
        });

        try {

            ExecutorService executor = Executors.newFixedThreadPool(8);

            CountDownLatch playerLatch = new CountDownLatch(nicknames.size());

            for (String nickname : nicknames) {
                executor.submit(() -> {
                    playerLatch.countDown();
                    assertThrows(NotSetNumOfPlayerException.class,
                                 () -> centralController.connectPlayer(
                                         nickname, serverPlayerConnector, serverTableConnector,
                                         chatConnector));
                });
            }
            playerLatch.await();

            CountDownLatch latch = new CountDownLatch(1);

            assertDoesNotThrow(() -> centralController.setNumOfPlayers("chen", 4));

            for (String nickname : nicknames) {
                executor.submit(() -> {
                    try {
                        latch.await();
                        assertDoesNotThrow(() -> centralController.connectPlayer(
                                nickname, serverPlayerConnector, serverTableConnector,
                                chatConnector));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            latch.countDown();
            executor.shutdown();
            if (! executor.awaitTermination(10, TimeUnit.SECONDS)) fail("Timeout");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
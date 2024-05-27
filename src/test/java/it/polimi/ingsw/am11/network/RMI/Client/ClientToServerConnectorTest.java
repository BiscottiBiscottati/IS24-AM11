package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.ClientGameConnector;
import it.polimi.ingsw.am11.network.RMI.Server.ServerRMI;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientToServerConnectorTest {

    ServerRMI serverRMI;

    ExecutorService executor;

    ClientRMI clientRMI;
    ClientRMI clientRMI2;
    ClientRMI clientRMI3;
    ClientRMI clientRMI4;

    ClientGameConnector gameConnector;
    ClientGameConnector gameConnector2;
    ClientGameConnector gameConnector3;
    ClientGameConnector gameConnector4;

    @Mock
    ClientViewUpdater updater;
    @Mock
    ClientViewUpdater updater2;
    @Mock
    ClientViewUpdater updater3;
    @Mock
    ClientViewUpdater updater4;
    @Mock
    ExceptionConnector exceptionConnector;

    @BeforeEach
    void setUp() throws RemoteException {
        when(updater.getExceptionConnector()).thenReturn(exceptionConnector);
        when(updater2.getExceptionConnector()).thenReturn(exceptionConnector);
        when(updater3.getExceptionConnector()).thenReturn(exceptionConnector);
        when(updater4.getExceptionConnector()).thenReturn(exceptionConnector);

        executor = Executors.newFixedThreadPool(5);

        CentralController.INSTANCE.forceReset();
        serverRMI = new ServerRMI(54321);
        executor.submit(serverRMI::start);

        clientRMI = new ClientRMI("localhost", 54321, updater);
        clientRMI2 = new ClientRMI("localhost", 54321, updater2);
        clientRMI3 = new ClientRMI("localhost", 54321, updater3);
        clientRMI4 = new ClientRMI("localhost", 54321, updater4);

        gameConnector = clientRMI.getGameUpdatesInterface();
        gameConnector2 = clientRMI2.getGameUpdatesInterface();
        gameConnector3 = clientRMI3.getGameUpdatesInterface();
        gameConnector4 = clientRMI4.getGameUpdatesInterface();
    }

    @RepeatedTest(10)
    @Timeout(3)
    void notifyGodPlayer() throws InterruptedException {
        CountDownLatch godNotified = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(1);

        doAnswer(invocation -> {
            godNotified.countDown();
            return 0;
        }).when(updater).notifyGodPlayer();
        doAnswer(invocation -> {
            finishLatch.countDown();
            return 0;
        }).when(updater).updateNumOfPlayers(3);

        assertDoesNotThrow(() -> gameConnector.setNickname("chen"));

        godNotified.await();

        assertDoesNotThrow(() -> gameConnector.setNumOfPlayers(3));

        finishLatch.await();

        // verify notify god player is called and number set
        verify(updater, Mockito.times(1)).notifyGodPlayer();
        verify(updater, Mockito.times(1)).updateNumOfPlayers(3);
    }

    @RepeatedTest(10)
    @Timeout(3)
    void setNumOfPlayers() throws InterruptedException {
        CountDownLatch finishLatch = new CountDownLatch(3);
        doAnswer(invocation -> {
            assertDoesNotThrow(() -> gameConnector.setNumOfPlayers(3));
            return 0;
        }).when(updater).notifyGodPlayer();

        doAnswer(invocation -> {
            assertDoesNotThrow(() -> gameConnector2.setNickname("edo"));
            assertDoesNotThrow(() -> gameConnector3.setNickname("ferdi"));
            return 0;
        }).when(updater).updateNumOfPlayers(3);

        doAnswer(invocation -> {
            finishLatch.countDown();
            return 0;
        }).when(updater).receiveStarterCard(anyInt());
        doAnswer(invocation -> {
            finishLatch.countDown();
            return 0;
        }).when(updater2).receiveStarterCard(anyInt());
        doAnswer(invocation -> {
            finishLatch.countDown();
            return 0;
        }).when(updater3).updateGameStatus(eq(GameStatus.CHOOSING_STARTERS));

        assertDoesNotThrow(() -> gameConnector.setNickname("chen"));

        finishLatch.await();

        verify(updater, times(1)).notifyGodPlayer();
        verify(updater, times(1)).updateNumOfPlayers(3);
        verify(updater, times(1)).receiveStarterCard(anyInt());
        verify(updater2, times(1)).receiveStarterCard(anyInt());
        verify(updater3, times(1)).receiveStarterCard(anyInt());

    }

    @RepeatedTest(5)
    @Timeout(5)
    void setStarters() throws InterruptedException {
        CountDownLatch latchLogin = new CountDownLatch(1);
        CountDownLatch latchFinish = new CountDownLatch(3);
        CountDownLatch numSet = new CountDownLatch(1);

        // Set Starter when choosing starters
        doAnswer(invocation -> {
            executor.submit(
                    () -> {
                        try {
                            latchLogin.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        assertDoesNotThrow(() -> gameConnector.setStarterCard(
                                true));
                        latchFinish.countDown();
                    });
            return 0;
        }).when(updater).updateGameStatus(eq(GameStatus.CHOOSING_STARTERS));

        doAnswer(invocation -> {
            executor.submit(() -> {
                try {
                    latchLogin.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                assertDoesNotThrow(() -> gameConnector2.setStarterCard(true));
                latchFinish.countDown();
            });
            return 0;
        }).when(updater2).updateGameStatus(eq(GameStatus.CHOOSING_STARTERS));

        // wait for CandidateObjective to be given
        doAnswer(invocation -> {
            latchFinish.countDown();
            return 0;
        }).when(updater).receiveCandidateObjective(anySet());
        doAnswer(invocation -> {
            latchFinish.countDown();
            return 0;
        }).when(updater2).receiveCandidateObjective(anySet());

        doAnswer(invocation -> {
            assertDoesNotThrow(() -> gameConnector.setNumOfPlayers(2));
            return 0;
        }).when(updater).notifyGodPlayer();

        doAnswer(invocation -> {
            numSet.countDown();
            return 0;
        }).when(updater).updateNumOfPlayers(2);

        // Connect god player
        assertDoesNotThrow(() -> gameConnector.setNickname("chen"));


        // Test setting starter when the game is in setup
        assertDoesNotThrow(() -> gameConnector.setStarterCard(true));

        numSet.await();

        // Connect second player
        executor.submit(() -> {
            assertDoesNotThrow(() -> gameConnector2.setNickname("edo"));
            latchLogin.countDown();
        });

        latchFinish.await();

        // Test that exception is thrown when setting starter when not in choosing starters
        verify(exceptionConnector, times(1))
                .throwException(any(GameStatusException.class));

        // Test that Starter are given
        verify(updater, times(1)).receiveStarterCard(anyInt());
        verify(updater2, times(1)).receiveStarterCard(anyInt());

        // Test that objective cards are given
        verify(updater, times(1)).receiveCandidateObjective(anySet());
        verify(updater2, times(1)).receiveCandidateObjective(anySet());

    }

    @AfterEach
    void tearDown() throws InterruptedException {
        executor.shutdown();
        clientRMI.close();
        clientRMI2.close();
        clientRMI3.close();
        clientRMI4.close();
        serverRMI.close();
    }
}
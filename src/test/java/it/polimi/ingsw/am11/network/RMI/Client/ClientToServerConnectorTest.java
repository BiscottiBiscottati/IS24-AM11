package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.table.GameStatus;
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
    }

    @RepeatedTest(10)
    void notifyGodPlayer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        doAnswer(invocation -> {
            latch.countDown();
            return 0;
        }).when(exceptionConnector).throwException(any(NotGodPlayerException.class));
        doAnswer(invocation -> {
            latch.countDown();
            return 0;
        }).when(exceptionConnector).throwException(any(NotSetNumOfPlayerException.class));
        doAnswer(invocation -> {
            latch.countDown();
            return 0;
        }).when(updater).notifyGodPlayer();

        assertDoesNotThrow(() -> clientRMI.login("chen"));

        clientRMI.await();

        assertDoesNotThrow(() -> clientRMI2.login("edo"));
        clientRMI2.await();

        assertDoesNotThrow(() -> clientRMI3.setNumOfPlayers("edo", 2));
        clientRMI3.await();

        assertDoesNotThrow(() -> clientRMI.setNumOfPlayers("chen", 2));
        clientRMI.await();


        latch.await();

        // verify not god player trying to set num of player
        verify(exceptionConnector, times(1))
                .throwException(any(NotGodPlayerException.class));
        // verify player trying to enter game without num of players
        verify(exceptionConnector, times(1))
                .throwException(any(NotSetNumOfPlayerException.class));
        // verify notify god player is called
        verify(updater, Mockito.times(1)).notifyGodPlayer();
    }

    @RepeatedTest(10)
    void setNumOfPlayers() {
        assertDoesNotThrow(() -> clientRMI.login("chen"));
        clientRMI.await();

        // Testing illegal number of players
        assertDoesNotThrow(() -> clientRMI.setNumOfPlayers("chen", 1));
        clientRMI.await();
        verify(exceptionConnector, times(1))
                .throwException(any(NumOfPlayersException.class));

        reset(exceptionConnector);

        assertDoesNotThrow(() -> clientRMI.setNumOfPlayers("chen", 5));
        clientRMI.await();
        verify(exceptionConnector, times(1))
                .throwException(any(NumOfPlayersException.class));

        // Testing not god player
        assertDoesNotThrow(() -> clientRMI2.setNumOfPlayers("edo", 3));
        clientRMI2.await();

        // Testing correct number of players
        assertDoesNotThrow(() -> clientRMI.setNumOfPlayers("chen", 2));
        clientRMI.await();

        // Testing already set number of players
        assertDoesNotThrow(() -> clientRMI2.setNumOfPlayers("chen", 3));
        clientRMI2.await();

        verify(exceptionConnector, times(1))
                .throwException(any(NotGodPlayerException.class));
        verify(updater, times(1)).updateNumOfPlayers(2);
        verify(exceptionConnector, times(1))
                .throwException(any(GameStatusException.class));

    }

    @RepeatedTest(5)
    @Timeout(5)
    void setStarters() throws InterruptedException {
        CountDownLatch latchLogin = new CountDownLatch(1);
        CountDownLatch latchFinish = new CountDownLatch(3);

        // Set Starter when choosing starters
        doAnswer(invocation -> {
            executor.submit(
                    () -> {
                        try {
                            latchLogin.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        assertDoesNotThrow(() -> clientRMI.setStarterCard("chen",
                                                                          true));
                        clientRMI.await();
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
                assertDoesNotThrow(() -> clientRMI2.setStarterCard("edo", true));
                clientRMI2.await();
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

        // Connect god player
        assertDoesNotThrow(() -> clientRMI.login("chen"));
        assertDoesNotThrow(() -> clientRMI.setNumOfPlayers("chen", 2));
        clientRMI.await();


        // Test setting starter when the game is in setup
        assertDoesNotThrow(() -> clientRMI.setStarterCard("chen", true));
        clientRMI.await();
        verify(exceptionConnector, times(1))
                .throwException(any(GameStatusException.class));

        // Connect second player
        executor.submit(() -> {
            assertDoesNotThrow(() -> clientRMI2.login("edo"));
            clientRMI2.await();
            latchLogin.countDown();
        });

        latchFinish.await();

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
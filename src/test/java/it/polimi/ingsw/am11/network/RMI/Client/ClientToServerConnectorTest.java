package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.RMI.Server.ServerMain;
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

    ServerMain serverMain;

    ExecutorService executor;

    ClientMain clientMain;
    ClientMain clientMain2;
    ClientMain clientMain3;
    ClientMain clientMain4;

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
        serverMain = new ServerMain(54321);
        executor.submit(serverMain::start);

        clientMain = new ClientMain("localhost", 54321, updater);
        clientMain2 = new ClientMain("localhost", 54321, updater2);
        clientMain3 = new ClientMain("localhost", 54321, updater3);
        clientMain4 = new ClientMain("localhost", 54321, updater4);
    }

    @RepeatedTest(10)
    void notifyGodPlayer() throws InterruptedException {
        assertDoesNotThrow(() -> clientMain.login("chen"));

        clientMain.await();

        Mockito.verify(updater, Mockito.times(1)).notifyGodPlayer();

        assertDoesNotThrow(() -> clientMain2.login("edo"));
        clientMain2.await();
        verify(exceptionConnector, times(1))
                .throwException(any(NotSetNumOfPlayerException.class));

        assertDoesNotThrow(() -> clientMain3.setNumOfPlayers("edo", 2));
        clientMain3.await();
        verify(exceptionConnector, times(1))
                .throwException(any(NotGodPlayerException.class));

        assertDoesNotThrow(() -> clientMain.setNumOfPlayers("chen", 2));
        clientMain.await();
    }

    @RepeatedTest(10)
    void setNumOfPlayers() throws RemoteException {
        assertDoesNotThrow(() -> clientMain.login("chen"));
        clientMain.await();

        // Testing illegal number of players
        assertDoesNotThrow(() -> clientMain.setNumOfPlayers("chen", 1));
        clientMain.await();
        verify(exceptionConnector, times(1))
                .throwException(any(NumOfPlayersException.class));

        reset(exceptionConnector);

        assertDoesNotThrow(() -> clientMain.setNumOfPlayers("chen", 5));
        clientMain.await();
        verify(exceptionConnector, times(1))
                .throwException(any(NumOfPlayersException.class));

        // Testing not god player
        assertDoesNotThrow(() -> clientMain2.setNumOfPlayers("edo", 3));
        clientMain2.await();
        verify(exceptionConnector, times(1))
                .throwException(any(NotGodPlayerException.class));

        // Testing correct number of players
        assertDoesNotThrow(() -> clientMain.setNumOfPlayers("chen", 2));
        clientMain.await();
        verify(updater, times(1)).updateNumOfPlayers(2);

        // Testing already set number of players
        assertDoesNotThrow(() -> clientMain2.setNumOfPlayers("chen", 3));
        clientMain2.await();
        verify(exceptionConnector, times(1))
                .throwException(any(GameStatusException.class));

    }

    @RepeatedTest(5)
    @Timeout(5)
    void setStarters() throws InterruptedException {
        CountDownLatch latchLogin = new CountDownLatch(1);
        CountDownLatch latchFinish = new CountDownLatch(2);

        // Set Starter when choosing starters
        doAnswer(invocation -> {
            executor.submit(
                    () -> {
                        try {
                            latchLogin.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        assertDoesNotThrow(() -> clientMain.setStarterCard("chen",
                                                                           true));
                        clientMain.await();
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
                assertDoesNotThrow(() -> clientMain2.setStarterCard("edo", true));
                clientMain2.await();
                latchFinish.countDown();
            });
            return 0;
        }).when(updater2).updateGameStatus(eq(GameStatus.CHOOSING_STARTERS));

        // Connect god player
        assertDoesNotThrow(() -> clientMain.login("chen"));
        assertDoesNotThrow(() -> clientMain.setNumOfPlayers("chen", 2));
        clientMain.await();


        // Test setting starter when the game is in setup
        assertDoesNotThrow(() -> clientMain.setStarterCard("chen", true));
        clientMain.await();
        verify(exceptionConnector, times(1))
                .throwException(any(GameStatusException.class));

        // Connect second player
        executor.submit(() -> {
            assertDoesNotThrow(() -> clientMain2.login("edo"));
            clientMain2.await();
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
    void tearDown() {
        executor.shutdown();
        clientMain.close();
        clientMain2.close();
        clientMain3.close();
        clientMain4.close();
        serverMain.close();
    }
}
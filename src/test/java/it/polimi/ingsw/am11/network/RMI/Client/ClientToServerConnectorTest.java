package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.model.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.network.RMI.Server.ServerMain;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientToServerConnectorTest {

    ServerMain serverMain;
    Thread serverThread;

    ClientMain clientMain;
    ClientMain clientMain2;
    ClientMain clientMain3;
    ClientMain clientMain4;

    @Mock
    ClientViewUpdater updater;
    @Mock
    ExceptionConnector exceptionConnector;

    @BeforeEach
    void setUp() throws RemoteException {
        when(updater.getExceptionConnector()).thenReturn(exceptionConnector);

        CentralController.INSTANCE.forceReset();
        serverMain = new ServerMain(54321);
        serverThread = new Thread(serverMain::start);
        serverThread.start();
        clientMain = new ClientMain("localhost", 54321, updater);
        clientMain2 = new ClientMain("localhost", 54321, updater);
        clientMain3 = new ClientMain("localhost", 54321, updater);
        clientMain4 = new ClientMain("localhost", 54321, updater);
    }

    @Test
    void notifyGodPlayer() throws RemoteException {
        clientMain.login("chen");

        Mockito.verify(updater, Mockito.times(1)).notifyGodPlayer();

        assertDoesNotThrow(() -> clientMain2.login("edo"));
        verify(exceptionConnector, times(1))
                .throwException(any(NotSetNumOfPlayerException.class));

        assertDoesNotThrow(() -> clientMain3.setNumOfPlayers("edo", 2));
        verify(exceptionConnector, times(1))
                .throwException(any(NotGodPlayerException.class));

        clientMain.setNumOfPlayers("chen", 2);
    }

    @AfterEach
    void tearDown() {
        clientMain.close();
        clientMain2.close();
        clientMain3.close();
        clientMain4.close();
        serverThread.interrupt();
        serverMain.close();
    }
}
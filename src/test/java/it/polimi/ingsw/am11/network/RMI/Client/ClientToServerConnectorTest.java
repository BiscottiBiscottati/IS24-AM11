package it.polimi.ingsw.am11.network.RMI.Client;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.RMI.Server.ServerMain;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@ExtendWith(MockitoExtension.class)
class ClientToServerConnectorTest {

    ServerMain serverMain;
    ClientMain clientMain;
    Thread serverThread;

    @Mock
    ClientViewUpdater updater;

    @BeforeEach
    void setUp() {
        CentralController.INSTANCE.forceReset();
        serverMain = new ServerMain(54321);
        serverThread = new Thread(serverMain::start);
        serverThread.start();
    }

    @Test
    void notifyGodPlayer() throws RemoteException, NotBoundException {
        clientMain = new ClientMain("localhost", 54321, updater);
        clientMain.login("test");

        Mockito.verify(updater, Mockito.times(1)).notifyGodPlayer();
    }

    @AfterEach
    void tearDown() {
        serverThread.interrupt();
        serverMain.close();
    }
}
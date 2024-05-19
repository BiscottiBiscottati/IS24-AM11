package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.RMI.Client.ClientMain;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.network.RMI.Server.ServerMain;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

class ClientMainTest {

    @BeforeEach
    void setUp() {
        CentralController.INSTANCE.forceReset();
    }

    @Test
    void main() {
    }

    @Test
    void testLogin()
    throws RemoteException, NotBoundException {
        Loggable stub1 = Mockito.mock(Loggable.class);
        PlayerViewInterface stub3 = Mockito.mock(PlayerViewInterface.class);
        Registry registry = Mockito.mock(Registry.class);
        Mockito.when(registry.lookup(any())).thenReturn(stub1);
        Mockito.when(registry.lookup(any())).thenReturn(stub3);

        ServerMain serverMain = new ServerMain(1234);
        ClientMain clientMain = new ClientMain("localhost", 1234);
        serverMain.start();
        ClientViewUpdater updater = Mockito.mock(ClientViewUpdater.class);

        clientMain.login("nick", updater);
        clientMain.setNumOfPlayers("nick", 2);

        ClientViewUpdater updater1 = Mockito.mock(ClientViewUpdater.class);
        ClientMain clientMain1 = new ClientMain("localhost", 1234);

        clientMain1.login("nick1", updater1);

        assertEquals("nick", CentralController.INSTANCE.getGodPlayer());
        assertNotEquals("nick1", CentralController.INSTANCE.getGodPlayer());

        clientMain.setStarterCard("nick", true);
        clientMain1.setStarterCard("nick1", true);

        clientMain.logout("nick");
        clientMain.login("nick", updater);
        assertEquals("nick", CentralController.INSTANCE.getGodPlayer());


    }
}
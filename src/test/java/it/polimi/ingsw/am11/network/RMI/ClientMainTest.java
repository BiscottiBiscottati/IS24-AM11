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
        serverMain.start();

        ClientViewUpdater updater = Mockito.mock(ClientViewUpdater.class);
        ClientMain clientMain = new ClientMain("localhost", 1234, updater);
        clientMain.login("nick");
        clientMain.setNumOfPlayers("nick", 5);
        clientMain.setNumOfPlayers("nick", 4);

        ClientViewUpdater updater2 = Mockito.mock(ClientViewUpdater.class);
        ClientMain clientMain2 = new ClientMain("localhost", 1234, updater2);
        clientMain2.login("nick");
        clientMain2.login("nicks");


    }
}
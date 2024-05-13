package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.RMI.Client.ClientMain;
import it.polimi.ingsw.am11.network.RMI.Client.ClientToServerConnector;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.PlayerViewInterface;
import it.polimi.ingsw.am11.network.RMI.Server.ServerMain;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class ClientMainTest {

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

        ServerMain serverMain = new ServerMain();
        ClientMain clientMain = new ClientMain();
        ServerMain.run();
        ClientViewUpdater updater = Mockito.mock(ClientViewUpdater.class);

        ConnectorInterface connector;
        ClientToServerConnector clientObject = new ClientToServerConnector(updater);
        connector = (ConnectorInterface) UnicastRemoteObject.exportObject(clientObject, 0);

        clientMain.login("nick", updater);
        clientMain.setNumOfPlayers("nick", 2);

        ClientViewUpdater updater1 = Mockito.mock(ClientViewUpdater.class);
        ClientMain clientMain1 = new ClientMain();

        ConnectorInterface connector1;
        ClientToServerConnector clientObject1 = new ClientToServerConnector(updater);
        connector1 = (ConnectorInterface) UnicastRemoteObject.exportObject(clientObject1, 0);

        clientMain1.login("nick1", updater1);

        assertEquals("nick", CentralController.INSTANCE.getGodPlayer());
        assertThrows(ServerException.class, () -> serverMain.setNumOfPlayers("nick1", 2));

    }
}
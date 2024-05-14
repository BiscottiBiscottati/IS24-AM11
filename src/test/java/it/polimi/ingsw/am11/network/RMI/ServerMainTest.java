package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.Server.ServerMain;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

class ServerMainTest {

    private ServerMain serverMain;
    private Loggable loggableMock;
    private ConnectorInterface connectorMock;

    @Test
    void main() {
    }

    @Test
    public void testLogin() throws RemoteException {
    }
}
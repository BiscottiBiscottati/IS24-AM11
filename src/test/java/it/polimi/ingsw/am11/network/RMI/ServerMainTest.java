package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ConnectorInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.Loggable;
import it.polimi.ingsw.am11.network.RMI.Server.ServerMain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.rmi.RemoteException;

class ServerMainTest {

    private ServerMain serverMain;
    private Loggable loggableMock;
    private ConnectorInterface connectorMock;

    @Test
    void main() {
    }

    @BeforeEach
    public void setup() {
        loggableMock = Mockito.mock(Loggable.class);
        connectorMock = Mockito.mock(ConnectorInterface.class);
    }

    @Test
    public void testLogin() throws RemoteException {
    }
}
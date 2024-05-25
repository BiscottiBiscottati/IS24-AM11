package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.RemoteInterfaces.ServerLoggable;
import it.polimi.ingsw.am11.network.RMI.Server.ServerRMI;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

class ServerRMITest {

    private ServerRMI serverRMI;
    private ServerLoggable serverLoggableMock;
    private ClientGameUpdatesInterface connectorMock;

    @Test
    void main() {
    }

    @Test
    public void testLogin() throws RemoteException {
    }
}
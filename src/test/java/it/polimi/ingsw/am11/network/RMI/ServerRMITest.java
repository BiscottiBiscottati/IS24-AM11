package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.ClientGameUpdatesInterface;
import it.polimi.ingsw.am11.network.RMI.remoteInterfaces.ServerLoggable;
import it.polimi.ingsw.am11.network.RMI.server.ServerRMI;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

@Disabled
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
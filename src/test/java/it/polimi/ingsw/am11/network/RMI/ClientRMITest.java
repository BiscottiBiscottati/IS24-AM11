package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.controller.CentralController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@Disabled
class ClientRMITest {

    @BeforeEach
    void setUp() {
        CentralController.INSTANCE.destroyGame();
    }

    @Test
    void main() {
    }

    @Test
    void testLogin() throws RemoteException, NotBoundException {
    }
}
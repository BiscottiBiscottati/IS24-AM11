package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.Executors;

class TestCommunication {

    ClientViewUpdater updater;

    @Test
    public void testClientServerCommunication() throws IOException {

        // Create a SocketManager instance
        SocketManager server = new SocketManager(12345);

        // Start the server in a separate thread
        Executors.newSingleThreadExecutor().execute(server::start);
        //clientSocket.con
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create a ClientSocket instance

    }
}
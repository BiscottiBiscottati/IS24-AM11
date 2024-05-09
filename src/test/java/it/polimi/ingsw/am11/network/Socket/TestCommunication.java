package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create a ClientSocket instance
        ClientSocket client = new ClientSocket("localhost", 12345, "ferdi",
                                               updater);
        ClientSocket client2 = new ClientSocket("localhost", 12345, "edo",
                                                updater);
        ClientSocket client3 = new ClientSocket("localhost", 12345, "chen",
                                                updater);

        client.connect();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client2.connect();
        client3.connect();

    }
}
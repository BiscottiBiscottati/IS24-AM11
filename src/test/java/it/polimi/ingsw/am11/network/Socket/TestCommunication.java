package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientPlayerView;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.Executors;

class TestCommunication {

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
        ClientSocket client = new ClientSocket("localhost", 12345, new ClientPlayerView("Ferdi")
        );
        ClientSocket client2 = new ClientSocket("localhost", 12345, new ClientPlayerView("Edo");
        ClientSocket client3 = new ClientSocket("localhost", 12345, new ClientPlayerView("Chen")
        );

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
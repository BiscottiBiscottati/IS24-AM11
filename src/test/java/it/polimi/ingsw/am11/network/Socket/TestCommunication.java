package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

class TestCommunication {

    @Test
    public void testClientServerCommunication() {

        // Create a SocketManager instance
        SocketManager server = new SocketManager(12345);

        // Start the server in a separate thread
        Executors.newSingleThreadExecutor().execute(server::start);

        // Create a ClientSocket instance
        ClientSocket client = new ClientSocket("localhost", 12345, "Ferdi");

        // Send a message to the server
        client.startCommunication();

        // Wait for the server to receive the message
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
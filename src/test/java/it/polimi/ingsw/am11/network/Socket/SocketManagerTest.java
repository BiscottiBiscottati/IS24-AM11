package it.polimi.ingsw.am11.network.Socket;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

class SocketManagerTest {

    @Test
    public void testClientSocket() {
        // Create a SocketManager instance
        SocketManager server = new SocketManager(12345);

        // Start the server in a separate thread
        Executors.newSingleThreadExecutor().execute(server::start);

        // Create a ClientSocket instance
        ClientSocket client = new ClientSocket("localhost", 12345, "Client");

        // Send a message to the server
        client.sendString("Hello, server!");

        // Wait for the server to receive the message
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(client.receiveString());
    }

    @Test
    public void testStartAndStopServer() {
        // Create a SocketManager instance
        SocketManager server = new SocketManager(12345);

        // Start the server in a separate thread
        Executors.newSingleThreadExecutor().execute(server::start);

        // Wait for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the server
        server.stop();

    }

}
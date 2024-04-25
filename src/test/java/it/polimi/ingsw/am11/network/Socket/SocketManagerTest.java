package it.polimi.ingsw.am11.network.Socket;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SocketManagerTest {

    @Test
    public void testClientServerConnection() {
        // Start the server in a separate thread
        Executors.newSingleThreadExecutor().execute(() -> {
            SocketManager server = new SocketManager(12345);
            server.start();
        });
        // Wait for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Try to connect to the server
        ClientSocket client = new ClientSocket("localhost", 12345);
        assertNotNull(client);
        // Send a message to the server
        String message = "Hello";
        client.send(message);
        // Receive the response from the server
        String response = client.receive();
        // Verify the response
        assertEquals(message.toUpperCase(), response);
        System.out.println(response);
    }

    @Test
    public void testMultipleClientServerConnection() {
        // Start the server in a separate thread
        Executors.newSingleThreadExecutor().execute(() -> {
            SocketManager server = new SocketManager(12345);
            server.start();
        });
        // Wait for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Messages to be sent by the clients
        String[] messages = {"Hello", "World", "Test"};

        for (String message : messages) {
            // Try to connect to the server
            ClientSocket client = new ClientSocket("localhost", 12345);
            assertNotNull(client);
            // Send a message to the server
            client.send(message);
            // Receive the response from the server
            String response = client.receive();
            // Verify the response
            assertEquals(message.toUpperCase(), response);
            System.out.println(response);
        }
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
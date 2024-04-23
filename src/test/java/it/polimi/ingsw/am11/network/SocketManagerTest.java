package it.polimi.ingsw.am11.network;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SocketManagerTest {

    @Test
    public void testConnection() {
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
        client.send("Hello");

    }
}
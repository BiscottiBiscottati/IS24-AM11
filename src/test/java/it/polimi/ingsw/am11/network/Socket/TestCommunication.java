package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Client.ReceiveCommand;
import it.polimi.ingsw.am11.network.Socket.Client.SendCommand;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.Executors;

class TestCommunication {

    private static SocketManager server;
    private static ClientSocket clientSocket;
    private static ClientSocket clientSocket2;

    @BeforeAll
    public static void setupServer() {
        server = new SocketManager(12345);
        Executors.newSingleThreadExecutor().execute(server::start);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetupPlayers() throws IOException {
        ClientViewUpdater clientViewUpdaterMock = Mockito.mock(ClientViewUpdater.class);
        ClientViewUpdater clientViewUpdaterMock2 = Mockito.mock(ClientViewUpdater.class);

        clientSocket = new ClientSocket("localhost", 12345, clientViewUpdaterMock);
        clientSocket2 = new ClientSocket("localhost", 12345, clientViewUpdaterMock2);
        // Create SendCommand and ReceiveCommand instances
        SendCommand sendCommand = new SendCommand(clientSocket.getOut());
        ReceiveCommand receiveCommand = new ReceiveCommand(clientViewUpdaterMock);
        SendCommand sendCommand2 = new SendCommand(clientSocket2.getOut());
        ReceiveCommand receiveCommand2 = new ReceiveCommand(clientViewUpdaterMock2);

        // Send a message to the server
        sendCommand.setNickname("Francesco");
        sendCommand.setNumOfPlayers(2);
        try {
            Thread.sleep(1000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommand2.setNickname("Giovanni");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.stop();
        clientSocket.close();
        clientSocket2.close();
    }
}

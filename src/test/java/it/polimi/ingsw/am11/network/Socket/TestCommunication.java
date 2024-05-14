package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Client.ReceiveCommand;
import it.polimi.ingsw.am11.network.Socket.Client.SendCommand;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.Executors;

class TestCommunication {

    @Test
    public void testClientServerCommunication() throws IOException, InterruptedException {
        SocketManager server = new SocketManager(12345);
        Executors.newSingleThreadExecutor().execute(server::start);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClientViewUpdater clientViewUpdaterMock = Mockito.mock(ClientViewUpdater.class);
        ClientViewUpdater clientViewUpdaterMock2 = Mockito.mock(ClientViewUpdater.class);

        ClientSocket clientSocket = new ClientSocket("localhost", 12345, clientViewUpdaterMock);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create SendCommand and ReceiveCommand instances
        SendCommand sendCommand = new SendCommand(clientSocket.getOut());
        ReceiveCommand receiveCommand = new ReceiveCommand(clientViewUpdaterMock);

        // Send a message to the server
        sendCommand.setNickname("Francesco");
        sendCommand.setNumOfPlayers(2);


    }
}
package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.network.CltToNetConnector;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.concurrent.Executors;

class TestCommunication {

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

        // Creare un mock di ClientViewUpdater
        ClientViewUpdater clientViewUpdaterMock = Mockito.mock(ClientViewUpdater.class);
        ClientViewUpdater clientViewUpdaterMock2 = Mockito.mock(ClientViewUpdater.class);

        // Utilizzare il mock per instanziare un ClientSocket
        ClientSocket clientSocket = new ClientSocket("localhost", 12345, clientViewUpdaterMock);
        ClientSocket clientSocket2 = new ClientSocket("localhost", 12345, clientViewUpdaterMock2);

        CltToNetConnector connector = clientSocket.getConnector();
        connector.setNumOfPlayers(2);


    }
}
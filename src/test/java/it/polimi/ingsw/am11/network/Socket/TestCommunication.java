package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Client.ReceiveCommandC;
import it.polimi.ingsw.am11.network.Socket.Client.SendCommandC;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
    public void testSetupPlayers() throws IOException, NumOfPlayersException, GameStatusException {
        ClientViewUpdater clientViewUpdaterMock = Mockito.mock(ClientViewUpdater.class);
        ClientViewUpdater clientViewUpdaterMock2 = Mockito.mock(ClientViewUpdater.class);

        clientSocket = new ClientSocket("localhost", 12345, clientViewUpdaterMock);
        clientSocket2 = new ClientSocket("localhost", 12345, clientViewUpdaterMock2);
        // Create SendCommand and ReceiveCommand instances
        SendCommandC sendCommandC = new SendCommandC(clientSocket.getOut());
        ReceiveCommandC receiveCommandC = new ReceiveCommandC(clientViewUpdaterMock);
        SendCommandC sendCommandC2 = new SendCommandC(clientSocket2.getOut());
        ReceiveCommandC receiveCommandC2 = new ReceiveCommandC(clientViewUpdaterMock2);
        // Send a message to the server
        sendCommandC.setNickname("Francesco");
        sendCommandC.setNumOfPlayers(2);
        try {
            Thread.sleep(5000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommandC2.setNickname("Giovanni");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendCommandC.setStarterCard(true);
        sendCommandC2.setStarterCard(false);
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(0)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock, Mockito.times(2)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(2)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        //sendCommandC.setPersonalObjective(1);
        //sendCommandC2.setPersonalObjective(2);

        server.stop();
        clientSocket.close();
        clientSocket2.close();

    }
}

package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Client.ReceiveCommandC;
import it.polimi.ingsw.am11.network.Socket.Client.SendCommandC;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestCommunication {

    //TODO

    static CentralController centralController;
    static ClientSocket clientSocket;
    static ClientSocket clientSocket2;
    static ClientSocket clientSocket3;
    static ClientSocket clientSocket4;
    static CountDownLatch latch;
    @Mock
    ClientViewUpdater clientViewUpdaterMock;
    @Mock
    ClientViewUpdater clientViewUpdaterMock2;
    @Mock
    ClientViewUpdater clientViewUpdaterMock3;
    @Mock
    ClientViewUpdater clientViewUpdaterMock4;
    @Mock
    ExceptionConnector exceptionConnectorMock;
    Thread server;
    SocketManager serverSocket;

    @BeforeAll
    public static void setupServer() {
        centralController = CentralController.INSTANCE;
    }

    // FIXME there are side effects when doing the tests together
    @BeforeEach
    void setUp() throws IOException {
        when(clientViewUpdaterMock.getExceptionConnector()).thenReturn(exceptionConnectorMock);
        when(clientViewUpdaterMock2.getExceptionConnector()).thenReturn(exceptionConnectorMock);
        when(clientViewUpdaterMock3.getExceptionConnector()).thenReturn(exceptionConnectorMock);
        when(clientViewUpdaterMock4.getExceptionConnector()).thenReturn(exceptionConnectorMock);

        centralController.forceReset();
        serverSocket = new SocketManager(12345);
        server = new Thread(() -> {
            serverSocket.start();
        });
        server.start();
        clientSocket = new ClientSocket("localhost", 12345, clientViewUpdaterMock);
        clientSocket2 = new ClientSocket("localhost", 12345, clientViewUpdaterMock2);
        clientSocket3 = new ClientSocket("localhost", 12345, clientViewUpdaterMock3);
        clientSocket4 = new ClientSocket("localhost", 12345, clientViewUpdaterMock4);

    }

    // FIXME test doesn't finish
    @Test
    public void testSetupPlayersWithWait() {

        // Create SendCommand and ReceiveCommand instances

        SendCommandC sendCommandC = new SendCommandC(clientSocket.getOut());
        ReceiveCommandC receiveCommandC = new ReceiveCommandC(clientViewUpdaterMock);
        SendCommandC sendCommandC2 = new SendCommandC(clientSocket2.getOut());
        ReceiveCommandC receiveCommandC2 = new ReceiveCommandC(clientViewUpdaterMock2);
        SendCommandC sendCommandC3 = new SendCommandC(clientSocket3.getOut());
        ReceiveCommandC receiveCommandC3 = new ReceiveCommandC(clientViewUpdaterMock3);
        SendCommandC sendCommandC4 = new SendCommandC(clientSocket4.getOut());
        ReceiveCommandC receiveCommandC4 = new ReceiveCommandC(clientViewUpdaterMock4);
        // Send a message to the server
        sendCommandC.setNickname("Francesco");

        try {
            Thread.sleep(1000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendCommandC.setNumOfPlayers(4);
        try {
            Thread.sleep(1000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommandC2.setNickname("Giovanni");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommandC3.setNickname("Giuseppe");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommandC4.setNickname("Giacomo");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendCommandC.setStarterCard(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommandC2.setStarterCard(false);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommandC3.setStarterCard(false);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendCommandC4.setStarterCard(false);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(0)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(0)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(0)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());


    }

    @Test
    public void testSetupPlayersWithoutWait() throws IOException {
        // Create SendCommand and ReceiveCommand instances
        SendCommandC sendCommandC = new SendCommandC(clientSocket.getOut());
        ReceiveCommandC receiveCommandC = new ReceiveCommandC(clientViewUpdaterMock);
        SendCommandC sendCommandC2 = new SendCommandC(clientSocket2.getOut());
        ReceiveCommandC receiveCommandC2 = new ReceiveCommandC(clientViewUpdaterMock2);
        SendCommandC sendCommandC3 = new SendCommandC(clientSocket3.getOut());
        ReceiveCommandC receiveCommandC3 = new ReceiveCommandC(clientViewUpdaterMock3);
        SendCommandC sendCommandC4 = new SendCommandC(clientSocket4.getOut());
        ReceiveCommandC receiveCommandC4 = new ReceiveCommandC(clientViewUpdaterMock4);

        // Setup Mockito chain invocation for sending num of player
        Mockito.doAnswer(invocation -> {
            sendCommandC.setNumOfPlayers(4);
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock).notifyGodPlayer();

        Mockito.doAnswer(invocation -> {
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock).updateNumOfPlayers(4);

        // Setup Mockito chain invocations for setting Starters and PersonalObjectives
        Mockito.doAnswer(invocation -> {
            sendCommandC.setStarterCard(true);
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock).receiveStarterCard(ArgumentMatchers.anyInt());

        Mockito.doAnswer(invocation -> {
            sendCommandC2.setStarterCard(false);
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock2).receiveStarterCard(ArgumentMatchers.anyInt());

        Mockito.doAnswer(invocation -> {
            sendCommandC3.setStarterCard(false);
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock3).receiveStarterCard(ArgumentMatchers.anyInt());

        Mockito.doAnswer(invocation -> {
            sendCommandC4.setStarterCard(false);
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock4).receiveStarterCard(ArgumentMatchers.anyInt());

        Mockito.doAnswer(invocation -> {
            Set<Integer> objsID = invocation.getArgument(0);
            sendCommandC.setPersonalObjective(objsID.stream().findAny().orElseThrow());
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock).receiveCandidateObjective(ArgumentMatchers.anySet());

        Mockito.doAnswer(invocation -> {
            Set<Integer> objsID = invocation.getArgument(0);
            sendCommandC2.setPersonalObjective(objsID.stream().findAny().orElseThrow());
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock2).receiveCandidateObjective(ArgumentMatchers.anySet());

        Mockito.doAnswer(invocation -> {
            Set<Integer> objsID = invocation.getArgument(0);
            sendCommandC3.setPersonalObjective(objsID.stream().findAny().orElseThrow());
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock3).receiveCandidateObjective(ArgumentMatchers.anySet());

        Mockito.doAnswer(invocation -> {
            Set<Integer> objsID = invocation.getArgument(0);
            sendCommandC4.setPersonalObjective(objsID.stream().findAny().orElseThrow());
            latch.countDown();
            return 0;
        }).when(clientViewUpdaterMock4).receiveCandidateObjective(ArgumentMatchers.anySet());

        latch = new CountDownLatch(2);

        // sending first player nickname
        sendCommandC.setNickname("Francesco");

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        latch = new CountDownLatch(8);

        // sending other players nickname
        sendCommandC2.setNickname("Giovanni");
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(0)).notifyGodPlayer();
        sendCommandC3.setNickname("Giuseppe");
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(0)).notifyGodPlayer();
        sendCommandC4.setNickname("Giacomo");
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(0)).notifyGodPlayer();

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verify the invocations from Server
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(1)).receiveStarterCard(
                ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
    }

    @AfterEach
    void tearDown() {

        clientSocket.close();
        clientSocket2.close();
        clientSocket3.close();
        clientSocket4.close();
        serverSocket.stop();
        server.interrupt();
        try {
            server.join();
        } catch (InterruptedException ignored) {
        }
        reset(clientViewUpdaterMock, clientViewUpdaterMock2, clientViewUpdaterMock3,
              clientViewUpdaterMock4, exceptionConnectorMock);
    }
}

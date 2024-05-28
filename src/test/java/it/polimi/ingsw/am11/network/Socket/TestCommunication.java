package it.polimi.ingsw.am11.network.Socket;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.Socket.Client.ClientMessageReceiver;
import it.polimi.ingsw.am11.network.Socket.Client.ClientMessageSender;
import it.polimi.ingsw.am11.network.Socket.Client.ClientSocket;
import it.polimi.ingsw.am11.network.Socket.Server.SocketManager;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.ExceptionConnector;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
class TestCommunication {

    //TODO

    static final Logger LOGGER = LoggerFactory.getLogger(TestCommunication.class);

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

        centralController.destroyGame();
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

    @Test
    void testSetupPlayersWithWait() {

        // Create SendCommand and ReceiveCommand instances

        ClientMessageSender clientMessageSender = new ClientMessageSender(clientSocket.getOut());
        ClientMessageSender clientMessageSender2 = new ClientMessageSender(clientSocket2.getOut());
        ClientMessageSender clientMessageSender3 = new ClientMessageSender(clientSocket3.getOut());
        ClientMessageSender clientMessageSender4 = new ClientMessageSender(clientSocket4.getOut());
        // Send a message to the server
        clientMessageSender.setNickname("Francesco");

        try {
            Thread.sleep(100); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientMessageSender.setNumOfPlayers(4);
        try {
            Thread.sleep(100); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientMessageSender2.setNickname("Giovanni");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientMessageSender3.setNickname("Giuseppe");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientMessageSender4.setNickname("Giacomo");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientMessageSender.setStarterCard(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientMessageSender2.setStarterCard(false);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientMessageSender3.setStarterCard(false);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientMessageSender4.setStarterCard(false);
        try {
            Thread.sleep(100);
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

    @RepeatedTest(10)
    @Timeout(value = 3)
    void testSetupPlayersWithoutWait() {
        // Create SendCommand and ReceiveCommand instances
        ClientMessageSender clientMessageSender = new ClientMessageSender(clientSocket.getOut());
        ClientMessageReceiver clientMessageReceiver = new ClientMessageReceiver(
                clientViewUpdaterMock);
        ClientMessageSender clientMessageSender2 = new ClientMessageSender(clientSocket2.getOut());
        ClientMessageReceiver clientMessageReceiver2 = new ClientMessageReceiver(
                clientViewUpdaterMock2);
        ClientMessageSender clientMessageSender3 = new ClientMessageSender(clientSocket3.getOut());
        ClientMessageReceiver clientMessageReceiver3 = new ClientMessageReceiver(
                clientViewUpdaterMock3);
        ClientMessageSender clientMessageSender4 = new ClientMessageSender(clientSocket4.getOut());
        ClientMessageReceiver clientMessageReceiver4 = new ClientMessageReceiver(
                clientViewUpdaterMock4);

        CountDownLatch latchForGod = new CountDownLatch(2);
        CountDownLatch latchForFinish = new CountDownLatch(8);

        // Setup Mockito chain invocation for sending num of player
        Mockito.doAnswer(invocation -> {
            clientMessageSender.setNumOfPlayers(4);
            LOGGER.info("God notified");
            latchForGod.countDown();
            return 0;
        }).when(clientViewUpdaterMock).notifyGodPlayer();

        Mockito.doAnswer(invocation -> {
            LOGGER.info("Num of player Set");
            latchForGod.countDown();
            return 0;
        }).when(clientViewUpdaterMock).updateNumOfPlayers(4);


        // Setup Mockito chain invocations for setting Starters
        Mockito.doAnswer(invocation -> {
                   clientMessageSender.setStarterCard(true);
                   LOGGER.info("Setting starter card for player 1");
                   latchForFinish.countDown();
                   return 0;
               }).when(clientViewUpdaterMock)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_STARTERS));

        Mockito.doAnswer(invocation -> {
                   clientMessageSender2.setStarterCard(false);
                   LOGGER.info("Setting starter card for player 2");
                   latchForFinish.countDown();
                   return 0;
               }).when(clientViewUpdaterMock2)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_STARTERS));

        Mockito.doAnswer(invocation -> {
                   clientMessageSender3.setStarterCard(false);
                   LOGGER.info("Setting starter card for player 3");
                   latchForFinish.countDown();
                   return 0;
               }).when(clientViewUpdaterMock3)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_STARTERS));

        Mockito.doAnswer(invocation -> {
                   clientMessageSender4.setStarterCard(false);
                   LOGGER.info("Setting starter card for player 4");
                   latchForFinish.countDown();
                   return 0;
               }).when(clientViewUpdaterMock4)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_STARTERS));

        AtomicInteger obj1 = new AtomicInteger();
        AtomicInteger obj2 = new AtomicInteger();
        AtomicInteger obj3 = new AtomicInteger();
        AtomicInteger obj4 = new AtomicInteger();

        // Mockito chain invocations for setting personal objectives
        Mockito.doAnswer(invocation -> {
                   Set<Integer> objsID = invocation.getArgument(0);
                   obj1.set(objsID.stream().findAny().orElseThrow());
                   LOGGER.info("Received candidate objectives for player 1");
                   return 0;
               }).when(clientViewUpdaterMock)
               .receiveCandidateObjective(ArgumentMatchers.anySet());

        Mockito.doAnswer(invocation -> {
            Set<Integer> objsID = invocation.getArgument(0);
            obj2.set(objsID.stream().findAny().orElseThrow());
            LOGGER.info("Received candidate objectives for player 2");
            return 0;
        }).when(clientViewUpdaterMock2).receiveCandidateObjective(ArgumentMatchers.anySet());

        Mockito.doAnswer(invocation -> {
                   Set<Integer> objsID = invocation.getArgument(0);
                   obj3.set(objsID.stream().findAny().orElseThrow());
                   LOGGER.info("Received candidate objectives for player 3");
                   return 0;
               }).when(clientViewUpdaterMock3)
               .receiveCandidateObjective(ArgumentMatchers.anySet());

        Mockito.doAnswer(invocation -> {
                   Set<Integer> objsID = invocation.getArgument(0);
                   obj4.set(objsID.stream().findAny().orElseThrow());
                   LOGGER.info("Received candidate objectives for player 4");
                   return 0;
               }).when(clientViewUpdaterMock4)
               .receiveCandidateObjective(ArgumentMatchers.anySet());

        // Setup chain invocation for choosing objectives
        Mockito.doAnswer(invocation -> {
                   int obj = obj1.get();
                   while (obj == 0) {
                       obj = obj1.get();
                   }
                   clientMessageSender.setPersonalObjective(obj);
                   latchForFinish.countDown();
                   return 0;
               }).when(clientViewUpdaterMock)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_OBJECTIVES));
        Mockito.doAnswer(invocation -> {
                   int obj = obj2.get();
                   while (obj == 0) {
                       obj = obj2.get();
                   }
                   clientMessageSender2.setPersonalObjective(obj);
                   latchForFinish.countDown();
                   return 0;
               }).when(clientViewUpdaterMock2)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_OBJECTIVES));
        Mockito.doAnswer(invocation -> {
                   int obj = obj3.get();
                   while (obj == 0) {
                       obj = obj3.get();
                   }
                   clientMessageSender3.setPersonalObjective(obj);
                   latchForFinish.countDown();
                   return 0;
               })
               .when(clientViewUpdaterMock3)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_OBJECTIVES));
        Mockito.doAnswer(invocation -> {
                   int obj = obj4.get();
                   while (obj == 0) {
                       obj = obj4.get();
                   }
                   clientMessageSender4.setPersonalObjective(obj);
                   latchForFinish.countDown();
                   return 0;
               })
               .when(clientViewUpdaterMock4)
               .updateGameStatus(ArgumentMatchers.eq(GameStatus.CHOOSING_OBJECTIVES));

        // sending first player nickname
        clientMessageSender.setNickname("Francesco");

        try {
            latchForGod.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // sending other players nickname
        clientMessageSender2.setNickname("Giovanni");
        clientMessageSender3.setNickname("Giuseppe");
        clientMessageSender4.setNickname("Giacomo");


        // Wait for the end of the test
        try {
            latchForFinish.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verify the invocations from Server

        // Verify no one has been notified as god player except Francesco
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(0)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(0)).notifyGodPlayer();
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(0)).notifyGodPlayer();
        // Verify starters given
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1))
               .receiveStarterCard(ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(1))
               .receiveStarterCard(ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(1))
               .receiveStarterCard(ArgumentMatchers.anyInt());
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(1))
               .receiveStarterCard(ArgumentMatchers.anyInt());
        // Verify candidate objectives given
        Mockito.verify(clientViewUpdaterMock, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock2, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock3, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
        Mockito.verify(clientViewUpdaterMock4, Mockito.times(1)).receiveCandidateObjective(
                ArgumentMatchers.anySet());
    }

    @Disabled(" for now")
    @Test
    @Timeout(value = 30)
    void testTCPKeepAlive() throws InterruptedException, IOException {
        ClientMessageSender clientMessageSender = new ClientMessageSender(clientSocket.getOut());
        ClientMessageSender clientMessageSender2 = new ClientMessageSender(clientSocket2.getOut());

        clientMessageSender2.setNickname("chen");

        clientSocket.close();

        Thread clientThread = new Thread(() -> {
            try {
                clientSocket = new ClientSocket("localhost", 12345, clientViewUpdaterMock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientSocket2.close();
        Thread.sleep(5000);


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

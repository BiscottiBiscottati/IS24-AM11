package it.polimi.ingsw.am11.chat;

import it.polimi.ingsw.am11.controller.CentralController;
import it.polimi.ingsw.am11.controller.ChatController;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.connector.ServerChatConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatManagerTest {

    static ChatController chatController = CentralController.INSTANCE.getChatController();
    @Mock
    ServerChatConnector chatConnector;

    @BeforeEach
    void setUp() {
        CentralController.INSTANCE.createNewGame();

        chatController.addPlayer("player1", chatConnector);

        chatController.addPlayer("player2", chatConnector);

        chatController.addPlayer("player3", chatConnector);

        chatController.addPlayer("player4", chatConnector);
    }

    @AfterEach
    void tearDown() {
        CentralController.INSTANCE.destroyGame();
    }

    @Test
    void sendPublicMsg() {
        chatController.broadcastMessage("player1", "Hello");
        verify(chatConnector, times(3))
                .sendPublicMsg("player1", "Hello");
        verify(chatConnector, times(1))
                .confirmSentMsg("player1", "Hello");
    }

    @Test
    void sendPrivateMsg() throws PlayerInitException {
        chatController.sendPrivateMessage("player1", "player2", "Hello");
        verify(chatConnector, times(1))
                .sendPrivateMsg("player1", "Hello");
        verify(chatConnector, times(1))
                .confirmSentMsg("player1", "Hello");
    }

    @Test
    void removePlayer() {
        chatController.removePlayer("player1");
        chatController.broadcastMessage("player2", "Hello");
        verify(chatConnector, times(2))
                .sendPublicMsg("player2", "Hello");
    }

    @Test
    void clear() {
        chatController.clear();
        chatController.broadcastMessage("player2", "Hello");
        verifyNoInteractions(chatConnector);
    }

    @Test
    void multipleMsgSent() {
        chatController.broadcastMessage("player1", "Hello");
        chatController.broadcastMessage("player2", "Hello");
        chatController.broadcastMessage("player3", "Hello");
        chatController.broadcastMessage("player4", "Hello");
        verify(chatConnector, times(12))
                .sendPublicMsg(anyString(), anyString());
        verify(chatConnector, times(4))
                .confirmSentMsg(anyString(), anyString());
    }

}
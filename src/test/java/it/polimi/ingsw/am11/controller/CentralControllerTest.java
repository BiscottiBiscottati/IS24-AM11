package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.model.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CentralControllerTest {

    CentralController centralController;

    @Mock
    PlayerConnector playerConnector;

    @Mock
    TableConnector tableConnector;

    @BeforeEach
    void setUp() {
        centralController = CentralController.INSTANCE;
    }

    @Test
    void connectPlayer() {
        assertDoesNotThrow(() -> centralController.connectPlayer("chen", playerConnector,
                                                                 tableConnector));
        assertThrows(NotSetNumOfPlayerException.class, () -> centralController.connectPlayer(
                "edo", playerConnector, tableConnector));

        assertDoesNotThrow(() -> centralController.setNumOfPlayers("chen", 3));

        assertDoesNotThrow(() -> centralController.connectPlayer("edo", playerConnector,
                                                                 tableConnector));

        assertDoesNotThrow(() -> centralController.connectPlayer("osama", playerConnector,
                                                                 tableConnector));
    }

    @Test
    void setNumOfPlayers() {
    }
}
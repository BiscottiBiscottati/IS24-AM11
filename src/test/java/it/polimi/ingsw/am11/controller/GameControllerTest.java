package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.network.connector.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.connector.ServerTableConnector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    GameController gameController;

    @Mock
    ServerPlayerConnector playerConnector;

    @Mock
    ServerTableConnector tableConnector;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
    }

    @AfterEach
    void tearDown() {
        gameController.destroyGame();
    }

    @Test
    void savesToDiskTest()
    throws NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException, NotGodPlayerException, InterruptedException,
           IllegalCardPlacingException {
        gameController.connectPlayer("chen", playerConnector, tableConnector);
        gameController.setNumOfPlayers("chen", 2);
        gameController.connectPlayer("edo", playerConnector, tableConnector);

        Thread.sleep(5100);

        gameController.getCardController().setStarterFor("chen", true);

        Thread.sleep(5100);
    }
}
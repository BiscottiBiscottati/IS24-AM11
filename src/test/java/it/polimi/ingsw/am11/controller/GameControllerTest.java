package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.controller.exceptions.NotGodPlayerException;
import it.polimi.ingsw.am11.controller.exceptions.NotSetNumOfPlayerException;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.utils.persistence.SavesManager;
import it.polimi.ingsw.am11.network.connector.ServerPlayerConnector;
import it.polimi.ingsw.am11.network.connector.ServerTableConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    static final long CUSTOM_INTERVAL = 100;

    GameController gameController;

    @Mock
    ServerPlayerConnector playerConnector;

    @Mock
    ServerTableConnector tableConnector;

    @AfterAll
    static void afterAll() {
        SavesManager.deleteAll();
    }

    @BeforeEach
    void setUp() {
        gameController = new GameController();
    }

    @Test
    void savesToDiskTest()
    throws NumOfPlayersException, PlayerInitException, NotSetNumOfPlayerException,
           GameStatusException, NotGodPlayerException, InterruptedException,
           IllegalCardPlacingException {
        gameController.connectPlayer("chen", playerConnector, tableConnector);
        gameController.setNumOfPlayers("chen", 2);
        gameController.connectPlayer("edo", playerConnector, tableConnector);

        gameController.getCardController().setStarterFor("chen", true);

        gameController.getCardController().setStarterFor("edo", true);

        assertTrue(SavesManager.loadMostRecentGame().isPresent());

    }
}
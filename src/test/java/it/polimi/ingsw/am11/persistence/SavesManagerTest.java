package it.polimi.ingsw.am11.persistence;

import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SavesManagerTest {
    GameModel model;

    @BeforeEach
    void setUp() {
        model = new GameLogic();
    }

    @Test
    void saveGame() throws NumOfPlayersException, PlayerInitException, GameStatusException {
        model.addPlayerToTable("chen", PlayerColor.BLUE);
        model.addPlayerToTable("edo", PlayerColor.RED);
        model.initGame();

        SavesManager.saveGame(model.save());
    }

    @Test
    void loadMostRecentGame() {
        model.load(SavesManager.loadMostRecentGame().orElseThrow());

        assertEquals(2, model.getPlayers().size());
        assertEquals(Set.of("chen", "edo"), model.getPlayers());
        assertEquals(GameStatus.CHOOSING_STARTERS, model.getStatus());
    }
}
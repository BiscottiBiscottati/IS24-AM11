package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.GameStatusException;
import it.polimi.ingsw.am11.exceptions.IllegalNumOfPlayersException;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    GameModel model;

    @BeforeEach
    void setUp() {
        model = new GameLogic();
    }

    @Test
    void testInitGame() {
        assertThrows(IllegalNumOfPlayersException.class, () -> model.initGame());
        assertDoesNotThrow(() -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertThrows(PlayerInitException.class,
                     () -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertDoesNotThrow(() -> model.addPlayerToTable("chen", PlayerColor.BLUE));
        assertDoesNotThrow(() -> model.addPlayerToTable("osama", PlayerColor.YELLOW));

        Set<String> players = Set.of("edo", "chen", "osama");
        assertDoesNotThrow(() -> model.initGame());

        assertEquals(3, model.getPlayers().size());
        assertEquals(players, model.getPlayers());

        assertThrows(GameStatusException.class,
                     () -> model.addPlayerToTable("lola", PlayerColor.GREEN));
        assertThrows(GameStatusException.class, model::initGame);

        assertTrue(players.contains(model.getFirstPlayer()));
        assertEquals(model.getFirstPlayer(), model.getCurrentTurnPlayer());

        assertThrows(GameStatusException.class, () -> model.removePlayer("lola"));

        assertEquals(2, model.getCommonObjectives().size());
        assertEquals(2, model.getExposedCards(PlayableCardType.GOLD).size());
        assertEquals(2, model.getExposedCards(PlayableCardType.RESOURCE).size());

        try {
            assertEquals(Position.of(0, 0),
                         model.getAvailablePositions("edo").stream()
                              .findFirst()
                              .orElseThrow());
        } catch (PlayerInitException e) {
            throw new RuntimeException(e);
        }
    }
}
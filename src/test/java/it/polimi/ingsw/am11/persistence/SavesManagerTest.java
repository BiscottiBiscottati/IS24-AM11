package it.polimi.ingsw.am11.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardPlacingException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.GameModelMemento;
import it.polimi.ingsw.am11.model.utils.persistence.SavesManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SavesManagerTest {
    GameModel model;

    @AfterAll
    static void afterAll() {
        SavesManager.deleteAll();
    }

    @BeforeEach
    void setUp() {
        model = new GameLogic();
    }

    @Test
    void simpleSaveTest() throws NumOfPlayersException, PlayerInitException, GameStatusException {
        model.addPlayerToTable("chen", PlayerColor.BLUE);
        model.addPlayerToTable("edo", PlayerColor.RED);
        model.initGame();

        SavesManager.saveGame(model.save());
    }

    @Test
    void saveAndLoadTest() throws NumOfPlayersException, PlayerInitException, GameStatusException,
                                  IllegalCardPlacingException {
        model.addPlayerToTable("chen", PlayerColor.BLUE);
        model.addPlayerToTable("edo", PlayerColor.RED);
        model.addPlayerToTable("ferdi", PlayerColor.YELLOW);
        model.initGame();

        Optional<Integer> starterChen = model.getStarterCard("chen");
        Optional<Integer> starterEdo = model.getStarterCard("edo");
        Optional<Integer> starterFerdi = model.getStarterCard("ferdi");

        SavesManager.saveGame(model.save());

        model = new GameLogic();

        model.load(SavesManager.loadMostRecentGame().orElseThrow());

        assertEquals(3, model.getPlayers().size());
        assertEquals(Set.of("chen", "edo", "ferdi"), model.getPlayers());
        assertEquals(GameStatus.CHOOSING_STARTERS, model.getStatus());

        assertEquals(starterChen, model.getStarterCard("chen"));
        assertEquals(starterEdo, model.getStarterCard("edo"));
        assertEquals(starterFerdi, model.getStarterCard("ferdi"));

        model.setStarterFor("chen", true);
        model.setStarterFor("edo", true);
        model.setStarterFor("ferdi", true);

        Set<Integer> objsChen = model.getCandidateObjectives("chen");
        Set<Integer> objsEdo = model.getCandidateObjectives("edo");
        Set<Integer> objsFerdi = model.getCandidateObjectives("ferdi");

        SavesManager.saveGame(model.save());

        model = new GameLogic();

        model.load(SavesManager.loadMostRecentGame().orElseThrow());

        assertEquals(3, model.getPlayers().size());
        assertEquals(Set.of("chen", "edo", "ferdi"), model.getPlayers());
        assertEquals(GameStatus.CHOOSING_OBJECTIVES, model.getStatus());

        assertEquals(objsChen, model.getCandidateObjectives("chen"));
        assertEquals(objsEdo, model.getCandidateObjectives("edo"));
        assertEquals(objsFerdi, model.getCandidateObjectives("ferdi"));
    }

    @Test
    void jsonSerializationTest() throws JsonProcessingException {
        GameModelMemento memento = model.save();

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(memento);
        System.out.println(json);
    }
}
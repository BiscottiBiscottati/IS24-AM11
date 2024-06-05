package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.objective.ObjectiveDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.GoldDeckFactory;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.exceptions.*;
import it.polimi.ingsw.am11.model.players.field.PositionManager;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.view.events.PlayerViewEvent;
import it.polimi.ingsw.am11.view.events.TableViewEvent;
import it.polimi.ingsw.am11.view.events.listeners.PlayerListener;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import it.polimi.ingsw.am11.view.events.utils.ActionMode;
import it.polimi.ingsw.am11.view.events.view.player.CandidateObjectiveEvent;
import it.polimi.ingsw.am11.view.events.view.player.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.PersonalObjectiveChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.StarterCardEvent;
import it.polimi.ingsw.am11.view.events.view.table.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.GameStatusChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.PlayerInfoEvent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameModelTest {

    static Deck<GoldCard> goldCardDeck;
    static Deck<ResourceCard> resourceCardDeck;
    static Deck<ObjectiveCard> objectiveCardDeck;
    static Set<String> players;
    GameModel model;

    @Mock
    PlayerListener chenListener;
    @Mock
    PlayerListener edoListener;
    @Mock
    PlayerListener osamaListener;
    @Mock
    TableListener tableListener;

    @BeforeAll
    static void beforeAll() {
        goldCardDeck = GoldDeckFactory.createDeck();
        resourceCardDeck = ResourceDeckFactory.createDeck();
        objectiveCardDeck = ObjectiveDeckFactory.createDeck();
        players = Set.of("edo", "chen", "osama");
    }

    @BeforeEach
    void setUp() {
        model = new GameLogic();
        goldCardDeck.reset();
        resourceCardDeck.reset();
        objectiveCardDeck.reset();
        model.addTableListener(tableListener);

        try {
            model.addPlayerToTable("edo", PlayerColor.RED);
            model.addPlayerToTable("chen", PlayerColor.BLUE);
            model.addPlayerToTable("osama", PlayerColor.YELLOW);
            model.addPlayerListener("edo", edoListener);
            model.addPlayerListener("chen", chenListener);
            model.addPlayerListener("osama", osamaListener);
            model.initGame();
        } catch (PlayerInitException | GameStatusException |
                 GameBreakingException | NumOfPlayersException e) {
            fail(e);
        }

        reset(chenListener, edoListener, osamaListener, tableListener);

    }

    @Test
    void testInitGame() {
        model = new GameLogic();
        ArgumentCaptor<TableViewEvent> captor = ArgumentCaptor.forClass(TableViewEvent.class);
        // Test illegal number of players
        assertThrows(NumOfPlayersException.class, () -> model.initGame());

        // Test adding of players
        assertDoesNotThrow(() -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertThrows(PlayerInitException.class,
                     () -> model.addPlayerToTable("edo", PlayerColor.RED));
        assertDoesNotThrow(() -> model.addPlayerToTable("chen", PlayerColor.BLUE));
        assertDoesNotThrow(() -> model.addPlayerToTable("osama", PlayerColor.YELLOW));

        // adding listeners
        model.addPlayerListener("edo", edoListener);
        model.addPlayerListener("chen", chenListener);
        model.addPlayerListener("osama", osamaListener);

        model.addTableListener(tableListener);

        Set<String> players = Set.of("edo", "chen", "osama");

        // Test init game
        assertDoesNotThrow(model::initGame);

        // 9 times
        // because there are 3 player field reset and 3 player points reset, 1 game status change
        // and 1 player info sent
        verify(tableListener, times(8)).propertyChange(captor.capture());

        captor.getAllValues().stream()
              .filter(GameStatusChangeEvent.class::isInstance)
              .map(GameStatusChangeEvent.class::cast)
              .forEach(event -> {
                  assertTrue(Set.of(GameStatus.SETUP, GameStatus.CHOOSING_STARTERS)
                                .contains(event.getOldValue()));
                  assertTrue(Set.of(GameStatus.CHOOSING_STARTERS, GameStatus.CHOOSING_OBJECTIVES)
                                .contains(event.getNewValue()));
              });

        captor.getAllValues().stream()
              .filter(PlayerInfoEvent.class::isInstance)
              .map(PlayerInfoEvent.class::cast)
              .findFirst()
              .ifPresentOrElse(event -> {
                                   assert event.getNewValue() != null;
                                   assertEquals(players,
                                                new HashSet<>(event.getNewValue().values()));
                               },
                               () -> fail("Player info event not found"));

        reset(tableListener);
        captor = ArgumentCaptor.forClass(TableViewEvent.class);

        verify(chenListener, times(1))
                .propertyChange(argThat(StarterCardEvent.class::isInstance));
        verify(edoListener, times(1))
                .propertyChange(argThat(StarterCardEvent.class::isInstance));
        verify(osamaListener, times(1))
                .propertyChange(argThat(StarterCardEvent.class::isInstance));

        // Check if the players are the same
        assertEquals(3, model.getPlayers().size());
        assertEquals(players, model.getPlayers());

        // Check if user can't add more players
        assertThrows(GameStatusException.class,
                     () -> model.addPlayerToTable("lola", PlayerColor.GREEN));
        assertThrows(GameStatusException.class, model::initGame);

        for (String player : players) {
            try {
                model.setStarterFor(player, true);
            } catch (IllegalCardPlacingException | GameStatusException | PlayerInitException |
                     GameBreakingException e) {
                fail(e);
            }
        }


        verify(tableListener, times(players.size() + 1))
                .propertyChange(captor.capture());

        captor.getAllValues().stream()
              .filter(FieldChangeEvent.class::isInstance)
              .map(FieldChangeEvent.class::cast)
              .forEach(event -> {
                  assertEquals(event.getAction(), ActionMode.INSERTION);
                  assertEquals(Position.of(0, 0), event.getValueOfAction().getKey());
                  assertTrue(players.contains(event.getPlayer().orElseThrow()));
              });

        captor.getAllValues().stream()
              .filter(GameStatusChangeEvent.class::isInstance)
              .map(GameStatusChangeEvent.class::cast)
              .forEach(event -> {
                  assertEquals(GameStatus.CHOOSING_STARTERS, event.getOldValue());
                  assertEquals(GameStatus.CHOOSING_OBJECTIVES, event.getNewValue());
              });

        try {
            Set<Position> availPos = Stream.of(Corner.values())
                                           .map(corner -> PositionManager.getPositionIn(
                                                   Position.of(0, 0), corner))
                                           .collect(Collectors.toSet());
            assertEquals(availPos,
                         model.getAvailablePositions("edo"));
        } catch (PlayerInitException | GameStatusException e) {
            fail(e);
        }
    }

    @Test
    void testDealingStarter() {
        Map<String, Integer> starterCards = new HashMap<>(8);

        try {
            // Test if the player can pick a starter card
            starterCards.put("edo", model.getStarterCard("edo").orElseThrow());
            starterCards.put("osama", model.getStarterCard("osama").orElseThrow());
            starterCards.put("chen", model.getStarterCard("chen").orElseThrow());

        } catch (GameStatusException | PlayerInitException e) {
            fail(e);
        }

        // Test if the player can't pick a starter card twice

        try {

            // pick if Starter is present
            assertTrue(model.getStarterCard("edo").isPresent());


            // Test if the player doesn't exist
            assertThrows(PlayerInitException.class, () -> model.getStarterCard("lola"));

            // Test if before placing positions the starter card is not set
            assertTrue(model.getAvailablePositions("edo")
                            .contains(Position.of(0, 0)));

            Function<String, Optional<Integer>> getStarter = player -> {
                try {
                    return model.getStarterCard(player);
                } catch (PlayerInitException | GameStatusException e) {
                    throw new RuntimeException(e);
                }
            };

            // getting all starters dealt
            Set<Integer> starters = players.stream()
                                           .map(getStarter)
                                           .filter(Optional::isPresent)
                                           .map(Optional::get)
                                           .collect(Collectors.toSet());
            // check starters are the same as players
            assertEquals(3, starters.size());

            for (String player : players) {
                // check if starter is the same for each player
                assertEquals(Optional.of(starterCards.get(player)),
                             model.getStarterCard(player));

                // place starter for each player
                model.setStarterFor(player, true);

                // check that nothing changes
                assertEquals(Optional.of(starterCards.get(player)),
                             model.getStarterCard(player));
            }

            ArgumentCaptor<TableViewEvent> captor = ArgumentCaptor.forClass(TableViewEvent.class);

            verify(tableListener, times(players.size() + 1))
                    .propertyChange(captor.capture());

            captor.getAllValues().stream()
                  .filter(FieldChangeEvent.class::isInstance)
                  .map(FieldChangeEvent.class::cast)
                  .forEach(event -> {
                      assertEquals(event.getAction(), ActionMode.INSERTION);
                      assertEquals(Position.of(0, 0), event.getValueOfAction().getKey());
                      assertTrue(players.contains(event.getPlayer().orElseThrow()));
                  });

            captor.getAllValues().stream()
                  .filter(GameStatusChangeEvent.class::isInstance)
                  .map(GameStatusChangeEvent.class::cast)
                  .forEach(event -> {
                      assertEquals(GameStatus.CHOOSING_STARTERS, event.getOldValue());
                      assertEquals(GameStatus.CHOOSING_OBJECTIVES, event.getNewValue());
                  });

            // get all positions around 0,0
            Set<Position> positions = Stream.of(Corner.values())
                                            .map(corner -> PositionManager.getPositionIn(
                                                    Position.of(0, 0), corner))
                                            .collect(Collectors.toSet());

            Function<String, Set<Position>> getAvailablePos = player -> {
                try {
                    return model.getAvailablePositions(player);
                } catch (PlayerInitException | GameStatusException e) {
                    throw new RuntimeException(e);
                }
            };

            // check that available positions are the same as the ones around 0,0
            players.stream()
                   .map(getAvailablePos)
                   .map(positions::containsAll)
                   .forEach(Assertions::assertTrue);

        } catch (PlayerInitException | GameStatusException | IllegalCardPlacingException |
                 GameBreakingException e) {
            fail(e);
        }

    }

    // FIXME to fix
    @Disabled("for now")
    @Test
    void testDealingObjective() {

        // FIXME to fix events calls

        ArgumentCaptor<PlayerViewEvent> playerCaptor = ArgumentCaptor.forClass(
                PlayerViewEvent.class);

        ArgumentCaptor<TableViewEvent> tableCaptor = ArgumentCaptor.forClass(
                TableViewEvent.class);

        for (String player : players) {
            assertDoesNotThrow(() -> model.setStarterFor(player, true));
        }

        verify(chenListener, times(1))
                .propertyChange(playerCaptor.capture());

        verify(edoListener, times(1))
                .propertyChange(playerCaptor.capture());

        verify(osamaListener, times(1))
                .propertyChange(playerCaptor.capture());

        verify(tableListener, times(1))
                .propertyChange(tableCaptor.capture());

        playerCaptor.getAllValues().stream()
                    .filter(CandidateObjectiveEvent.class::isInstance)
                    .map(CandidateObjectiveEvent.class::cast)
                    .forEach(event -> {
                        assertEquals(2, event.getNewValue().size());
                        assertTrue(event.getNewValue().stream()
                                        .map(objectiveCardDeck::getCardById)
                                        .allMatch(Optional::isPresent));
                    });

        tableCaptor.getAllValues().stream()
                   .filter(GameStatusChangeEvent.class::isInstance)
                   .map(GameStatusChangeEvent.class::cast)
                   .forEach(event -> {
                       assertEquals(GameStatus.CHOOSING_STARTERS, event.getOldValue());
                       assertEquals(GameStatus.CHOOSING_OBJECTIVES, event.getNewValue());
                   });

        reset(chenListener, edoListener, osamaListener, tableListener);

        Map<String, Integer> objOfPlayer = new HashMap<>(8);
        try {
            for (String player : players) {
                // pick candidate objectives
                Set<Integer> objCards = model.getCandidateObjectives(player);

                // check the size of candidate objectives
                assertEquals(2, objCards.size());
                assertEquals(objCards, model.getCandidateObjectives(player));


                // check that they are objective cards
                assertEquals(2,
                             objCards.stream()
                                     .map(objectiveCardDeck::getCardById)
                                     .filter(Optional::isPresent)
                                     .count());

                // check that player can't set an objective that is not in the candidate objectives
                assertThrows(IllegalPlayerSpaceActionException.class,
                             () -> model.setObjectiveFor(player, 0));

                int chosenCard = objCards.stream().findFirst().orElseThrow();
                objOfPlayer.put(player, chosenCard);

                // set objective for player
                model.setObjectiveFor(player, chosenCard);

                // check that objective has been set
                assertEquals(chosenCard,
                             model.getPlayerObjective(player).stream().findFirst().orElseThrow());
            }

            for (String player : players) {
                // check each objective has been set and are equals
                Optional<Integer> objCard = model.getPlayerObjective(player).stream().findFirst();
                assertEquals(Optional.of(objOfPlayer.get(player)), objCard);
            }
        } catch (GameStatusException | PlayerInitException | IllegalPlayerSpaceActionException |
                 GameBreakingException e) {
            fail(e);
        }

        playerCaptor = ArgumentCaptor.forClass(PlayerViewEvent.class);

        verify(chenListener, times(4))
                .propertyChange(playerCaptor.capture());

        verify(edoListener, times(4))
                .propertyChange(playerCaptor.capture());

        verify(osamaListener, times(4))
                .propertyChange(playerCaptor.capture());

        playerCaptor.getAllValues().stream()
                    .filter(PersonalObjectiveChangeEvent.class::isInstance)
                    .map(PersonalObjectiveChangeEvent.class::cast)
                    .forEach(event -> assertEquals(objOfPlayer.get(event.getPlayer()),
                                                   event.getNewValue()));

        playerCaptor.getAllValues().stream()
                    .filter(HandChangeEvent.class::isInstance)
                    .map(HandChangeEvent.class::cast)
                    .forEach(event -> {
                        assertEquals(ActionMode.INSERTION, event.getAction());
                        assertTrue(Stream.of(event.getNewValue())
                                         .filter(Objects::nonNull)
                                         .map(GameModelTest::getPlayableFromId)
                                         .allMatch(Optional::isPresent));
                    });

        verify(tableListener, times(11))
                .propertyChange(tableCaptor.capture());

        tableCaptor.getAllValues().stream()
                   .filter(GameStatusChangeEvent.class::isInstance)
                   .map(GameStatusChangeEvent.class::cast)
                   .forEach(event -> {
                       assertEquals(GameStatus.CHOOSING_OBJECTIVES, event.getOldValue());
                       assertEquals(GameStatus.ONGOING, event.getNewValue());
                   });

        List<TableViewEvent> events = tableCaptor.getAllValues();
    }

    static @NotNull Optional<? extends PlayableCard> getPlayableFromId(int id) {
        return Stream.of(goldCardDeck, resourceCardDeck)
                     .map(deck -> deck.getCardById(id))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findFirst();
    }


}
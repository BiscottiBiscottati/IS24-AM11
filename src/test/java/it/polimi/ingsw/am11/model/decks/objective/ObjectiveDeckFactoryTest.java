package it.polimi.ingsw.am11.model.decks.objective;

import it.polimi.ingsw.am11.model.cards.objective.CollectingCard;
import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.PositioningCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.utils.Item;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.utils.DatabaseConstants;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveDeckFactoryTest {
    Connection connection;
    PreparedStatement collectingStatement;
    PreparedStatement positioningStatement;

    @AfterEach
    void tearDown() {
        try {
            if (collectingStatement != null && ! collectingStatement.isClosed())
                collectingStatement.close();
            if (positioningStatement != null && ! positioningStatement.isClosed())
                positioningStatement.close();
            if (connection != null && ! connection.isClosed()) connection.close();
        } catch (SQLException e) {
            fail(e);
        }
    }

    @BeforeEach
    void setUp() {
        try {
            connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
            collectingStatement = connection.prepareStatement(
                    "SELECT * FROM item_collect_cards WHERE global_id = ?");
            positioningStatement = connection.prepareStatement(
                    "SELECT * FROM positioning_cards WHERE global_id = ?");
        } catch (SQLException e) {
            fail(e);
        }
    }

    @Test
    void createDeck() {
        Deck<ObjectiveCard> deck = ObjectiveDeckFactory.createDeck();

        // Testing the creation of an ObjectiveDeck
        assertNotNull(deck);

        // Testing the size of the deck
        assertEquals(16, deck.getRemainingCards());

        Set<Integer> uniqueId = new HashSet<>(16);

        // Testing the uniqueness of the cards
        for (int i = 0; i < 16; i++) {
            Optional<ObjectiveCard> optCard = deck.draw();

            // Testing the card not empty
            assertFalse(optCard.isEmpty());

            ObjectiveCard card = optCard.get();

            // Testing uniqueness of the id
            int tempId = card.getId();
            assertTrue(uniqueId.add(tempId));

            try {
                switch (card) {
                    case CollectingCard collectingCard -> checkCollecting(collectingCard);
                    case PositioningCard positioningCard -> checkPositioning(positioningCard);
                    default -> throw new IllegalStateException("Unexpected value: " + card);
                }
            } catch (SQLException e) {
                fail(e);
            }
        }
    }

    private void checkCollecting(@NotNull CollectingCard card) throws SQLException {
        collectingStatement.setInt(1, card.getId());
        try (ResultSet resultSet = collectingStatement.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(card.getPoints(), resultSet.getInt("points"));
                assertEquals(card.getType().name(), resultSet.getString("card_type"));
                for (Color color : Color.values()) {
                    assertEquals(card.hasItemRequirements(color),
                                 resultSet.getInt(color.getColumnName()));
                }
                for (Symbol symbol : Symbol.values()) {
                    assertEquals(card.hasItemRequirements(symbol),
                                 resultSet.getInt(symbol.getColumnName()));
                }
            } else {
                throw new SQLException("Card not found in the database");
            }
        }
    }

    private void checkPositioning(@NotNull PositioningCard positioningCard)
    throws SQLException {
        positioningStatement.setInt(1, positioningCard.getId());
        try (ResultSet resultSet = positioningStatement.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(positioningCard.getPoints(), resultSet.getInt("points"));
                assertEquals(positioningCard.getType().name(), resultSet.getString("card_type"));
                switch (positioningCard) {
                    case TripletCard triplet -> {
                        assertEquals(triplet.isFlipped(), resultSet.getBoolean("is_flipped"));
                        Color colorToCheck = Color.valueOf(resultSet.getString("primary_color"));
                        assertEquals(3, triplet.hasItemRequirements(colorToCheck));
                        getItems().filter(item -> item != colorToCheck)
                                  .forEach(item -> assertEquals(0,
                                                                triplet.hasItemRequirements(item)));
                    }
                    case LCard lCard -> {
                        assertEquals(lCard.isFlipped(), resultSet.getBoolean("is_flipped"));
                        assertEquals(lCard.isRotated(), resultSet.getBoolean("is_rotated"));
                        Color colorToCheck = Color.valueOf(resultSet.getString("primary_color"));
                        Color secondColorToCheck = Color.valueOf(
                                resultSet.getString("secondary_color"));
                        assertEquals(2, lCard.hasItemRequirements(colorToCheck));
                        assertEquals(1, lCard.hasItemRequirements(secondColorToCheck));
                        getItems().filter(
                                          item -> item != colorToCheck &&
                                                  item != secondColorToCheck)
                                  .forEach(
                                          item -> assertEquals(0,
                                                               lCard.hasItemRequirements(item)));
                    }
                    default ->
                            throw new IllegalStateException("Unexpected value: " + positioningCard);
                }
            } else {
                throw new SQLException("Card not found in the database");
            }
        }
    }

    private static @NotNull Stream<Item> getItems() {
        return Stream.concat(Arrays.stream(Color.values()), Arrays.stream(Symbol.values()));
    }
}
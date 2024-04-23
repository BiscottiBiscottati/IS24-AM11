package it.polimi.ingsw.am11.model.decks.playable;

import it.polimi.ingsw.am11.model.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.playable.ResourceDeckFactory;
import it.polimi.ingsw.am11.model.decks.utils.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDeckFactoryTest {

    Connection connection;
    PreparedStatement idQuery;

    @BeforeEach
    void setUp() {
        try {
            connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
            idQuery = connection.prepareStatement(
                    "SELECT * FROM playable_cards WHERE global_id = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (idQuery != null && ! idQuery.isClosed()) idQuery.close();
            if (connection != null && ! connection.isClosed()) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createDeck() {
        Deck<ResourceCard> deck = ResourceDeckFactory.createDeck();

        // Testing the creation of a ResourceDeck
        assertNotNull(deck);

        // Testing the size of the deck
        assertEquals(40, deck.getRemainingCards());

        Set<Integer> uniqueIds = new HashSet<>(40);

        // Testing the uniqueness of the cards
        for (int i = 0; i < 40; i++) {
            Optional<ResourceCard> optCard = deck.draw();

            assertFalse(optCard.isEmpty());

            ResourceCard card = optCard.get();

            int tempId = card.getId();
            assertTrue(uniqueIds.add(tempId));

            // Testing card contents
            try {
                idQuery.setInt(1, tempId);

                try (ResultSet result = idQuery.executeQuery()) {
                    result.next();

                    // Testing the card color
                    assertEquals(result.getString("card_color"), card.getColor().name());

                    // Testing the card value
                    assertEquals(result.getString("card_type"), card.getType().name());

                    // Testing the card symbol
                    assertEquals(result.getInt("points"), card.getPoints());

                    for (Corner corner : Corner.values()) {
                        switch (CornerContainer.of(result.getString(corner.getColumnName()))) {
                            case Availability.NOT_USABLE -> {
                                assertFalse(card.isFrontAvailable(corner));
                                assertEquals(Availability.NOT_USABLE, card.getItemCorner(corner));
                            }
                            case Availability.USABLE -> {
                                assertTrue(card.isFrontAvailable(corner));
                                assertEquals(Availability.USABLE, card.getItemCorner(corner));
                            }
                            case Symbol symbol -> {
                                assertTrue(card.isFrontAvailable(corner));
                                assertEquals(symbol, card.getItemCorner(corner));
                            }
                            case Color color -> {
                                assertTrue(card.isFrontAvailable(corner));
                                assertEquals(color, card.getItemCorner(corner));
                            }
                            default -> throw new IllegalStateException("Unexpected value!");
                        }
                    }
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
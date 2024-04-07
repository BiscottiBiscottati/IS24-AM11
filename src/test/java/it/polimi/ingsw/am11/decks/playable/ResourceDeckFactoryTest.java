package it.polimi.ingsw.am11.decks.playable;

import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class ResourceDeckFactoryTest {

    Connection connection;
    PreparedStatement idQuery;

    @BeforeEach
    void setUp() {
        try {
            connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
            idQuery = connection.prepareStatement("SELECT * FROM playable_cards WHERE global_id = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (idQuery != null && !idQuery.isClosed()) idQuery.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createDeck() {
        Deck<ResourceCard> deck = ResourceDeckFactory.createDeck();

        // Testing the creation of a ResourceDeck
        Assertions.assertNotNull(deck);

        // Testing size of the deck
        Assertions.assertEquals(40, deck.getRemainingCards());

        Set<Integer> uniqueIds = new HashSet<>(40);

        // Testing the uniqueness of the cards
        for (int i = 0; i < 40; i++) {
            ResourceCard card = deck.drawCard();

            Assertions.assertNotNull(card);

            int tempId = card.getId();
            Assertions.assertFalse(uniqueIds.contains(tempId));
            uniqueIds.add(tempId);

            // Testing card contents
            try {
                idQuery.setInt(1, tempId);

                try (ResultSet result = idQuery.executeQuery()) {
                    result.next();

                    // Testing the card color
                    Assertions.assertEquals(result.getString("card_color"), card.getColor().name());

                    // Testing the card value
                    Assertions.assertEquals(result.getString("card_type"), card.getType().name());

                    // Testing the card symbol
                    Assertions.assertEquals(result.getInt("points"), card.getPoints());

                    for (Corner corner : Corner.values()) {
                        switch (CornerContainer.of(result.getString(corner.getColumnName()))) {
                            case Availability.NOT_USABLE -> {
                                Assertions.assertFalse(card.isFrontAvailable(corner));
                                Assertions.assertEquals(Availability.NOT_USABLE, card.checkItemCorner(corner));
                            }
                            case Availability.USABLE -> {
                                Assertions.assertTrue(card.isFrontAvailable(corner));
                                Assertions.assertEquals(Availability.USABLE, card.checkItemCorner(corner));
                            }
                            case Symbol symbol -> {
                                Assertions.assertTrue(card.isFrontAvailable(corner));
                                Assertions.assertEquals(symbol, card.checkItemCorner(corner));
                            }
                            case Color color -> {
                                Assertions.assertTrue(card.isFrontAvailable(corner));
                                Assertions.assertEquals(color, card.checkItemCorner(corner));
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
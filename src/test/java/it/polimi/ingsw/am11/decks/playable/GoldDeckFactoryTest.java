package it.polimi.ingsw.am11.decks.playable;

import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.Availability;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.DatabaseConstants;
import it.polimi.ingsw.am11.decks.Deck;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class GoldDeckFactoryTest {

    GoldDeckFactory factory;
    Connection connection;

    PreparedStatement idQuery;

    PreparedStatement placingReqQuery;

    @AfterEach
    void tearDown() {
        try {
            if (idQuery != null && !idQuery.isClosed()) idQuery.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        factory = new GoldDeckFactory();
        try {
            connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
            idQuery = connection.prepareStatement("SELECT * FROM playable_cards WHERE global_id = ?");
            placingReqQuery = connection.prepareStatement("SELECT * FROM placing_requirements WHERE id = ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createDeck() {
        Deck<GoldCard> deck = factory.createDeck();

        // Testing the creation of a GoldDeck
        Assertions.assertNotNull(deck);

        // testing contents of the deck
        Assertions.assertEquals(40, deck.getRemainingCards());

        Set<Integer> uniqueId = new HashSet<>(40);

        // Testing the card's properties correctness and uniqueness in database
        for (int i = 0; i < 40; i++) {
            GoldCard card = deck.drawCard();

            // Testing the card not null
            Assertions.assertNotNull(card);

            // Testing uniqueness of the id
            int temp_id = card.getId();
            Assertions.assertFalse(uniqueId.contains(temp_id));
            uniqueId.add(temp_id);

            try {
                idQuery.setInt(1, temp_id);
                try (ResultSet result = idQuery.executeQuery()) {
                    result.next();

                    // Testing the card color
                    Assertions.assertEquals(result.getString("card_color"), card.getColor().name());

                    // Testing the card value
                    Assertions.assertEquals(result.getString("card_type"), card.getType().name());

                    // Testing the card symbol
                    Assertions.assertEquals(result.getInt("points"), card.getPoints());

                    // Testing the card symbol to collect
                    Assertions.assertEquals(
                            result.getString("symbol_to_collect"),
                            card.getSymbolToCollect().isEmpty() ? null : card.getSymbolToCollect().get().name()
                    );

                    // Testing the card placing requirements
                    Assertions.assertNotNull(card.getPlacingRequirements());
                    int placingRequirementsId = result.getInt("placing_requirements_id");
                    placingReqQuery.setInt(1, placingRequirementsId);
                    try (ResultSet placingResult = placingReqQuery.executeQuery()) {
                        placingResult.next();
                        for (Color color : Color.values()) {
                            Assertions.assertEquals(placingResult.getInt(color.getColumnName()),
                                                    card.getPlacingRequirements().get(color));
                        }
                    }

                    // Testing the card points requirements
                    Assertions.assertNotNull(card.getPointsRequirements());
                    Assertions.assertEquals(result.getString("points_requirements"),
                                            card.getPointsRequirements().name());

                    // Testing the card front corners availability
                    for (Corner corner : Corner.values()) {
                        switch (CornerContainer.of(result.getString(corner.getColumnName()))) {
                            case Availability.NOT_USABLE -> {
                                Assertions.assertFalse(card.isAvailable(corner));
                                Assertions.assertEquals(Availability.NOT_USABLE, card.checkItemCorner(corner));
                            }
                            case Availability.USABLE -> {
                                Assertions.assertTrue(card.isAvailable(corner));
                                Assertions.assertEquals(Availability.USABLE, card.checkItemCorner(corner));
                            }
                            case Symbol symbol -> {
                                Assertions.assertTrue(card.isAvailable(corner));
                                Assertions.assertEquals(symbol, card.checkItemCorner(corner));
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
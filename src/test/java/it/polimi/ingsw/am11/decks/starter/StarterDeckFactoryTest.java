package it.polimi.ingsw.am11.decks.starter;

import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class StarterDeckFactoryTest {

    Connection connection;
    PreparedStatement queryStatement;
    PreparedStatement centerStatement;

    @AfterEach
    void tearDown() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @BeforeEach
    void setUp() {
        try {
            connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
            queryStatement = connection.prepareStatement("SELECT * FROM starter_cards WHERE global_id = ?");
            centerStatement = connection.prepareStatement("SELECT * FROM center_colors WHERE id = ?");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void createDeck() {
        Deck<StarterCard> deck = StarterDeckFactory.createDeck();

        // Testing the creation of a StarterDeck
        Assertions.assertNotNull(deck);

        // Testing the size of the deck
        Assertions.assertEquals(6, deck.getRemainingCards());

        Set<Integer> uniqueIds = new HashSet<>(6);

        // Testing the presence of unique ids
        for (int i = 0; i < 6; i++) {
            Optional<StarterCard> optCard = deck.draw();

            // Testing the card not null
            Assertions.assertFalse(optCard.isEmpty());

            StarterCard card = optCard.get();

            // Testing uniqueness of the id
            int tempId = card.getId();
            Assertions.assertTrue(uniqueIds.add(tempId));

            // Testing card contents
            try {
                queryStatement.setInt(1, tempId);
                try (ResultSet result = queryStatement.executeQuery()) {
                    result.next();

                    // Testing Card's front and retro Corner's content
                    for (Corner corner : Corner.values()) {
                        Assertions.assertEquals(
                                result.getString("front_" + corner.getColumnName()),
                                card.checkFront(corner).toString());
                        Assertions.assertEquals(
                                result.getString("retro_" + corner.getColumnName()),
                                card.checkRetroColorIn(corner).toString());
                    }

                    // Setting the center colors
                    centerStatement.setInt(1, result.getInt("front_center_color_id"));
                }

                // Testing the center colors
                try (ResultSet centerResult = centerStatement.executeQuery()) {
                    centerResult.next();
                    for (Color color : Color.values()) {
                        if (centerResult.getBoolean(color.getColumnName())) {
                            Assertions.assertTrue(card.getCenterColorsFront().contains(color));
                        } else Assertions.assertFalse(card.getCenterColorsFront().contains(color));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
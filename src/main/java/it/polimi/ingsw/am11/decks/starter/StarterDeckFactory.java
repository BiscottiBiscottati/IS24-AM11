package it.polimi.ingsw.am11.decks.starter;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DatabaseConstants;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class StarterDeckFactory {

    private static final String queryStatement = "SELECT * FROM starter_cards";
    private static final String CENTER_QUERY = "SELECT * FROM center_colors WHERE id = ?";

    private StarterDeckFactory() {
    }

    private static void setCenterColors(@NotNull PreparedStatement centerStatement,
                                        @NotNull ResultSet result,
                                        StarterCard.Builder cardBuilder)
    throws SQLException {
        centerStatement.setInt(1, result.getInt("front_center_color_id"));
        try (ResultSet centerResult = centerStatement.executeQuery()) {
            centerResult.next();
            for (Color color : Color.values()) {
                if (centerResult.getBoolean(color.getColumnName())) {
                    cardBuilder.hasCenterColor(color);
                }
            }
        }
    }

    private static void setFrontRetroCorners(StarterCard.Builder cardBuilder,
                                             ResultSet result)
    throws IllegalCardBuildException, SQLException {
        for (Corner corner : Corner.values()) {
            cardBuilder.hasItemFrontIn(
                    corner,
                    CornerContainer.of(result.getString("front_" + corner.getColumnName())));
            cardBuilder.hasColorRetroIn(
                    corner,
                    Color.valueOf(result.getString("retro_" + corner.getColumnName())));
        }
    }

    /**
     * Creates a deck of <code>StarterCard</code> based on the SQLite database.
     * <p>
     * This method retrieves data from the SQLite database and uses it to create a deck of <code>StarterCard</code>. It
     * first establishes a connection to the database and prepares two statements to execute queries.
     * <p>
     * The first query retrieves all the data needed to create a <code>StarterCard</code>.
     * <p>
     * The second query retrieves the center colors of the card.
     * <p>
     * For each row in the result set of the first query, it creates a new <code>StarterCard</code> and adds it to the
     * deck. It sets the front and retro corners of the card and the center colors.
     * <p>
     * If an <code>SQLException</code> or <code>IllegalCardBuildException</code> is thrown during this process, it is
     * caught and wrapped in a <code>RuntimeException</code>.
     *
     * @return A deck of Starter Cards.
     * @throws RuntimeException if an <code>SQLException</code> or <code>IllegalCardBuildException</code> is thrown
     *                          during the creation of the deck.
     * @see SQLException
     * @see IllegalCardBuildException
     */
    @Contract(" -> new")
    public static @NotNull Deck<StarterCard> createDeck() {
        // Builder for the ImmutableMap that will hold the Starter Cards
        ImmutableMap.Builder<Integer, StarterCard> builder = ImmutableMap.builder();

        // Try-with-resources block to manage the database connection and statements
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(queryStatement);
             ResultSet result = statement.executeQuery();
             PreparedStatement centerStatement = connection.prepareStatement(CENTER_QUERY)) {

            // Loop over the result set
            while (result.next()) {
                // Get the data needed to create a Starter Card
                int id = result.getInt("global_id");
                StarterCard.Builder cardBuilder = new StarterCard.Builder(id);

                // Set the front and retro corners and the center colors of the card
                setFrontRetroCorners(cardBuilder, result);
                setCenterColors(centerStatement, result, cardBuilder);

                // Add the card to the builder
                builder.put(id, cardBuilder.build());
            }

        } catch (SQLException | IllegalCardBuildException e) {
            // If an exception is thrown, wrap it in a RuntimeException and rethrow it
            throw new RuntimeException(e);
        }

        // Return a new Deck containing the Starter Cards
        return new Deck<>(builder.build());
    }

}

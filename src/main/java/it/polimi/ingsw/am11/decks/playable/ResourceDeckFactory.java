package it.polimi.ingsw.am11.decks.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.playable.ResourceCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DatabaseConstants;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;


public class ResourceDeckFactory {

    public static final String queryStatement = "SELECT * FROM playable_cards WHERE card_type = 'RESOURCE'";

    private ResourceDeckFactory() {
    }

    private static CornerContainer getCornerContainer(@NotNull Corner corner,
                                                      @NotNull ResultSet result)
    throws SQLException {
        return CornerContainer.of(result.getString(corner.getColumnName()));
    }

    private static void setFrontCorners(ResourceCard.Builder cardBuilder, ResultSet result) throws SQLException {
        for (Corner corner : Corner.values()) {
            cardBuilder.hasIn(corner, getCornerContainer(corner, result));
        }
    }

    /**
     * Creates a deck of <code>ResourceCard</code> based on the SQLite database.
     * <p>
     * This method retrieves data from the SQLite database and uses it to create a deck of <code>ResourceCard</code>. It
     * first establishes a connection to the database and prepares a statement to execute a query. The query retrieves
     * all the data needed to create a <code>ResourceCard</code>.
     * <p>
     * For each row in the result set, it creates a new Resource Card and adds it to the deck.
     * <p>
     * If an <code>SQLException</code> or <code>IllegalCardBuildException</code> is thrown during this process, it is
     * caught and wrapped in a <code>RuntimeException</code>.
     *
     * @return A deck of Resource Cards.
     * @throws RuntimeException if an <code>SQLException</code> or <code>IllegalCardBuildException</code> is thrown
     *                          during the creation of the deck.
     * @see SQLException
     * @see IllegalCardBuildException
     */
    @Contract(" -> new")
    public static @NotNull Deck<ResourceCard> createDeck() {
        // Builder for the ImmutableMap that will hold the Resource Cards
        ImmutableMap.Builder<Integer, ResourceCard> builder = new ImmutableMap.Builder<>();

        // Try-with-resources block to manage the database connection and statement
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(queryStatement);
             ResultSet resultSet = statement.executeQuery()) {

            // Loop over the result set
            while (resultSet.next()) {
                // Get the data needed to create a Resource Card
                int id = resultSet.getInt("global_id");
                ResourceCard.Builder cardBuilder = new ResourceCard.Builder(
                        id,
                        resultSet.getInt("points"),
                        Color.valueOf(resultSet.getString("card_color"))
                );

                // Set the front corners of the card
                setFrontCorners(cardBuilder, resultSet);

                // Add the card to the builder
                builder.put(id, cardBuilder.build());
            }
        } catch (SQLException | IllegalCardBuildException e) {
            // If an exception is thrown, wrap it in a RuntimeException and rethrow it
            throw new RuntimeException(e);
        }

        // Return a new Deck containing the Resource Cards
        return new Deck<>(builder.build());
    }
}

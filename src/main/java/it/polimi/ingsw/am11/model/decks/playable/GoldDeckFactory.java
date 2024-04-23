package it.polimi.ingsw.am11.model.decks.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.model.cards.playable.GoldCard;
import it.polimi.ingsw.am11.model.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.enums.PointsRequirementsType;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.decks.Deck;
import it.polimi.ingsw.am11.model.decks.utils.DatabaseConstants;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class GoldDeckFactory {

    public static final String queryStatement = "SELECT * FROM playable_cards WHERE card_type = " +
                                                "'GOLD'";
    public static final String PLACING_REQ_QUERY = "SELECT * FROM placing_requirements WHERE id =" +
                                                   " ?";

    private GoldDeckFactory() {
    }

    /**
     * Creates a deck of <code>GoldCard</code> based on the SQLite database.
     * <p>
     * This method retrieves data from the SQLite database and uses it to create a deck of
     * <code>GoldCard</code>. It first establishes a connection to the database and prepares a
     * statement to execute a query. The query retrieves all the data needed to create a
     * <code>GoldCard</code>.
     * <p>
     * For each row in the result set, it creates a new <code>GoldCard</code> and adds it to the
     * deck.
     * <p>
     * If an <code>SQLException</code> or <code>IllegalCardBuildException</code> is thrown during
     * this process, it is caught and wrapped in a <code>RuntimeException</code>.
     *
     * @return A deck of Gold Cards.
     * @throws RuntimeException if an <code>SQLException</code> or
     *                          <code>IllegalCardBuildException</code> is thrown during the
     *                          creation of the deck.
     * @see SQLException
     * @see IllegalCardBuildException
     */
    @Contract(" -> new")
    public static @NotNull Deck<GoldCard> createDeck() {
        // Builder for the ImmutableMap that will hold the Gold Cards
        ImmutableMap.Builder<Integer, GoldCard> builder = ImmutableMap.builder();

        // Try-with-resources block to manage the database connection and statement
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(queryStatement);
             ResultSet resultSet = statement.executeQuery()) {

            // Loop over the result set
            while (resultSet.next()) {
                // Get the data needed to create a Gold Card
                int id = resultSet.getInt("global_id");
                GoldCard.Builder cardBuilder = new GoldCard.Builder(
                        id,
                        resultSet.getInt("points"),
                        Color.valueOf(resultSet.getString("card_color")));

                // Set the front corners, placing requirements, and points requirements of the card
                setFrontCorners(cardBuilder, resultSet);
                setPlacingRequirements(cardBuilder, resultSet);
                setPointsRequirements(cardBuilder, resultSet);

                // Add the card to the builder
                builder.put(id, cardBuilder.build());
            }
        } catch (SQLException | IllegalCardBuildException e) {
            // If an exception is thrown, wrap it in a RuntimeException and rethrow it
            throw new RuntimeException(e);
        }

        // Return a new Deck containing the Gold Cards
        return new Deck<>(builder.build());
    }

    private static void setFrontCorners(@NotNull GoldCard.Builder cardBuilder,
                                        @NotNull ResultSet result)
    throws IllegalCardBuildException, SQLException {
        for (Corner corner : Corner.values()) {
            cardBuilder.hasIn(corner, getCornerContainer(corner, result));
        }
    }

    private static void setPlacingRequirements(@NotNull GoldCard.Builder cardBuilder,
                                               @NotNull ResultSet result)
    throws SQLException {
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement supportStatement = connection.prepareStatement(PLACING_REQ_QUERY)) {
            supportStatement.setInt(1, result.getInt("placing_requirements_id"));
            try (ResultSet placingResult = supportStatement.executeQuery()) {
                placingResult.next();
                for (Color color : Color.values()) {
                    cardBuilder.hasRequirements(color, placingResult.getInt(color.getColumnName()));
                }
            }
        }
    }


    private static void setPointsRequirements(@NotNull GoldCard.Builder cardBuilder,
                                              @NotNull ResultSet result)
    throws SQLException {
        String symbolToCollect = result.getString("symbol_to_collect");
        String resultString = result.getString("points_requirements");
        cardBuilder.hasPointRequirements(PointsRequirementsType.valueOf(resultString));
        if (symbolToCollect == null) {
            cardBuilder.hasSymbolToCollect(null);
        } else {
            cardBuilder.hasSymbolToCollect(Symbol.valueOf(symbolToCollect));
        }
    }

    private static CornerContainer getCornerContainer(@NotNull Corner corner,
                                                      @NotNull ResultSet result)
    throws SQLException {
        if (result.isClosed()) {
            throw new RuntimeException("Result set is closed");
        }
        return CornerContainer.of(result.getString(corner.getColumnName()));
    }
}

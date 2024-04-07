package it.polimi.ingsw.am11.decks.playable;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.playable.GoldCard;
import it.polimi.ingsw.am11.cards.utils.CornerContainer;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.cards.utils.enums.PointsRequirementsType;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DatabaseConstants;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class GoldDeckFactory {

    public static final String queryStatement = "SELECT * FROM playable_cards WHERE card_type = 'GOLD'";
    public static final String PLACING_REQ_QUERY = "SELECT * FROM placing_requirements WHERE id = ?";

    private GoldDeckFactory() {
    }

    private static CornerContainer getCornerContainer(@NotNull Corner corner,
                                                      @NotNull ResultSet result)
            throws SQLException {
        if (result.isClosed()) {
            throw new RuntimeException("Result set is closed");
        }
        return CornerContainer.of(result.getString(corner.getColumnName()));
    }

    private static void setFrontCorners(GoldCard.Builder cardBuilder,
                                        ResultSet result)
            throws IllegalCardBuildException, SQLException {
        for (Corner corner : Corner.values()) {
            cardBuilder.hasIn(corner, getCornerContainer(corner, result));
        }
    }

    private static void setPlacingRequirements(GoldCard.Builder cardBuilder,
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


    /**
     * Creates a deck of Gold Cards based on the SQLite database.
     *
     * @return A deck of Gold Cards.
     */
    @Contract(" -> new")
    public static @NotNull Deck<GoldCard> createDeck() {
        ImmutableMap.Builder<Integer, GoldCard> builder = ImmutableMap.builder();
        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(queryStatement);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("global_id");
                GoldCard.Builder cardBuilder = new GoldCard.Builder(
                        id,
                        resultSet.getInt("points"),
                        Color.valueOf(resultSet.getString("card_color")));
                setFrontCorners(cardBuilder, resultSet);
                setPlacingRequirements(cardBuilder, resultSet);
                setPointsRequirements(cardBuilder, resultSet);
                builder.put(id, cardBuilder.build());
            }
        } catch (SQLException | IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
        return new Deck<>(builder.build());
    }
}

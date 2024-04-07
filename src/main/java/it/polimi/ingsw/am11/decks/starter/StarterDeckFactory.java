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

    @Contract(" -> new")
    public static @NotNull Deck<StarterCard> createDeck() {
        ImmutableMap.Builder<Integer, StarterCard> builder = ImmutableMap.builder();

        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(queryStatement);
             ResultSet result = statement.executeQuery();
             PreparedStatement centerStatement = connection.prepareStatement(CENTER_QUERY)) {
            while (result.next()) {
                int id = result.getInt("global_id");
                StarterCard.Builder cardBuilder = new StarterCard.Builder(id);

                setFrontRetroCorners(cardBuilder, result);
                setCenterColors(centerStatement, result, cardBuilder);

                builder.put(id, cardBuilder.build());
            }

        } catch (SQLException | IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        return new Deck<>(builder.build());
    }

}

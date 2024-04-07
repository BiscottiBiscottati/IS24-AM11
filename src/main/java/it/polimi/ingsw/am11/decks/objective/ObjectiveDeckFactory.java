package it.polimi.ingsw.am11.decks.objective;

import com.google.common.collect.ImmutableMap;
import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.decks.Deck;
import it.polimi.ingsw.am11.decks.utils.DatabaseConstants;
import it.polimi.ingsw.am11.exceptions.IllegalCardBuildException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class ObjectiveDeckFactory {

    private static final String collectingStatement = "SELECT * FROM item_collect_cards WHERE card_type = ?";
    private static final String positioningStatement = "SELECT * FROM positioning_cards WHERE card_type = ?";

    private ObjectiveDeckFactory() {
    }

    @Contract(" -> new")
    public static @NotNull Deck<ObjectiveCard> createDeck() {
        ImmutableMap.Builder<Integer, ObjectiveCard> builder = new ImmutableMap.Builder<>();

        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement collectingStatement = connection.prepareStatement(
                     ObjectiveDeckFactory.collectingStatement);
             PreparedStatement positioningStatement = connection.prepareStatement(
                     ObjectiveDeckFactory.positioningStatement)) {
            setCollectingCards(collectingStatement, builder);
            setPositioningCards(positioningStatement, builder);

        } catch (SQLException | IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        return new Deck<>(builder.build());
    }

    private static void setPositioningCards(@NotNull PreparedStatement positioningStatement,
                                            ImmutableMap.Builder<Integer, ObjectiveCard> builder)
            throws SQLException, IllegalCardBuildException {
        positioningStatement.setString(1, "L_SHAPE");
        try (ResultSet resultSet = positioningStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("global_id");
                LCard.Builder cardBuilder = new LCard.Builder(
                        id,
                        resultSet.getInt("points")
                );
                cardBuilder.isFlipped(resultSet.getBoolean("is_flipped"));
                cardBuilder.isRotated(resultSet.getBoolean("is_rotated"));
                cardBuilder.hasPrimaryColor(Color.valueOf(resultSet.getString("primary_color")));
                cardBuilder.hasSecondaryColor(Color.valueOf(resultSet.getString("secondary_color")));
                builder.put(id, cardBuilder.build());
            }
        }
        positioningStatement.setString(1, "TRIPLET");
        try (ResultSet resultSet = positioningStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("global_id");
                TripletCard.Builder cardBuilder = new TripletCard.Builder(
                        id,
                        resultSet.getInt("points")
                );
                cardBuilder.isFlipped(resultSet.getBoolean("is_flipped"));
                cardBuilder.hasColor(Color.valueOf(resultSet.getString("primary_color")));
                builder.put(id, cardBuilder.build());
            }
        }
    }

    private static void setCollectingCards(@NotNull PreparedStatement collectingStatement,
                                           ImmutableMap.Builder<Integer, ObjectiveCard> builder)
            throws SQLException, IllegalCardBuildException {
        collectingStatement.setString(1, "COLOR_COLLECT");
        try (ResultSet resultSet = collectingStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("global_id");
                ColorCollectCard.Builder cardBuilder = new ColorCollectCard.Builder(
                        id,
                        resultSet.getInt("points")
                );

                for (Color color : Color.values()) {
                    cardBuilder.hasColor(color, resultSet.getInt(color.getColumnName()));
                }
                builder.put(id, cardBuilder.build());
            }
        }
        collectingStatement.setString(1, "SYMBOL_COLLECT");
        try (ResultSet resultSet = collectingStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("global_id");
                SymbolCollectCard.Builder cardBuilder = new SymbolCollectCard.Builder(
                        id,
                        resultSet.getInt("points")
                );

                for (Symbol symbol : Symbol.values()) {
                    cardBuilder.hasSymbol(symbol, resultSet.getInt(symbol.getColumnName()));
                }
                builder.put(id, cardBuilder.build());
            }
        }
    }

}

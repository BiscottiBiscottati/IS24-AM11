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
import java.util.Arrays;


public class ResourceDeckFactory {

    public static final String queryStatement = "SELECT * FROM playable_cards WHERE card_type = 'RESOURCE'";

    private ResourceDeckFactory() {
    }

    private static CornerContainer getCornerContainer(
            @NotNull Corner corner,
            @NotNull ResultSet result) {
        try {
            return CornerContainer.of(result.getString(corner.getColumnName()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setFrontCorners(ResourceCard.Builder cardBuilder, ResultSet result) {
        Arrays.stream(Corner.values())
              .forEach(corner -> cardBuilder.hasIn(corner, getCornerContainer(corner, result)));
    }

    @Contract(" -> new")
    public static @NotNull Deck<ResourceCard> createDeck() {
        ImmutableMap.Builder<Integer, ResourceCard> builder = new ImmutableMap.Builder<>();

        try (Connection connection = DriverManager.getConnection(DatabaseConstants.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement(queryStatement);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("global_id");
                ResourceCard.Builder cardBuilder = new ResourceCard.Builder(
                        id,
                        resultSet.getInt("points"),
                        Color.valueOf(resultSet.getString("card_color"))
                );
                setFrontCorners(cardBuilder, resultSet);
                builder.put(id, cardBuilder.build());
            }
        } catch (SQLException | IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }

        return new Deck<>(builder.build());
    }
}

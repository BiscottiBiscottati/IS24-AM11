package it.polimi.ingsw.am11.decks;


import it.polimi.ingsw.am11.decks.utils.DeckType;
import it.polimi.ingsw.am11.decks.utils.PlayableDeckType;
import it.polimi.ingsw.am11.decks.utils.UtilitiesDeckType;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToDriverManagerGetConnection")
public class DBConnectionManager implements AutoCloseable {
    private final static String sqlitePrefix = "jdbc:sqlite:";
    private final Connection connection;
    private final Statement statement;
    private final List<String> tablesToQuery;
    private ResultSet result;

    public DBConnectionManager() {
        try {
            URL dbStream = this.getClass().getResource("/db/cards.sqlite");
            if (dbStream == null) {
                throw new RuntimeException("Database not found!");
            }
            this.connection = DriverManager.getConnection(sqlitePrefix + dbStream);
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed!");
        }
        tablesToQuery = new ArrayList<>(2);
    }

    public void selectType(@NotNull DeckType deckType) {
        tablesToQuery.clear();
        switch (deckType) {
            case PlayableDeckType.GOLD:
                tablesToQuery.add("playable_cards WHERE card_type = 'GOLD'");
            case PlayableDeckType.RESOURCE:
                tablesToQuery.add("playable_cards WHERE card_type = 'RESOURCE'");
            case UtilitiesDeckType.OBJECTIVE:
                tablesToQuery.add("item_collect_cards");
                tablesToQuery.add("positioning_cards");
            case UtilitiesDeckType.STARTER:
                tablesToQuery.add("starter_cards");
        }
    }

    @Override
    public void close() throws Exception {
        if (result != null) {
            result.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}

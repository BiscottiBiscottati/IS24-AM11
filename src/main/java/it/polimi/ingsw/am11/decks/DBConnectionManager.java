package it.polimi.ingsw.am11.decks;


import it.polimi.ingsw.am11.decks.utils.DeckType;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectionManager implements AutoCloseable {
    private final static String sqlitePrefix = "jdbc:sqlite:";

    private final Connection connection;

    public DBConnectionManager() {

        try {
            InputStream dbStream = getClass().getResourceAsStream("/db/cards.db");
            if (dbStream == null) {
                throw new RuntimeException("Database not found!");
            }
            this.connection = DriverManager.getConnection(sqlitePrefix + dbStream);

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed!");
        }
    }

    public void getCards(DeckType deckType) {
        try {
            Statement statement = this.connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get statement from connection!");
        } finally {
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}

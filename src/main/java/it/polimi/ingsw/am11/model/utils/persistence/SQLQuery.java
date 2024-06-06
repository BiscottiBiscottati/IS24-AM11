package it.polimi.ingsw.am11.model.utils.persistence;

public class SQLQuery {
    public static final String CREATE_SAVES_TABLE = """
            CREATE TABLE IF NOT EXISTS saves (
                id INTEGER PRIMARY KEY,
                save_time TEXT NOT NULL,
                save_data TEXT NOT NULL
            );
            """;

    public static final String CREATE_CUSTOM_TABLE = """
            CREATE TABLE IF NOT EXISTS custom_saves (
                id INTEGER PRIMARY KEY,
                save_name TEXT NOT NULL DEFAULT 'no_name',
                save_time TEXT NOT NULL,
                save_data TEXT NOT NULL
            );
            """;

    public static final String INSERT_SAVE = """
            INSERT INTO saves (save_time, save_data)
            VALUES (?, ?);
            """;

    public static final String SELECT_MOST_RECENT = """
            SELECT save_data
            FROM saves
            ORDER BY save_time DESC
            LIMIT 1;
            """;

    public static final String TRIGGER_TO_DELETE_OLDER = """
            CREATE TRIGGER IF NOT EXISTS delete_old_saves
            AFTER INSERT ON saves
            WHEN (SELECT COUNT(*) FROM saves) > 10
            BEGIN
                DELETE FROM saves
                WHERE id NOT IN (
                    SELECT id
                    FROM saves
                    ORDER BY save_time DESC
                    LIMIT 10
                );
            END;
            """;

    public static final String DELETE_ALL_SAVES = """
            DELETE FROM saves;
            """;

    public static final String DELETE_ALL_CUSTOM = """
            DELETE FROM custom_saves;
            """;

}

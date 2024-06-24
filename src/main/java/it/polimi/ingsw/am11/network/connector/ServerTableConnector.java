package it.polimi.ingsw.am11.network.connector;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.network.RMI.server.game.ServerConnectorImpl;
import it.polimi.ingsw.am11.network.socket.server.game.ServerGameSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

/**
 * Interface that defines the methods that the server can use to update the table.
 * <p>
 * The server will use these methods to send updates viewable by everyone to the clients.
 * <p>
 * Each player should provide an implementation of this interface to receive updates from the
 * server.
 *
 * @see ServerConnectorImpl
 * @see ServerGameSender
 */
public interface ServerTableConnector {

    void updateDeckTop(@NotNull PlayableCardType type, Color color);

    void updateField(@NotNull String nickname, int x, int y, int cardId, boolean isRetro);

    void updateShownPlayable(@Nullable Integer previousId, @Nullable Integer currentId);

    void updateTurnChange(@NotNull String nickname);

    void updatePlayerPoint(@NotNull String nickname, int points);

    void updateGameStatus(@NotNull GameStatus status);

    void updateCommonObjective(@NotNull Set<Integer> cardsId, boolean removeMode);

    void sendFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard);

    void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers);

    void updateNumOfPlayers(@NotNull Integer numOfPlayers);
}

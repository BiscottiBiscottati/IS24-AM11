package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.table.GameStatus;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public interface TableConnector {

    void updateDeckTop(PlayableCardType type, Color color);

    void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                     boolean removeMode);

    void updateShownPlayable(Integer previousId, Integer currentId);

    void updateTurnChange(String nickname);

    void updatePlayerPoint(String nickname, int points);

    void updateGameStatus(GameStatus status);

    void updateCommonObjective(Set<Integer> cardsId, boolean removeMode);

    void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard);

    void updatePlayers(EnumMap<PlayerColor, String> currentPlayers, String newPlayer);

}

package it.polimi.ingsw.am11.network;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;

public interface TableConnector {

    void updateDeckTop(PlayableCardType type, Color color);

    void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                     boolean removeMode);

    void updateShownPlayable(Integer previousId, Integer currentId);

    void updateTurnChange(String nickname);

    void updatePlayerPoint(String nickname, int points);

    void updateGameStatus(GameStatus status);

    void updateCommonObjective(int cardId, boolean removeMode);
}

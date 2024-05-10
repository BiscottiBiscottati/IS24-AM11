package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.network.PlayerConnector;
import it.polimi.ingsw.am11.network.TableConnector;

import java.rmi.Remote;
import java.util.Map;
import java.util.Set;

public interface ConnectorInterface extends Remote, PlayerConnector, TableConnector {

    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);

    void sendStarterCard(int cardId);

    void sendCandidateObjective(Set<Integer> cardsId);

    void updateDeckTop(PlayableCardType type, Color color);

    void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                     boolean removeMode);

    void updateShownPlayable(Integer previousId, Integer currentId);

    void updateTurnChange(String nickname);

    void updatePlayerPoint(String nickname, int points);

    void updateGameStatus(GameStatus status);

    void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard);

    void updateCommonObjective(int cardId, boolean removeMode);
}

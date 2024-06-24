package it.polimi.ingsw.am11.view.client.GUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface GuiObserver {
    void updateDeckTop(PlayableCardType type, Color color);

    void updateField(String nickname, int x, int y, int cardId, boolean isRetro);

    void updateShownPlayable(Integer previousId, Integer currentId);

    void updateTurnChange(String nickname);

    void updatePlayerPoint(String nickname, int points);

    void updateGameStatus(GameStatus status) throws IOException;

    void updateCommonObjective(Set<Integer> cardId, boolean removeMode);

    void receiveFinalLeaderboard(Map<String, Integer> finalLeaderboard);

    void throwException(Exception ex);

    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);


    void receiveStarterCard(int cardId);

    void receiveCandidateObjective(Set<Integer> cardId);

    void notifyGodPlayer();

    void updatePlayers(Map<PlayerColor, String> currentPlayers);

    void updateNumOfPlayers(int numOfPlayers);

    void disconnectedFromServer();
}

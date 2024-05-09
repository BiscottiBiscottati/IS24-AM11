package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;

import java.util.Set;

public interface ClientViewUpdater {

    void updateDeckTop(PlayableCardType type, Color color);


    void updateField(String nickname, int x, int y, int cardId, boolean removeMode);


    void updateShownPlayable(int previousId, int currentId);


    void updateTurnChange(String nickname);


    void updatePlayerPoint(String nickname, int points);


    void updateGameStatus(GameStatus status);


    void updateCommonObjective(int cardId, boolean removeMode);

    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);

    void receiveStarterCard(int cardId);

    void receiveCandidateObjective(Set<Integer> cardId);
}

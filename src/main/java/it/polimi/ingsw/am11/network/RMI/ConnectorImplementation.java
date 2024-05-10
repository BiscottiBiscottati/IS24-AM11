package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;

import java.util.Map;
import java.util.Set;

public class ConnectorImplementation implements ConnectorInterface {

    public void updateHand(int cardId, boolean removeMode) {
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
    }

    @Override
    public void sendStarterCard(int cardId) {
    }

    @Override
    public void sendCandidateObjective(Set<Integer> cardsId) {
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
    }

    @Override
    public void updateTurnChange(String nickname) {
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
    }

    @Override
    public void updateGameStatus(GameStatus status) {
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
    }

    @Override
    public void updateCommonObjective(int cardId, boolean removeMode) {
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardsId, boolean removeMode) {
    }
}

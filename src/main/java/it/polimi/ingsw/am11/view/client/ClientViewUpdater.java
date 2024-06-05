package it.polimi.ingsw.am11.view.client;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

public interface ClientViewUpdater {

    void updateDeckTop(@NotNull PlayableCardType type, Color color);

    void updateField(@NotNull String nickname, int x, int y, int cardId,
                     boolean isRetro, boolean removeMode);

    void updateShownPlayable(Integer previousId, Integer currentId);

    void updateTurnChange(@NotNull String nickname);

    void updatePlayerPoint(@NotNull String nickname, int points);

    void updateGameStatus(@NotNull GameStatus status);

    void updateCommonObjective(@NotNull Set<Integer> cardId, boolean removeMode);

    void receiveFinalLeaderboard(@NotNull Map<String, Integer> finalLeaderboard);

    void updateHand(int cardId, boolean removeMode);

    void updatePersonalObjective(int cardId, boolean removeMode);

    void receiveStarterCard(int cardId);

    void receiveCandidateObjective(Set<Integer> cardId);

    void notifyGodPlayer();

    void updatePlayers(@NotNull SequencedMap<PlayerColor, String> currentPlayers);

    void updateNumOfPlayers(int numOfPlayers);

    void disconnectedFromServer(@NotNull String message);

    @NotNull
    ExceptionThrower getExceptionThrower();

    @NotNull
    ClientChatUpdater getChatUpdater();
}

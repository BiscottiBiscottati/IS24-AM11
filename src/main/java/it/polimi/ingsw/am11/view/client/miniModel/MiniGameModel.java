package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MiniGameModel {
    private final Map<String, CliPlayer> playerMap;
    private final CliTable table;
    private Map<String, Integer> finalLeaderboard;
    private String currentTurn;
    private String godPlayer;
    private String myName;


    public MiniGameModel() {
        this.playerMap = new HashMap();
        this.table = new CliTable();
        this.finalLeaderboard = null;
    }

    public CliPlayer getCliPlayer(String nickname) {
        return playerMap.get(nickname);
    }

    public Set<String> getplayers() {
        return playerMap.keySet();
    }

    public void setMyName(String nick) {
        myName = nick;
    }

    public String myName() {
        return myName;
    }

    public String getGodPlayer() {
        return godPlayer;
    }

    public void setGodPlayer(String godPlayer) {
        this.godPlayer = godPlayer;
    }

    public void addPlayer(String nickname, PlayerColor color) {
        playerMap.put(nickname, new CliPlayer(nickname, color));
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String nickname) {
        currentTurn = nickname;
    }

    public CliTable table() {
        return table;
    }

    public void addPoints(String nickname, int val) {
        playerMap.get(nickname).addPoints(val);
    }

    public int getPoints(String nickname) {
        return playerMap.get(nickname).getPoints();
    }

    public void setFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        this.finalLeaderboard = finalLeaderboard;
    }

    public @Nullable Integer getFinalPosition(String nickname) {
        if (finalLeaderboard != null) {
            return finalLeaderboard.get(nickname);
        }
        return null;
    }

    public void addCardInHand(int cardId) {
        playerMap.get(myName).getSpace().addCardInHand(cardId);
    }

    public void removeCardFromHand(int cardId) {
        playerMap.get(myName).getSpace().removeCardFromHand(cardId);
    }

    public void addPersonalObjective(int cardId) {
        playerMap.get(myName).getSpace().addPersonalObjective(cardId);

    }

    public void rmPersonalObjective(int cardId) {
        playerMap.get(myName).getSpace().rmPersonalObjective(cardId);

    }

    public void addStarterCard(int cardId) {
        playerMap.get(myName).getSpace().setStarterCard(cardId);
    }

    public void addCandidateObjectives(Set<Integer> candidates) {
        playerMap.get(myName).getSpace().addCandidateObjectives(candidates);
    }

    public void place(String nickname, Position pos, int cardId, boolean isRetro) {
        playerMap.get(nickname).getField().place(pos, cardId, isRetro);
    }

    public void removePlaced(String nickname, Position pos) {
        playerMap.get(nickname).getField().remove(pos);
    }
}

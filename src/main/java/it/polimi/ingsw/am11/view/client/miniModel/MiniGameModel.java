package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.utils.TurnAction;
import it.polimi.ingsw.am11.model.utils.memento.ReconnectionModelMemento;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This is used to store the state of the game in the client, it is a stripped down verion of the
 * GameLogic class, it is used to store only the necessary information to display the game on the
 * client side, it has no logic.
 */
public class MiniGameModel {
    private final @NotNull List<String> chatMessages;
    private final @NotNull SequencedMap<String, CliPlayer> playerMap;
    private final @NotNull CliTable table;
    private Map<String, Integer> finalLeaderboard;
    private String currentTurn;
    private String godPlayer;
    private String myName;
    private boolean iPlaced;
    private String startingPlayer;


    public MiniGameModel() {
        this.playerMap = new LinkedHashMap<>(8);
        this.table = new CliTable();
        this.finalLeaderboard = new HashMap<>(8);
        this.iPlaced = false;
        this.currentTurn = "";
        this.godPlayer = "";
        this.chatMessages = new ArrayList<>(8);
        this.startingPlayer = "";
    }

    public CliPlayer getCliPlayer(String nickname) {
        return playerMap.get(nickname);
    }

    public SequencedSet<String> getPlayers() {
        return playerMap.sequencedKeySet();
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

    public @Nullable String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(@NotNull String nickname) {
        currentTurn = nickname;
    }

    public @NotNull CliTable table() {
        return table;
    }

    public @Nullable Map<String, Integer> getFinalLeaderboard() {
        return finalLeaderboard;
    }

    public void setFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        this.finalLeaderboard = finalLeaderboard;
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

    public void load(@NotNull ReconnectionModelMemento memento) {
        currentTurn = memento.playerManager().currentPlayer() == null ? "" :
                      memento.playerManager().currentPlayer();
        startingPlayer = memento.playerManager().firstPlayer() == null ? "" :
                         memento.playerManager().firstPlayer();

        table.load(memento.table(), memento.plateau());
        finalLeaderboard.clear();
        finalLeaderboard.putAll(memento.plateau().leaderboard());
        playerMap.clear();
        memento.playerManager().players().forEach((player) -> {
            CliPlayer cliPlayer = new CliPlayer(player.nickname(), player.color());
            cliPlayer.load(player);
            cliPlayer.addPoints(memento.plateau().playerPoints().get(player.nickname()));
            playerMap.put(player.nickname(), cliPlayer);
        });
        iPlaced = memento.playerManager().currentAction().equals(TurnAction.DRAW_CARD);
    }

    public boolean getiPlaced() {
        return iPlaced;
    }

    public void addChatMessage(String message) {
        chatMessages.add(message);
    }

    public @NotNull List<String> getChatMessages() {
        return chatMessages;
    }

    public void setIPlaced(boolean iPlaced) {
        this.iPlaced = iPlaced;
    }

    public String getStartingPlayer() {
        return startingPlayer;
    }

}

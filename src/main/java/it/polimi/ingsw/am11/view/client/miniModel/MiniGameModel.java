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

    /**
     * Retrieves a CliPlayer object from the playerMap using the provided nickname as the key.
     *
     * @param nickname The nickname of the player. This is used as the key to retrieve the CliPlayer
     *                 object from the playerMap.
     * @return The CliPlayer object associated with the provided nickname. If no such player exists
     * in the map, this method will return null.
     */
    public CliPlayer getCliPlayer(String nickname) {
        return playerMap.get(nickname);
    }

    /**
     * Retrieves a set of player nicknames from the playerMap.
     *
     * @return A SequencedSet of player nicknames.
     */
    public SequencedSet<String> getPlayers() {
        return playerMap.sequencedKeySet();
    }

    /**
     * Sets the name of the current player.
     *
     * @param nick The nickname of the player. This nickname will be used as the player's name in
     *             the game.
     */
    public void setMyName(String nick) {
        myName = nick;
    }

    /**
     * Retrieves the name of the current player.
     *
     * @return The name of the current player.
     */
    public String myName() {
        return myName;
    }

    /**
     * Retrieves the name of the god player.
     *
     * @return The name of the god player.
     */
    public String getGodPlayer() {
        return godPlayer;
    }

    /**
     * Sets the name of the god player.
     *
     * @param godPlayer The name of the god player. This name will be used to identify the god
     *                  player in the game.
     */
    public void setGodPlayer(String godPlayer) {
        this.godPlayer = godPlayer;
    }

    /**
     * Adds a new player to the game.
     *
     * @param nickname The nickname of the player. This nickname will be used as the player's
     *                 identifier in the game.
     * @param color    The color associated with the player. This color will be used for player's
     *                 representation in the game.
     */
    public void addPlayer(String nickname, PlayerColor color) {
        playerMap.put(nickname, new CliPlayer(nickname, color));
    }

    /**
     * Retrieves the current turn's player nickname.
     *
     * @return The nickname of the player whose turn it is currently. If no turn is active, this
     * method will return null.
     */
    public @Nullable String getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets the nickname of the player whose turn it is currently.
     *
     * @param nickname The nickname of the player. This nickname will be used to identify the
     *                 current turn's player.
     */
    public void setCurrentTurn(@NotNull String nickname) {
        currentTurn = nickname;
    }

    /**
     * Retrieves the CliTable object representing the game table.
     *
     * @return The CliTable object representing the game table.
     */
    public @NotNull CliTable table() {
        return table;
    }

    /**
     * Retrieves the final leaderboard of the game.
     *
     * @return A Map where the keys are the nicknames of the players and the values are their final
     * scores. If the final leaderboard is not set, this method will return null.
     */
    public @Nullable Map<String, Integer> getFinalLeaderboard() {
        return finalLeaderboard;
    }

    /**
     * Sets the final leaderboard of the game.
     *
     * @param finalLeaderboard A Map where the keys are the nicknames of the players and the values
     *                         are their final scores.
     */
    public void setFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        this.finalLeaderboard = finalLeaderboard;
    }

    /**
     * Adds a card to the current player's hand.
     *
     * @param cardId The ID of the card to be added to the player's hand.
     */
    public void addCardInHand(int cardId) {
        playerMap.get(myName).getSpace().addCardInHand(cardId);
    }

    /**
     * Removes a card from the current player's hand.
     *
     * @param cardId The ID of the card to be removed from the player's hand.
     */
    public void removeCardFromHand(int cardId) {
        playerMap.get(myName).getSpace().removeCardFromHand(cardId);
    }

    /**
     * Adds a personal objective card to the current player's hand.
     *
     * @param cardId The ID of the personal objective card to be added to the player's hand.
     */
    public void addPersonalObjective(int cardId) {
        playerMap.get(myName).getSpace().addPersonalObjective(cardId);
    }

    /**
     * Removes a personal objective card from the current player's hand.
     *
     * @param cardId The ID of the personal objective card to be removed from the player's hand.
     */
    public void rmPersonalObjective(int cardId) {
        playerMap.get(myName).getSpace().rmPersonalObjective(cardId);
    }

    /**
     * Adds a starter card to the current player's hand.
     *
     * @param cardId The ID of the starter card to be added to the player's hand.
     */
    public void addStarterCard(int cardId) {
        playerMap.get(myName).getSpace().setStarterCard(cardId);
    }

    /**
     * Adds a set of candidate objectives to the current player's hand.
     *
     * @param candidates The set of IDs of the candidate objectives to be added to the player's
     *                   hand.
     */
    public void addCandidateObjectives(Set<Integer> candidates) {
        playerMap.get(myName).getSpace().addCandidateObjectives(candidates);
    }

    /**
     * Loads the state of the game from a ReconnectionModelMemento object.
     *
     * @param memento The ReconnectionModelMemento object containing the state of the game to be
     *                loaded.
     */
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

    /**
     * Retrieves the placement status of the current player.
     *
     * @return The placement status of the current player. If the player has placed, this method
     * will return true. Otherwise, it will return false.
     */
    public boolean getiPlaced() {
        return iPlaced;
    }

    /**
     * Adds a new message to the chatMessages list.
     *
     * @param message The message to be added to the chatMessages list.
     */
    public void addChatMessage(String message) {
        chatMessages.add(message);
    }

    /**
     * Retrieves the list of chat messages.
     *
     * @return A List of Strings representing the chat messages.
     */
    public @NotNull List<String> getChatMessages() {
        return chatMessages;
    }

    /**
     * Sets the placement status of the current player.
     *
     * @param iPlaced The placement status to be set for the current player. If the player has
     *                placed, this should be true. Otherwise, it should be false.
     */
    public void setIPlaced(boolean iPlaced) {
        this.iPlaced = iPlaced;
    }

    /**
     * Retrieves the name of the starting player.
     *
     * @return The name of the starting player.
     */
    public String getStartingPlayer() {
        return startingPlayer;
    }

}

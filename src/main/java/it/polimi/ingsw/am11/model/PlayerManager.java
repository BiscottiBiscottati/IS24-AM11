package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PersonalSpace;
import it.polimi.ingsw.am11.players.Player;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.field.PlayerField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerManager {
    private static int maxNumberOfPlayers;
    private final Map<String, Player> players;
    private final LinkedList<Player> playerQueue;
    private Player firstPlayer;
    private Player currentPlaying;

    public PlayerManager() {
        this.players = new HashMap<>(8);
        this.playerQueue = new LinkedList<>();
    }

    public static void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        PlayerManager.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public static int getMaxNumberOfPlayer() {
        return PlayerManager.maxNumberOfPlayers;
    }

    public Set<String> getPlayers() {
        return players.keySet();
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public @Nullable String getCurrentTurnPlayer() {
        return (currentPlaying != null) ? currentPlaying.nickname() : null;
    }

    public @Nullable String getFirstPlayer() {
        return (firstPlayer != null) ? firstPlayer.nickname() : null;
    }

    public @NotNull Player getPlayer(String nickname) throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player;
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    public Set<Integer> getHand(String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerHand()
                      .stream()
                      .map(PlayableCard::getId)
                      .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Integer> getPlayerObjective(@NotNull String nickname) {
        return players.get(nickname)
                      .space()
                      .getPlayerObjective()
                      .stream()
                      .map(ObjectiveCard::getId)
                      .collect(Collectors.toUnmodifiableSet());
    }

    public @Nullable PlayerColor getPlayerColor(@NotNull String nickname) {
        Player player = players.get(nickname);
        return (player != null) ? player.color() : null;
    }

    public Player addPlayerToTable(@NotNull String nickname, @NotNull PlayerColor colour)
    throws PlayerInitException {
        if (players.containsKey(nickname)) {
            throw new PlayerInitException(nickname + " is already in use");
        } else if (players.values()
                          .stream()
                          .map(Player::color)
                          .anyMatch(playerColor -> playerColor.equals(colour))) {
            throw new PlayerInitException(
                    "Colour already in use: " + colour
            );
        } else if (players.size() >= maxNumberOfPlayers) {
            throw new PlayerInitException(
                    "You are trying to add too many players, the limit is " +
                    maxNumberOfPlayers
            );
        } else {
            Player newPlayer = new Player(nickname, colour);
            players.put(nickname, newPlayer);
            return newPlayer;
        }
    }

    public void removePlayer(String nickname) {
        players.remove(nickname);
    }

    public void startingTheGame() {
        playerQueue.addAll(players.values());
        Collections.shuffle(playerQueue);
        firstPlayer = playerQueue.getFirst();
        currentPlaying = playerQueue.removeFirst();
    }

    public boolean isFirstTheCurrent() {
        return firstPlayer == currentPlaying;
    }

    public void goNextTurn() {
        playerQueue.addLast(currentPlaying);
        currentPlaying = playerQueue.removeFirst();
    }

    public void resetAll() {
        playerQueue.clear();
        players.values()
               .stream()
               .map(Player::space)
               .forEach(PersonalSpace::clearAll);
        players.values()
               .stream()
               .map(Player::field)
               .forEach(PlayerField::clearAll);
    }
}

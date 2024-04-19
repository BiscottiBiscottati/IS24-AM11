package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.players.PersonalSpace;
import it.polimi.ingsw.am11.players.Player;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import it.polimi.ingsw.am11.players.field.PlayerField;
import org.jetbrains.annotations.NotNull;

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

    public Optional<String> getCurrentTurnPlayer() {
        return Optional.ofNullable(currentPlaying)
                       .map(Player::nickname);
    }

    public Optional<String> getFirstPlayer() {
        return Optional.ofNullable(firstPlayer)
                       .map(Player::nickname);
    }

    public Optional<Player> getPlayer(String nickname) {
        return Optional.ofNullable(players.get(nickname));
    }

    public Set<Integer> getHand(String nickname) throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space()
                         .getPlayerHand()
                         .stream()
                         .map(PlayableCard::getId)
                         .collect(Collectors.toUnmodifiableSet());
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    public Set<Integer> getPlayerObjective(@NotNull String nickname) throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space()
                         .getPlayerObjective()
                         .stream()
                         .map(ObjectiveCard::getId)
                         .collect(Collectors.toUnmodifiableSet());
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }

    }

    public Optional<StarterCard> getStarterCard(@NotNull String nickname)
    throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space().getStarterCard();
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    public Set<ObjectiveCard> getCandidateObjectives(@NotNull String nickname)
    throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space().getCandidateObjectives();
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    public ObjectiveCard getCandidateObjectiveByID(@NotNull String nickname, int id)
    throws PlayerInitException, IllegalPlayerSpaceActionException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space().getCandidateObjectiveByID(id);
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    public void setStarterCard(@NotNull String nickname, @NotNull StarterCard starter)
    throws PlayerInitException, IllegalPlayerSpaceActionException {
        Player player = players.get(nickname);
        if (player != null) {
            player.space().setStarterCard(starter);
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    public void setNewCandidateObjective(@NotNull String nickname, @NotNull ObjectiveCard objective)
    throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            player.space().setNewCandidateObjectives(objective);
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }


    public Optional<PlayerColor> getPlayerColor(@NotNull String nickname) {
        return Optional.ofNullable(players.get(nickname))
                       .map(Player::color);
    }

    public Set<Position> getAvailablePositions(@NotNull String nickname)
    throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.field().getAvailablePositions();
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
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

    public boolean areTheyReady() {
        boolean isReady = true;
        for (Player player : players.values()) {
            isReady = isReady &&
                      player.space().areObjectiveAll() &&
                      ! player.field().isAvailable(new Position(0, 0));
        }
        return isReady;
    }
}

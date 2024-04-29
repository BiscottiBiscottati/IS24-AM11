package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.playable.PlayableCard;
import it.polimi.ingsw.am11.model.cards.starter.StarterCard;
import it.polimi.ingsw.am11.model.exceptions.IllegalPlayerSpaceActionException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;
import it.polimi.ingsw.am11.model.players.PersonalSpace;
import it.polimi.ingsw.am11.model.players.Player;
import it.polimi.ingsw.am11.model.players.field.PlayerField;
import it.polimi.ingsw.am11.model.players.utils.PlayerColor;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.PlayerViewUpdater;
import it.polimi.ingsw.am11.view.events.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.TurnChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerManager {
    private static int maxNumberOfPlayers;
    private final Map<String, Player> players;
    private final Queue<Player> playerQueue;
    private final PropertyChangeSupport pcs;
    private Player firstPlayer;
    private Player currentPlaying;

    public PlayerManager() {
        this.players = new HashMap<>(8);
        this.playerQueue = new ArrayDeque<>(maxNumberOfPlayers);
        this.pcs = new PropertyChangeSupport(this);
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
    throws PlayerInitException, NumOfPlayersException {
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
            throw new NumOfPlayersException(
                    "You are trying to add too many players, the limit is " +
                    maxNumberOfPlayers
            );
        } else {
            Player newPlayer = new Player(nickname, colour);
            players.put(nickname, newPlayer);
            playerQueue.add(newPlayer);
            return newPlayer;
        }
    }

    public void removePlayer(String nickname) {
        Player toRemove = players.get(nickname);
        if (toRemove == null) {
            return;
        }
        playerQueue.remove(toRemove);
        players.remove(nickname);
    }

    public void startingTheGame() {
        int randomIndex = new Random().nextInt(playerQueue.size());
        firstPlayer = players.values()
                             .toArray(new Player[playerQueue.size()])[randomIndex];

        Player peeked = playerQueue.element();
        while (peeked != firstPlayer) {
            playerQueue.offer(playerQueue.poll());
            peeked = playerQueue.element();
        }
        currentPlaying = playerQueue.element();
        pcs.firePropertyChange(new TurnChangeEvent(List.copyOf(playerQueue),
                                                   null,
                                                   currentPlaying.nickname()));
    }

    public boolean isFirstTheCurrent() {
        return firstPlayer == currentPlaying;
    }

    public void goNextTurn() {
        String previousPlayer = currentPlaying.nickname();
        playerQueue.remove();
        playerQueue.offer(currentPlaying);
        currentPlaying = playerQueue.element();
        currentPlaying.space().setCardBeenPlaced(false);
        pcs.firePropertyChange(new TurnChangeEvent(List.copyOf(playerQueue),
                                                   previousPlayer,
                                                   currentPlaying.nickname()));
    }

    public void resetAll() {
        players.values()
               .stream()
               .map(Player::space)
               .forEach(PersonalSpace::clearAll);
        players.values()
               .stream()
               .map(Player::field)
               .forEach(PlayerField::clearAll);

        players.forEach((name, player) -> {
            pcs.firePropertyChange(new HandChangeEvent(
                    player.space().getPlayerHand(),
                    name,
                    null,
                    null
            ));
            pcs.firePropertyChange(new FieldChangeEvent(
                    player.field().getCardsPositioned(),
                    name,
                    null,
                    null
            ));
        });
    }

    public boolean areStarterChoosed() {
        boolean isReady = true;
        for (Player player : players.values()) {
            isReady = isReady &&
                      ! player.field().isAvailable(new Position(0, 0));
        }
        return isReady;
    }

    public boolean areObjectiveChoosed() {
        boolean isReady = true;
        for (Player player : players.values()) {
            isReady = isReady &&
                      player.space().areObjectiveGiven();
        }
        return isReady;
    }

    public void addListener(PlayerViewUpdater listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removeListener(PlayerViewUpdater listener) {
        pcs.removePropertyChangeListener(listener);
    }
}

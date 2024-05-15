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
import it.polimi.ingsw.am11.view.events.listeners.PlayerListener;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.player.CandidateObjectiveEvent;
import it.polimi.ingsw.am11.view.events.view.player.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.StarterCardEvent;
import it.polimi.ingsw.am11.view.events.view.table.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.TurnChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerManager {
    private static int maxNumberOfPlayers;
    private final Map<String, Player> players;
    private final Queue<Player> playerQueue;
    private final GameListenerSupport pcs;
    private final Set<Player> unavailablePlayers;
    private Player firstPlayer;
    private Player currentPlaying;

    public PlayerManager() {
        this.players = new HashMap<>(8);
        this.playerQueue = new ArrayDeque<>(maxNumberOfPlayers);
        this.unavailablePlayers = new HashSet<>(8);
        this.pcs = new GameListenerSupport();
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

    public void addUnavailablePlayer(Player player) {
        unavailablePlayers.add(player);
    }

    public void playerIsNowAvailable(Player player) {
        unavailablePlayers.remove(player);
    }

    public void setStarterCard(@NotNull String nickname, @NotNull StarterCard starter)
    throws PlayerInitException, IllegalPlayerSpaceActionException {
        Optional.ofNullable(players.get(nickname))
                .orElseThrow(() -> new PlayerInitException("Player " + nickname + " not found"))
                .space()
                .setStarterCard(starter);

        pcs.fireEvent(new StarterCardEvent(nickname, null, starter.getId()));
    }

    public void setCandidateObjectives(@NotNull String nickname,
                                       @NotNull Set<ObjectiveCard> objectives)
    throws PlayerInitException {
        PersonalSpace space = Optional.ofNullable(players.get(nickname))
                                      .orElseThrow(() -> new PlayerInitException(
                                              "Player " + nickname + " not found"))
                                      .space();

        objectives.forEach(space::setNewCandidateObjectives);

        Set<Integer> candidateObjs = objectives.stream()
                                               .map(ObjectiveCard::getId)
                                               .collect(Collectors.toUnmodifiableSet());
        pcs.fireEvent(new CandidateObjectiveEvent(nickname, null, candidateObjs));
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

    public void chooseFirstPlayer() {
        int randomIndex = new Random().nextInt(playerQueue.size());
        firstPlayer = players.values()
                             .toArray(new Player[playerQueue.size()])[randomIndex];

        Player peeked = playerQueue.element();
        while (peeked != firstPlayer) {
            playerQueue.add(playerQueue.remove());
            peeked = playerQueue.element();
        }
        currentPlaying = playerQueue.element();
        pcs.fireEvent(new TurnChangeEvent(null, currentPlaying.nickname()));
    }

    public boolean isFirstTheCurrent() {
        return firstPlayer == currentPlaying;
    }

    public void goNextTurn() {

        do {
            String previousPlayer = currentPlaying.nickname();
            playerQueue.remove();
            playerQueue.add(currentPlaying);
            currentPlaying = playerQueue.element();
            currentPlaying.space().setCardBeenPlaced(false);

            pcs.fireEvent(new TurnChangeEvent(previousPlayer, currentPlaying.nickname()));
        } while (unavailablePlayers.contains(currentPlaying));
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

        // FIXME we may not need to fire a clear event
        players.forEach((name, player) -> {
            pcs.fireEvent(new HandChangeEvent(
                    name,
                    null,
                    null
            ));
            pcs.fireEvent(new FieldChangeEvent(
                    name,
                    null,
                    null
            ));
        });
    }

    public boolean areStarterSet() {
        boolean isReady = true;
        for (Player player : players.values()) {
            isReady = isReady &&
                      ! player.field().isAvailable(new Position(0, 0));
        }
        return isReady;
    }

    public boolean areObjectiveSet() {
        boolean isReady = true;
        for (Player player : players.values()) {
            isReady = isReady &&
                      player.space().areObjectiveGiven();
        }
        return isReady;
    }


    public void addListener(String player, PlayerListener playerListener) {
        pcs.addListener(player, playerListener);
    }

    public void addListener(TableListener tableListener) {
        pcs.addListener(tableListener);
    }

    public void removeListener(PlayerListener listener) {
        pcs.removeListener(listener);
    }

    public void removeListener(TableListener listener) {
        pcs.removeListener(listener);
    }

}

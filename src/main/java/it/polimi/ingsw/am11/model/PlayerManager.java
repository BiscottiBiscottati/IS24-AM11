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
import it.polimi.ingsw.am11.model.utils.TurnAction;
import it.polimi.ingsw.am11.persistence.memento.PlayerManagerMemento;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.player.CandidateObjectiveEvent;
import it.polimi.ingsw.am11.view.events.view.player.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.StarterCardEvent;
import it.polimi.ingsw.am11.view.events.view.table.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.TurnChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

    private static int maxNumberOfPlayers;
    private final SequencedMap<String, Player> players;
    private final Queue<Player> playerQueue;
    private final GameListenerSupport pcs;
    private final Set<Player> unavailablePlayers;
    private Player firstPlayer;
    private Player currentPlaying;
    private TurnAction currentAction;

    public PlayerManager(GameListenerSupport pcs) {
        this.players = new LinkedHashMap<>(8);
        this.playerQueue = new ArrayDeque<>(maxNumberOfPlayers);
        this.unavailablePlayers = new HashSet<>(8);
        this.firstPlayer = null;
        this.currentPlaying = null;
        this.currentAction = TurnAction.PLACE_CARD;
        this.pcs = pcs;
    }

    public static void setMaxNumberOfPlayers(int maxNumberOfPlayers) {

        LOGGER.debug("MODEL: Setting max number of players to {}", maxNumberOfPlayers);

        PlayerManager.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public static int getMaxNumberOfPlayer() {
        return PlayerManager.maxNumberOfPlayers;
    }

    public SequencedSet<String> getPlayers() {
        return players.sequencedKeySet();
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

    public TurnAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(@NotNull TurnAction action) {
        this.currentAction = action;
    }

    public void reconnectPlayer(Player player) {
        unavailablePlayers.remove(player);
    }

    public boolean isDisconnected(@NotNull String nickname) {
        return unavailablePlayers.parallelStream()
                                 .map(Player::nickname)
                                 .anyMatch(nickname::equals);
    }

    public void disconnectPlayer(Player player) {
        unavailablePlayers.add(player);
    }

    public void setStarterCard(@NotNull String nickname, @NotNull StarterCard starter)
    throws PlayerInitException, IllegalPlayerSpaceActionException {
        Optional.ofNullable(players.get(nickname))
                .orElseThrow(() -> new PlayerInitException("Player " + nickname + " not found"))
                .space()
                .setStarterCard(starter);

        LOGGER.info("MODEL: Starter {} given to {}", starter.getId(), nickname);

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

        LOGGER.info("MODEL: Candidate objectives {} given to {}", candidateObjs, nickname);

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

        for (String item : players.keySet()) {
            if (item.equalsIgnoreCase(nickname)) {
                throw new PlayerInitException(nickname + " is already in use");
            }
        }

        if (players.values()
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

        LOGGER.debug("MODEL: Players are: {}",
                     players.values().stream().map(Player::nickname).toList());

        Player peeked = playerQueue.element();
        while (peeked != firstPlayer) {
            playerQueue.add(playerQueue.remove());
            peeked = playerQueue.element();
        }
        currentPlaying = playerQueue.element();
        currentAction = TurnAction.PLACE_CARD;

        LOGGER.info("MODEL: First player is {}", firstPlayer.nickname());
        LOGGER.debug("MODEL: Current player is {}", currentPlaying.nickname());

        pcs.fireEvent(new TurnChangeEvent(null, currentPlaying.nickname()));
    }

    public boolean isFirstTheCurrent() {
        return firstPlayer == currentPlaying;
    }

    public void goNextTurn() {
        String previousPlayer = currentPlaying.nickname();
        playerQueue.remove();
        playerQueue.add(currentPlaying);
        currentPlaying = playerQueue.element();
        currentPlaying.space().setCardBeenPlaced(false);
        currentAction = TurnAction.PLACE_CARD;

        LOGGER.info("MODEL: Player {} is now playing", currentPlaying.nickname());

        pcs.fireEvent(new TurnChangeEvent(previousPlayer, currentPlaying.nickname()));
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
            LOGGER.debug("MODEL: Clearing player {} hand and field", name);
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
        return players.values().parallelStream()
                      .noneMatch(player -> player.field().isAvailable(Position.of(0, 0)));
    }

    public boolean areObjectiveSet() {
        return players.values().parallelStream()
                      .map(Player::space)
                      .allMatch(PersonalSpace::areObjectiveGiven);
    }

    public boolean isTurnOf(@NotNull String nickname) {
        if (currentPlaying == null) {
            return false;
        }
        return currentPlaying.nickname().equals(nickname);
    }

    public boolean isCurrentDisconnected() {
        return unavailablePlayers.contains(currentPlaying);
    }

    public boolean areAllDisconnected() {
        return unavailablePlayers.size() == players.size();
    }

    public int getNumberOfConnected() {
        return players.size() - unavailablePlayers.size();
    }

    public PlayerManagerMemento save() {
        return new PlayerManagerMemento(
                playerQueue.stream()
                           .map(Player::save)
                           .toList(),
                firstPlayer.nickname(),
                currentPlaying.nickname(),
                currentAction
        );
    }

    public void load(@NotNull PlayerManagerMemento memento) {
        hardReset();

        memento.players().stream()
               .map(Player::load)
               .forEach(player -> {
                   players.put(player.nickname(), player);
                   playerQueue.add(player);
               });

        firstPlayer = players.get(memento.firstPlayer());
        currentPlaying = players.get(memento.currentPlayer());
        currentAction = memento.currentAction();
    }

    public void hardReset() {
        LOGGER.debug("MODEL: Hard reset on player manager");

        players.clear();
        playerQueue.clear();
        unavailablePlayers.clear();
        firstPlayer = null;
        currentPlaying = null;
    }
}

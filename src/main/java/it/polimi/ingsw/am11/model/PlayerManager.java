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
import it.polimi.ingsw.am11.model.utils.memento.PlayerManagerMemento;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.player.CandidateObjectiveEvent;
import it.polimi.ingsw.am11.view.events.view.player.HandChangeEvent;
import it.polimi.ingsw.am11.view.events.view.player.StarterCardEvent;
import it.polimi.ingsw.am11.view.events.view.table.FieldChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.TurnChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlayerManager.class);

    private static int maxNumberOfPlayers;
    private final @NotNull SequencedMap<String, Player> players;
    private final @NotNull Queue<Player> playerQueue;
    private final @NotNull Set<Player> unavailablePlayers;
    private final @NotNull GameListenerSupport pcs;
    private @Nullable Player firstPlayer;
    private @Nullable Player currentPlaying;
    private TurnAction currentAction;

    public PlayerManager(@NotNull GameListenerSupport pcs) {
        this.players = new LinkedHashMap<>(8);
        this.playerQueue = new ArrayDeque<>(maxNumberOfPlayers);
        this.unavailablePlayers = new HashSet<>(8);
        this.firstPlayer = null;
        this.currentPlaying = null;
        this.currentAction = TurnAction.PLACE_CARD;
        this.pcs = pcs;
    }

    /**
     * Set the maximum number of players allowed in the game
     *
     * @param maxNumberOfPlayers the maximum number of players allowed
     */
    public static void setMaxNumberOfPlayers(int maxNumberOfPlayers) {

        LOGGER.debug("MODEL: Setting max number of players to {}", maxNumberOfPlayers);

        PlayerManager.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    /**
     * Get the maximum number of players allowed in the game
     *
     * @return the maximum number of players allowed
     */
    public static int getMaxNumberOfPlayer() {
        return PlayerManager.maxNumberOfPlayers;
    }

    /**
     * Return a SequencedSet of the players in the game, the order depends on the turn order if it
     * has already been set
     *
     * @return a SequencedSet of the players in the game
     */
    public SequencedSet<String> getPlayers() {
        return players.sequencedKeySet();
    }

    /**
     * Get the number of players in the game
     *
     * @return the number of players in the game
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * The name of the player whose turn is currently ongoing, if the game has not started yet this
     * method will return an empty Optional
     *
     * @return the name of the player whose turn is currently ongoing, if present
     */
    public @NotNull Optional<String> getCurrentTurnPlayer() {
        return Optional.ofNullable(currentPlaying)
                       .map(Player::nickname);
    }

    /**
     * If the players are all currently connected
     *
     * @return true if all the players are connected, false otherwise
     */
    public boolean areAllReconnected() {
        return unavailablePlayers.isEmpty();
    }

    /**
     * Get the name of the starting player in the game, if the game has not started yet this method
     * will return an empty Optional
     *
     * @return the name of the starting player in the game, if set
     */
    public @NotNull Optional<String> getFirstPlayer() {
        return Optional.ofNullable(firstPlayer)
                       .map(Player::nickname);
    }

    /**
     * Get the Player class instance of the player with the given nickname, if the player is not
     * present this method will return an empty Optional
     *
     * @param nickname the name of the player to get
     * @return the Player with the given nickname, if present
     */
    public @NotNull Optional<Player> getPlayer(String nickname) {
        return Optional.ofNullable(players.get(nickname));
    }

    /**
     * Get the hand of the player with the given nickname
     *
     * @param nickname the name of the player to get the hand of
     * @return a Set of the ids of the cards in the hand of the player
     * @throws PlayerInitException if the player with the given nickname is not present
     */
    public @NotNull Set<Integer> getHand(String nickname) throws PlayerInitException {
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

    /**
     * Get the personal objective cards of the player with the given nickname
     *
     * @param nickname the name of the player to get the personal objectives of
     * @return a Set of the ids of the personal objectives of the player
     * @throws PlayerInitException if the player with the given nickname is not present
     */
    public @NotNull Set<Integer> getPlayerObjective(@NotNull String nickname)
    throws PlayerInitException {
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

    /**
     * Get the starter card of the player with the given nickname, if the player has not yet chosen
     * a starter card this method will return an empty Optional
     *
     * @param nickname the name of the player to get the starter card of
     * @return the starter card of the player with the given nickname, if present
     * @throws PlayerInitException if the player with the given nickname is not present
     */
    public @NotNull Optional<StarterCard> getStarterCard(@NotNull String nickname)
    throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space().getStarterCard();
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    /**
     * Get the candidate objectives of the player with the given nickname
     *
     * @param nickname the name of the player to get the candidate objectives of
     * @return a Set of the ids of the candidate objectives of the player, empty if the cards have
     * not been dealt yet
     * @throws PlayerInitException if the player with the given nickname is not present
     */
    public Set<ObjectiveCard> getCandidateObjectives(@NotNull String nickname)
    throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space().getCandidateObjectives();
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    /**
     * Get the ObjectiveCard class instance of the candidate objective with the given id of the
     * player with the given nickname
     *
     * @param nickname the name of the player to get the candidate objective of
     * @param id       the id of the candidate objective to get
     * @return the ObjectiveCard with the given id of the player with the given nickname
     * @throws PlayerInitException               if the player with the given nickname is not
     *                                           present
     * @throws IllegalPlayerSpaceActionException if the objective with the given id is not one of
     *                                           the candidate objectives of the player
     */
    public ObjectiveCard getCandidateObjectiveByID(@NotNull String nickname, int id)
    throws PlayerInitException, IllegalPlayerSpaceActionException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.space().getCandidateObjectiveByID(id);
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    /**
     * During a turn the player first have to place a card on the field, then they have to draw one
     * from the ones in the table. This method returns the action that the player has to do in the
     * current turn
     *
     * @return the action that the player has to do in the current turn
     */
    public TurnAction getCurrentAction() {
        return currentAction;
    }

    /**
     * Set the action that the player has to do in the current turn
     *
     * @param action the action that the player has to do in the current turn
     */
    public void setCurrentAction(@NotNull TurnAction action) {
        this.currentAction = action;
    }

    /**
     * Remove the given player from the list of disconnected players
     *
     * @param player the player to reconnect
     */
    public void reconnectPlayer(Player player) {
        unavailablePlayers.remove(player);
    }

    /**
     * Check if the player with the given nickname is currently connected
     *
     * @param nickname the name of the player to check
     * @return true if the player with the given nickname is connected, false otherwise
     */
    public boolean isConnected(@NotNull String nickname) {
        return ! isDisconnected(nickname);
    }

    /**
     * Check if the player with the given nickname is currently disconnected
     *
     * @param nickname the name of the player to check
     * @return true if the player with the given nickname is disconnected, false otherwise
     */
    public boolean isDisconnected(@NotNull String nickname) {
        return unavailablePlayers.parallelStream()
                                 .map(Player::nickname)
                                 .anyMatch(nickname::equals);
    }

    /**
     * Add the given player to the list of disconnected players
     *
     * @param player the player to disconnect
     */
    public void disconnectPlayer(Player player) {
        unavailablePlayers.add(player);
    }

    /**
     * Associate the given starter card to the player with the given nickname
     *
     * @param nickname the name of the player to associate the starter card to
     * @param starter  the starter card to associate to the player
     * @throws PlayerInitException               if the player with the given nickname is not
     *                                           present
     * @throws IllegalPlayerSpaceActionException if the player has already chosen a starter card
     */
    public void setStarterCard(@NotNull String nickname, @NotNull StarterCard starter)
    throws PlayerInitException, IllegalPlayerSpaceActionException {
        Optional.ofNullable(players.get(nickname))
                .orElseThrow(() -> new PlayerInitException("Player " + nickname + " not found"))
                .space()
                .setStarterCard(starter);

        LOGGER.info("MODEL: Starter {} given to {}", starter.getId(), nickname);

        pcs.fireEvent(new StarterCardEvent(nickname, null, starter.getId()));
    }

    /**
     * Associate the given candidate objectives to the player with the given nickname
     *
     * @param nickname   the name of the player to associate the candidate objectives to
     * @param objectives the candidate objectives to associate to the player
     * @throws PlayerInitException if the player with the given nickname is not present
     */
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

    /**
     * Get the PlayerColor of the player with the given nickname, if the player is not present this
     * method will return an empty Optional
     *
     * @param nickname the name of the player to get the color of
     * @return the PlayerColor of the player with the given nickname, if present
     */
    public @NotNull Optional<PlayerColor> getPlayerColor(@NotNull String nickname) {
        return Optional.ofNullable(players.get(nickname))
                       .map(Player::color);
    }

    /**
     * Get the available positions to place a card on the field of the player with the given
     * nickname
     *
     * @param nickname the name of the player to get the available positions of
     * @return a Set of the available positions to place a card on the field of the player
     * @throws PlayerInitException if the player with the given nickname is not present
     */
    public Set<Position> getAvailablePositions(@NotNull String nickname)
    throws PlayerInitException {
        Player player = players.get(nickname);
        if (player != null) {
            return player.field().getAvailablePositions();
        } else {
            throw new PlayerInitException("Player " + nickname + " not found");
        }
    }

    /**
     * Method used when adding a new player to the game, it will add the player to the list of
     * players and to the queue of players.
     *
     * @param nickname the nickname of the player
     * @param colour   the color of the player
     * @throws PlayerInitException   if the nickname is already in use or the colour is already in
     *                               use
     * @throws NumOfPlayersException if the maximum number of players has been reached
     */
    public void addPlayerToTable(@NotNull String nickname, @NotNull PlayerColor colour)
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

        }
    }

    /**
     * Method used when removing a player from the game, it will remove the player from the list of
     * players and from the queue of players.
     *
     * @param nickname of the player to remove
     */
    public void removePlayer(String nickname) {
        Player toRemove = players.get(nickname);
        if (toRemove == null) {
            return;
        }
        playerQueue.remove(toRemove);
        players.remove(nickname);
    }

    /**
     * Method used to set the starting player of the game, it will choose a random player from the
     * list. The player will be set as the first player and the current player.
     */
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

        assert firstPlayer != null;
        LOGGER.info("MODEL: First player is {}", firstPlayer.nickname());
        assert currentPlaying != null;
        LOGGER.debug("MODEL: Current player is {}", currentPlaying.nickname());

        pcs.fireEvent(new TurnChangeEvent(null, currentPlaying.nickname()));
    }

    /**
     * Return if the starting player is the current player, a true also means that a new round has
     * started
     *
     * @return true if the starting player is the current player, false otherwise
     */
    public boolean isFirstTheCurrent() {
        return firstPlayer == currentPlaying;
    }

    /**
     * Method used to continue the game to the next turn, it will put the current player at the end
     * of the queue and, the next player as the current player and remove it from the front of the
     * queue.
     */
    public void goNextTurn() {
        assert currentPlaying != null;
        String previousPlayer = currentPlaying.nickname();
        playerQueue.remove();
        playerQueue.add(currentPlaying);
        currentPlaying = playerQueue.element();
        currentAction = TurnAction.PLACE_CARD;

        LOGGER.info("MODEL: Player {} is now playing", currentPlaying.nickname());

        pcs.fireEvent(new TurnChangeEvent(previousPlayer, currentPlaying.nickname()));
    }

    /**
     * Method used to reset the PlayerManager to the initial state, it will clear all the players
     * hand and field.
     */
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

    /**
     * Used to check if all players have set their starter card, by set it means that they chose to
     * place it on the field on the front or on the retro
     *
     * @return true if all players have set their starter card, false otherwise
     */
    public boolean areStarterSet() {
        return players.values().parallelStream()
                      .noneMatch(player -> player.field().isAvailable(Position.of(0, 0)));
    }

    /**
     * @return true if all players have set their candidate objectives, false otherwise
     */
    public boolean areObjectiveSet() {
        return players.values().parallelStream()
                      .map(Player::space)
                      .allMatch(PersonalSpace::areObjectiveGiven);
    }

    /**
     * Check if the player with the given nickname is the one currently playing
     *
     * @param nickname the name of the player to check
     * @return true if it's the turn of the player with the given nickname, false otherwise
     */
    public boolean isTurnOf(@NotNull String nickname) {
        if (currentPlaying == null) {
            return false;
        }
        return currentPlaying.nickname().equals(nickname);
    }

    /**
     * Check if the player whose turn is currently ongoing is disconnected
     *
     * @return true if the player whose turn is currently ongoing is disconnected, false otherwise
     */
    public boolean isCurrentDisconnected() {
        return unavailablePlayers.contains(currentPlaying);
    }

    /**
     * Check if all players are disconnected
     *
     * @return true if all players are disconnected, false otherwise
     */
    public boolean areAllDisconnected() {
        return unavailablePlayers.size() == players.size();
    }

    /**
     * Return the number of players that are currently connected, a player is considered connected
     * if it is not in the list of disconnected players
     *
     * @return the number of players that are currently connected
     */
    public int getNumberOfConnected() {
        return players.size() - unavailablePlayers.size();
    }

    /**
     * Used to save the state of the PlayerManager
     *
     * @return a PlayerManagerMemento containing the state of the PlayerManager
     */
    public @NotNull PlayerManagerMemento savePublic() {
        return new PlayerManagerMemento(
                playerQueue.stream()
                           .map(Player::save)
                           .toList(),
                firstPlayer != null ? firstPlayer.nickname() : null,
                currentPlaying != null ? currentPlaying.nickname() : null,
                currentAction
        );
    }

    /**
     * Used to save the state of the PlayerManager, including only the private information of the
     * player with the given nickname
     *
     * @param nickname the nickname of the player to save
     * @return a PlayerManagerMemento containing the state of the PlayerManager
     */
    public @NotNull PlayerManagerMemento savePublic(@NotNull String nickname) {
        return new PlayerManagerMemento(
                playerQueue.stream()
                           .map(player -> player.nickname().equals(nickname) ?
                                          player.save() :
                                          player.savePublic())
                           .toList(),
                firstPlayer != null ? firstPlayer.nickname() : null,
                currentPlaying != null ? currentPlaying.nickname() : null,
                currentAction
        );
    }

    /**
     * Used to load the state of the PlayerManager
     *
     * @param memento the PlayerManagerMemento containing the state of the PlayerManager
     */
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

    /**
     * Used to hard reset the PlayerManager, it will clear all the players and the queue of players
     */
    public void hardReset() {
        LOGGER.debug("MODEL: Hard reset on player manager");

        players.clear();
        playerQueue.clear();
        unavailablePlayers.clear();
        firstPlayer = null;
        currentPlaying = null;
    }
}

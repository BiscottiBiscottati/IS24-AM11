package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.model.utils.GameStatus;
import it.polimi.ingsw.am11.model.utils.memento.PlateauMemento;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.table.FinalLeaderboardEvent;
import it.polimi.ingsw.am11.view.events.view.table.GameStatusChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.PlayerPointsChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Plateau {
    private static final Logger LOGGER = LoggerFactory.getLogger(Plateau.class);

    private static int armageddonTimePoints;
    private final @NotNull Map<String, Integer> playerPoints;
    private final @NotNull Map<String, Integer> counterObjective;
    private final @NotNull Map<String, Integer> finalLeaderboard;
    private final @NotNull AtomicReference<GameStatus> status;
    private final @NotNull GameListenerSupport pcs;


    public Plateau(@NotNull GameListenerSupport pcs) {
        this.playerPoints = new HashMap<>(8);
        this.counterObjective = new HashMap<>(3);
        this.status = new AtomicReference<>(GameStatus.SETUP);
        this.finalLeaderboard = new HashMap<>(8);
        this.pcs = pcs;
    }

    public GameStatus getStatus() {
        return status.get();
    }

    /**
     * Set the game status and notify the listeners
     *
     * @param status the new game status
     */
    public void setStatus(@NotNull GameStatus status) {
        GameStatus oldValue = this.status.get();
        this.status.compareAndSet(oldValue, status);

        LOGGER.info("MODEL: Game status changed from {} to {}", oldValue, status);

        pcs.fireEvent(new GameStatusChangeEvent(oldValue, status));
    }

    /**
     * Add points to the specified player and notify the listeners
     *
     * @param player the player to add points to
     * @param points the points to add
     * @throws IllegalPlateauActionException if the player is not found
     */
    public void addPlayerPoints(@NotNull String player, int points)
    throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else {
            temp += points;
            playerPoints.put(player, temp);

            LOGGER.info("MODEL: Player {} gained {} points, total: {}", player, points,
                        temp);

            pcs.fireEvent(new PlayerPointsChangeEvent(
                    player,
                    temp - points,
                    temp));
        }
        if (temp >= armageddonTimePoints && this.status.get() == GameStatus.ONGOING) {
            setStatus(GameStatus.ARMAGEDDON);
        }

    }

    /**
     * Check if the game is in the status armageddon time
     *
     * @return true if the game is in the status armageddon time
     */
    public boolean isArmageddonTime() {
        return status.get() == GameStatus.ARMAGEDDON;
    }

    /**
     * Set the points needed to activate the armageddon time
     *
     * @param armageddonTime the points needed to activate the armageddon time
     */
    public static void setArmageddonTime(int armageddonTime) {
        Plateau.armageddonTimePoints = armageddonTime;
    }

    /**
     * Set the game status to Armageddon
     */
    public void activateArmageddon() {
        setStatus(GameStatus.ARMAGEDDON);
    }

    /**
     * Remove the specified player from the plateau, it will be removed from the points, counter-
     * * objective and final leaderboard
     *
     * @param player the player to remove
     */
    public void removePlayer(@NotNull String player) {
        playerPoints.remove(player);
        counterObjective.remove(player);
        finalLeaderboard.remove(player);
    }

    /**
     * Reset the points, counter objective and final leaderboard for every player, but it keeps the
     * players in the plateau
     */
    public void reset() {
        playerPoints.keySet()
                    .forEach(player -> {
                        playerPoints.put(player, 0);
                        pcs.fireEvent(new PlayerPointsChangeEvent(
                                player,
                                null,
                                0
                        ));
                    });
        finalLeaderboard.keySet()
                        .forEach(
                                player -> finalLeaderboard.put(player, null)
                        );
        counterObjective.keySet()
                        .forEach(
                                player -> counterObjective.put(player, 0)
                        );
    }

    /**
     * Add a new player to the plateau with 0 points and 0 counter objectives
     *
     * @param newPlayer nickname of the new player to add
     */
    public void addPlayer(@NotNull String newPlayer) {
        playerPoints.put(newPlayer, 0);
        counterObjective.put(newPlayer, 0);
    }

    /**
     * The counter-objective is a Map that contains the number of objectives completed by each
     * player; this method adds 1 to the counter-objective of the specified player
     *
     * @param player the player to add the counter-objective to
     * @throws IllegalPlateauActionException if the player is not found
     */
    public void addCounterObjective(@NotNull String player)
    throws IllegalPlateauActionException {
        if (! counterObjective.containsKey(player)) {
            throw new IllegalPlateauActionException("Player not found");
        } else {
            counterObjective.merge(player, 1, Integer::sum);
        }
    }

    /**
     * Get the points of the specified player
     *
     * @param player the player to get the points of
     * @return the points of the specified player
     * @throws IllegalPlateauActionException if the player is not found
     */
    public int getPlayerPoints(@NotNull String player) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }

    /**
     * Get the number of completed objectives of the specified player
     *
     * @param player the player to get the counter-objective of
     * @return the number of completed objectives of the specified player
     * @throws IllegalPlateauActionException if the player is not found
     */
    public int getCounterObjective(@NotNull String player) throws IllegalPlateauActionException {
        Integer temp = counterObjective.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }

    /**
     * Set the final leaderboard, the final leaderboard is a Map that contains the final position of
     * each player, more than one player can have the same final position if they have the same
     * points and counter-objective. It will be considered a  tie
     */
    public void setFinalLeaderboard() {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(playerPoints.entrySet());

        entries.sort(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed());

        int rank = 1;
        int size = entries.size();
        for (int i = 0; i < size; i++) {
            if (i > 0 && entries.get(i).getValue().equals(entries.get(i - 1).getValue())
                && counterObjective.get(entries.get(i).getKey())
                                   .equals(counterObjective.get(entries.get(i - 1).getKey()))) {
                finalLeaderboard.put(entries.get(i).getKey(),
                                     finalLeaderboard.get(entries.get(i - 1).getKey()));
            } else {
                finalLeaderboard.put(entries.get(i).getKey(), rank++);
            }
        }

        Map<String, Integer> finalLeaderboardString = Map.copyOf(finalLeaderboard);

        LOGGER.info("MODEL: Final leaderboard set: {}", finalLeaderboardString);

        pcs.fireEvent(new FinalLeaderboardEvent(finalLeaderboardString));
    }

    /**
     * Get the final position of the specified player, more than one player can have the same final
     * position if they have the same * points and counter-objective. It will be considered a  tie
     *
     * @param player the player to get the final position of
     * @return the final position of the specified player
     * @throws IllegalPlateauActionException if the player is not found
     */
    public int getPlayerFinishingPosition(@NotNull String player)
    throws IllegalPlateauActionException {
        if (finalLeaderboard.containsKey(player)) {
            return finalLeaderboard.get(player);
        } else {
            throw new IllegalPlateauActionException("Player not found");
        }
    }

    /**
     * Get the winner/winners of the game, the winner are the players that have position 1 in the
     * final leaderboard
     *
     * @return set with the nickname of the winner/winners
     */
    public @NotNull Set<String> getWinners() {
        Set<String> winners = new HashSet<>(8);
        for (Map.Entry<String, Integer> entry : finalLeaderboard.entrySet()) {
            if (entry.getValue() == 1) {
                winners.add(entry.getKey());
            }
        }
        return Set.copyOf(winners);
    }

    /**
     * Arbitrary set the winner of the game, the winner will have position 1 in the final
     * leaderboard, the other players will have position 2
     *
     * @param player the nickname of the winner
     */
    public void setWinner(@NotNull String player) {
        finalLeaderboard.keySet()
                        .forEach(p -> finalLeaderboard.put(p, 2));

        finalLeaderboard.put(player, 1);

        LOGGER.info("MODEL: Winner set: {}", player);

        pcs.fireEvent(new FinalLeaderboardEvent(finalLeaderboard));
    }

    /**
     * Used to save the state of the plateau
     *
     * @return a memento with the state of the plateau
     */
    public @NotNull PlateauMemento save() {
        return new PlateauMemento(Map.copyOf(playerPoints),
                                  Map.copyOf(counterObjective),
                                  Map.copyOf(finalLeaderboard),
                                  status.get());
    }

    /**
     * Used to load the state of the plateau
     *
     * @param memento the memento with the state of the plateau
     */
    public void load(@NotNull PlateauMemento memento) {
        hardReset();

        playerPoints.putAll(memento.playerPoints());
        counterObjective.putAll(memento.objCounter());
        finalLeaderboard.putAll(memento.leaderboard());

        status.set(memento.status());
    }

    /**
     * Hard reset the plateau, it will remove all the players, points, counter-objective and final
     */
    public void hardReset() {
        LOGGER.debug("MODEL: Hard reset on plateau");
        playerPoints.clear();
        counterObjective.clear();
        finalLeaderboard.clear();
        status.set(GameStatus.SETUP);
    }

}

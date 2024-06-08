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

    private static int armageddonTime;
    private final @NotNull Map<String, Integer> playerPoints;
    private final @NotNull Map<String, Integer> counterObjective;
    private final @NotNull Map<String, Integer> finalLeaderboard;
    private final @NotNull GameListenerSupport pcs;
    private final @NotNull AtomicReference<GameStatus> status;


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

    public void setStatus(@NotNull GameStatus status) {
        GameStatus oldValue = this.status.get();
        this.status.compareAndSet(oldValue, status);

        LOGGER.info("MODEL: Game status changed from {} to {}", oldValue, status);

        pcs.fireEvent(new GameStatusChangeEvent(oldValue, status));
    }

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
        if (temp >= armageddonTime && this.status.get() == GameStatus.ONGOING) {
            setStatus(GameStatus.ARMAGEDDON);
        }

    }

    public boolean isArmageddonTime() {
        return status.get() == GameStatus.ARMAGEDDON;
    }

    public static void setArmageddonTime(int armageddonTime) {
        Plateau.armageddonTime = armageddonTime;
    }

    public void activateArmageddon() {
        setStatus(GameStatus.ARMAGEDDON);
    }

    public void removePlayer(@NotNull String player) {
        playerPoints.remove(player);
        counterObjective.remove(player);
        finalLeaderboard.remove(player);
    }

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

    public void addPlayer(@NotNull String newPlayer) {
        playerPoints.put(newPlayer, 0);
        counterObjective.put(newPlayer, 0);
    }

    public void addCounterObjective(@NotNull String player)
    throws IllegalPlateauActionException {
        if (! counterObjective.containsKey(player)) {
            throw new IllegalPlateauActionException("Player not found");
        } else {
            counterObjective.merge(player, 1, Integer::sum);
        }
    }

    public int getPlayerPoints(@NotNull String player) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }

    public int getCounterObjective(@NotNull String player) throws IllegalPlateauActionException {
        Integer temp = counterObjective.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }

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

    public int getPlayerFinishingPosition(@NotNull String player)
    throws IllegalPlateauActionException {
        if (finalLeaderboard.containsKey(player)) {
            return finalLeaderboard.get(player);
        } else {
            throw new IllegalPlateauActionException("Player not found");
        }
    }

    public @NotNull Set<String> getWinners() {
        Set<String> winners = new HashSet<>(8);
        for (Map.Entry<String, Integer> entry : finalLeaderboard.entrySet()) {
            if (entry.getValue() == 1) {
                winners.add(entry.getKey());
            }
        }
        return Set.copyOf(winners);
    }

    public void setWinner(@NotNull String player) {
        finalLeaderboard.keySet()
                        .forEach(p -> finalLeaderboard.put(p, 2));

        finalLeaderboard.put(player, 1);

        LOGGER.info("MODEL: Winner set: {}", player);

        pcs.fireEvent(new FinalLeaderboardEvent(finalLeaderboard));
    }

    public @NotNull PlateauMemento save() {
        return new PlateauMemento(Map.copyOf(playerPoints),
                                  Map.copyOf(counterObjective),
                                  Map.copyOf(finalLeaderboard),
                                  status.get());
    }

    public void load(@NotNull PlateauMemento memento) {
        hardReset();

        playerPoints.putAll(memento.playerPoints());
        counterObjective.putAll(memento.objCounter());
        finalLeaderboard.putAll(memento.leaderboard());

        status.set(memento.status());
    }

    public void hardReset() {
        LOGGER.debug("MODEL: Hard reset on plateau");
        playerPoints.clear();
        counterObjective.clear();
        finalLeaderboard.clear();
        status.set(GameStatus.SETUP);
    }

}

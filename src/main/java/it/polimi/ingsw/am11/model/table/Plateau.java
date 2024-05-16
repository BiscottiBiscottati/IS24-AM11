package it.polimi.ingsw.am11.model.table;

import it.polimi.ingsw.am11.model.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.model.players.Player;
import it.polimi.ingsw.am11.view.events.listeners.TableListener;
import it.polimi.ingsw.am11.view.events.support.GameListenerSupport;
import it.polimi.ingsw.am11.view.events.view.table.FinalLeaderboardEvent;
import it.polimi.ingsw.am11.view.events.view.table.GameStatusChangeEvent;
import it.polimi.ingsw.am11.view.events.view.table.PlayerPointsChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plateau {
    private static final Logger LOGGER = LoggerFactory.getLogger(Plateau.class);

    private static int armageddonTime;
    private final Map<Player, Integer> playerPoints;
    private final Map<Player, Integer> counterObjective;
    private final Map<Player, Integer> finalLeaderboard;
    private final GameListenerSupport pcs;
    private GameStatus status;


    public Plateau(GameListenerSupport pcs) {
        this.playerPoints = new HashMap<>(8);
        this.counterObjective = new HashMap<>(3);
        this.status = GameStatus.SETUP;
        this.finalLeaderboard = new HashMap<>(8);
        this.pcs = pcs;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        GameStatus oldValue = this.status;
        this.status = status;

        LOGGER.info("Game status changed from {} to {}", oldValue, status);

        pcs.fireEvent(new GameStatusChangeEvent(oldValue, status));
    }

    public void addPlayerPoints(Player player, int points) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else {
            temp += points;
            playerPoints.put(player, temp);

            LOGGER.info("Player {} gained {} points, total: {}", player.nickname(), points, temp);

            pcs.fireEvent(new PlayerPointsChangeEvent(
                    player.nickname(),
                    temp - points,
                    temp));
        }
        if (temp >= armageddonTime && status == GameStatus.ONGOING) {
            setStatus(GameStatus.ARMAGEDDON);
        }

    }

    public boolean isArmageddonTime() {
        return status == GameStatus.ARMAGEDDON;
    }

    public static void setArmageddonTime(int armageddonTime) {
        Plateau.armageddonTime = armageddonTime;
    }

    public void activateArmageddon() {
        setStatus(GameStatus.ARMAGEDDON);
    }

    public void removePlayer(Player player) {
        playerPoints.remove(player);
        counterObjective.remove(player);
        finalLeaderboard.remove(player);
    }

    public void reset() {
        playerPoints.keySet()
                    .forEach(player -> {
                        playerPoints.put(player, 0);
                        pcs.fireEvent(new PlayerPointsChangeEvent(
                                player.nickname(),
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
        setStatus(GameStatus.CHOOSING_STARTERS);
    }

    public void addPlayer(Player newPlayer) {
        playerPoints.put(newPlayer, 0);
        counterObjective.put(newPlayer, 0);
    }

    public void addCounterObjective(Player player)
    throws IllegalPlateauActionException {
        if (! counterObjective.containsKey(player)) {
            throw new IllegalPlateauActionException("Player not found");
        } else {
            counterObjective.merge(player, 1, Integer::sum);
        }
    }

    public int getPlayerPoints(Player player) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }

    public int getCounterObjective(Player player) throws IllegalPlateauActionException {
        Integer temp = counterObjective.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }

    public void setFinalLeaderboard() {
        List<Map.Entry<Player, Integer>> entries = new ArrayList<>(playerPoints.entrySet());

        entries.sort((e1, e2) -> {
            int pointsComparison = e2.getValue().compareTo(e1.getValue());
            if (pointsComparison != 0) {
                return pointsComparison;
            } else {
                return counterObjective.get(e2.getKey()).compareTo(
                        counterObjective.get(e1.getKey()));
            }
        });

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

        Map<String, Integer> finalLeaderboardString = finalLeaderboard.entrySet()
                                                                      .stream()
                                                                      .map(e -> Map.entry(
                                                                              e.getKey().nickname(),
                                                                              e.getValue()))
                                                                      .collect(
                                                                              HashMap::new,
                                                                              (m, e) -> m.put(
                                                                                      e.getKey(),
                                                                                      e.getValue()),
                                                                              HashMap::putAll);

        LOGGER.info("Final leaderboard set: {}", finalLeaderboardString);

        pcs.fireEvent(new FinalLeaderboardEvent(finalLeaderboardString));
    }

    public int getPlayerFinishingPosition(Player player) throws IllegalPlateauActionException {
        if (finalLeaderboard.containsKey(player)) {
            return finalLeaderboard.get(player);
        } else {
            throw new IllegalPlateauActionException("Player not found");
        }
    }


    public List<Player> getWinners() {
        List<Player> winners = new ArrayList<>(4);
        for (Map.Entry<Player, Integer> entry : finalLeaderboard.entrySet()) {
            if (entry.getValue() == 1) {
                winners.add(entry.getKey());
            }
        }
        return winners;
    }

    public void addListener(TableListener listener) {
        pcs.addListener(listener);
    }

    public void removeListener(TableListener listener) {
        pcs.removeListener(listener);
    }

}

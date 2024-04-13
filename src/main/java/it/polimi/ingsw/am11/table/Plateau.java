package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Plateau {
    private final Map<Player, Integer> playerPoints;
    private final Map<Player, Integer> finalLeaderboard;
    private final int armageddonTime;
    GameStatus Status;

    // TODO why is there a finalLeaderboard and playerPoints? Are they the same thing?
    public Plateau(int armageddonTime) {
        this.playerPoints = new HashMap<>(8);
        this.armageddonTime = armageddonTime;
        this.Status = GameStatus.ONGOING;
        this.finalLeaderboard = new HashMap<>(8);
    }

    public GameStatus getStatus() {
        return Status;
    }

    public void setStatus(GameStatus status) {
        Status = status;
    }

    public GameStatus isArmageddonTime() {
        return Status;
    }

    public void activateArmageddon() {
        Status = GameStatus.ARMAGEDDON;
    }

    public void reset() {
        playerPoints.keySet()
                    .forEach(
                            player -> playerPoints.put(player, 0)
                    );
        finalLeaderboard.keySet()
                        .forEach(
                                player -> finalLeaderboard.put(player, null)
                        );
        Status = GameStatus.ONGOING;
    }

    public void addPlayer(Player newPlayer) {
        playerPoints.put(newPlayer, 0);
    }

    public void addPlayerPoints(Player player, int points) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else {
            temp += points;
            playerPoints.put(player, temp);
        }
        if (temp >= armageddonTime) {
            Status = GameStatus.ARMAGEDDON;
        }
    }

    public int getPlayerPoints(Player player) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(player, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }

    public void setFinalLeaderboard() {

        AtomicInteger rank = new AtomicInteger(0);
        AtomicInteger previousPoints = new AtomicInteger(- 1);

        playerPoints.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // sort in descending order
                    .forEach(entry -> {
                        if (previousPoints.get() != entry.getValue()) {
                            rank.incrementAndGet();
                        }
                        finalLeaderboard.put(entry.getKey(), rank.get());
                        previousPoints.set(entry.getValue());
                    });
    }

    public int getPlayerFinishingPosition(Player player) {

        return finalLeaderboard.getOrDefault(player, - 1);
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

    public Map<Player, Integer> getFinalLeaderboard() {
        return finalLeaderboard;
    }

}

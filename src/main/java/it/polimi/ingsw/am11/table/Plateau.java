package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.players.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plateau {
    private final Map<Player, Integer> playerPoints;
    private final int armageddonTime;
    private final Map<Player, Integer> finalLeaderboard;
    private boolean isArmageddonTime;

    // TODO why is there a finalLeaderboard and playerPoints? Are they the same thing?
    public Plateau(int armageddonTime) {
        this.playerPoints = new HashMap<>(8);
        this.armageddonTime = armageddonTime;
        this.isArmageddonTime = false;
        this.finalLeaderboard = new HashMap<>(8);
    }


    public boolean isArmageddonTime() {
        return isArmageddonTime;
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
    }

    public void addPlayer(Player newPlayer) {
        playerPoints.put(newPlayer, 0);
    }

    public void addPlayerPoints(Player playerName, int points) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(playerName, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else {
            temp += points;
            playerPoints.put(playerName, temp);
        }
        if (temp >= armageddonTime) {
            isArmageddonTime = true;
        }
    }

    public int getPlayerPoints(Player playerName) throws IllegalPlateauActionException {
        Integer temp = playerPoints.getOrDefault(playerName, null);
        if (temp == null) {
            throw new IllegalPlateauActionException("Player not found");
        } else return temp;
    }


    public int getPlayerFinihingPosition(Player player) {
        //TODO: need to implement this method
        return -1;
    }

    public List<Player> getWinners() {
        //TODO: need to implement this method, remember that there could be more winners.
        return null;
    }
}

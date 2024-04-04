package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.players.Player;

import java.util.HashMap;
import java.util.Map;

public class Plateau {
    private final Map<Player, Integer> playerPoints;
    private final int armageddonTime;
    private boolean isArmageddonTime;

    public Plateau(int armageddonTime) {
        this.playerPoints = new HashMap<>(8);
        this.armageddonTime = armageddonTime;
        this.isArmageddonTime = false;
    }

    public void reset() {
        playerPoints.keySet()
                    .forEach(
                            player -> playerPoints.put(player, 0)
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



    /*public List<Player> getLeadingPlayer() {
        return
    }*/
}

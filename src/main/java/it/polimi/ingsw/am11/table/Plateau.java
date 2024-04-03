package it.polimi.ingsw.am11.table;

import it.polimi.ingsw.am11.exceptions.IllegalPlateauActionException;
import it.polimi.ingsw.am11.exceptions.IllegalPlateauSetupException;
import it.polimi.ingsw.am11.players.Player;

import java.util.HashMap;
import java.util.Map;

public class Plateau {
    private final Map<Player, Integer> playerPoints;
    private final int armageddonTime;
    private final int numOfPlayers;
    private boolean isArmageddonTime;

    public Plateau(int armageddonTime, int numOfPlayers) throws IllegalPlateauSetupException {
        this.playerPoints = new HashMap<>(8);
        if (armageddonTime <= 0) {
            throw new IllegalPlateauSetupException(
                    "Illegal value for number of points to start the final phase, passed value is " + armageddonTime
            );
        } else this.armageddonTime = armageddonTime;

        if (numOfPlayers <= 1) {
            throw new IllegalPlateauSetupException(
                    "Illegal value for numOfPlayers, passed value is " + numOfPlayers
            );
        } else this.numOfPlayers = numOfPlayers;

        this.isArmageddonTime = false;
    }

    public void reset() {
        playerPoints.keySet()
                    .forEach(
                            player -> playerPoints.put(player, 0)
                    );
    }

    public void addPlayer(Player newPlayer) throws IllegalPlateauSetupException {
        if (playerPoints.size() < numOfPlayers) {
            playerPoints.put(newPlayer, 0);
        } else {
            throw new IllegalPlateauSetupException("Reached number of players limit!");
        }
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

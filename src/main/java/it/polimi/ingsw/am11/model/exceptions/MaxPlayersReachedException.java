package it.polimi.ingsw.am11.model.exceptions;

public class MaxPlayersReachedException extends Exception {
    public MaxPlayersReachedException(String message) {
        super(message);
    }
}

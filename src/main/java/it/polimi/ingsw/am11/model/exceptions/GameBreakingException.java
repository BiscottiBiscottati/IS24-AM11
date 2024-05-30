package it.polimi.ingsw.am11.model.exceptions;

public class GameBreakingException extends RuntimeException {
    public GameBreakingException(String message) {
        super(message);
    }

    public GameBreakingException(Throwable cause) {
        super(cause);
    }
}

package it.polimi.ingsw.am11.model.exceptions;

import java.rmi.RemoteException;

public class ServerException extends RemoteException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public ServerException() {
        super();
    }
}

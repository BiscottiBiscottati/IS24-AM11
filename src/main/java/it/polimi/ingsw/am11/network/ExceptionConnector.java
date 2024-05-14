package it.polimi.ingsw.am11.network;

public interface ExceptionConnector {
    void throwIllegalPlayerSpaceActionException(String message);

    void throwTurnsOrderException(String message);

    void throwPlayerInitException(String message);

    void throwIllegalCardPlacingException(String message);

    void throwIllegalPickActionException(String message);

    void throwNotInHandException(String message);

    void throwEmptyDeckException(String message);

    void throwMaxNumOfPlayersException(String message);

    void thorwNicknameAlreadyTakenException(String message);
    

}

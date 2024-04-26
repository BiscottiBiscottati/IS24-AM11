package it.polimi.ingsw.am11.network.RMI;

public interface GameControllerInterface {
    void initGame() throws Exception;

    void goNextTurn() throws Exception;

    void addPlayer(String nickname) throws Exception;

    void removePlayer(String nickname) throws Exception;

    void forceEnd() throws Exception;
}

package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GameModel {
    //region GetterGameStatus
    List<String> getPlayers();

    String getCurrentTurnPlayer();

    String getFirstPlayer();
    //endregion

    //region GettersPlayer
    List<Integer> getPlayerHand(String nickname);

    List<Integer> getPlayerObjective(String nickname);

    PlayerColor getPlayerColor(String nickname);

    //FIXME this interface has to be changed, it can't return a map
    Map<Position, CardContainer> getPositionedCard(String nickname);

    Set<Position> getAvailablePositions(String nickname);

    //endregion

    //region GetterPickableTable
    Optional<Color> getResourceDeckTop();

    Optional<Color> getGoldDeckTop();

    List<Integer> getCommonObjectives();

    List<Integer> getExposedGoldsCrd();

    List<Integer> getExposedResourcesCrd();
    //endregion

    //region GetterPlateau
    int getPlayerPoints(String nickname)
    throws IllegalPlateauActionException;

    boolean isArmageddonTime();

    int getPlayerFinishingPosition(String nickname);

    List<String> getWinner();

    //endregion

    //region GameInitialization
    void initGame() throws IllegalNumOfPlayersException;

    void addPlayerToTable(String nickname, PlayerColor colour) throws PlayerInitException;

    void shufflePlayers();

    void setStartingPlayer();

    int pickStarter() throws EmptyDeckException;

    int pickObjective() throws EmptyDeckException;

    void setStarterFor(String nickname, int cardID, boolean isRetro)
    throws IllegalCardPlacingException;

    void setObjectiveFor(String nickname, int cardID)
    throws IllegalPlayerSpaceActionException;
    //endregion

    //region TurnsActions

    void goNextTurn() throws GameBreakingException;

    void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException, NotInHandException;

    int drawFromGoldDeck(String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException, TurnsOrderException;

    int drawFromResourceDeck(String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException, TurnsOrderException;

    void drawVisibleGold(String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException, TurnsOrderException;

    void drawVisibleResource(String nickname, int ID)
    throws GameBreakingException,
           IllegalPickActionException,
           IllegalPlayerSpaceActionException, TurnsOrderException;

    //TODO add possibility of signaling how many objectives a player has completed
    void countObjectivesPoints() throws IllegalPlateauActionException;
    //endregion

}

package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import it.polimi.ingsw.am11.table.GameStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GameModel {
    //region GetterGameStatus
    Set<String> getPlayers();

    String getCurrentTurnPlayer();

    String getFirstPlayer();
    //endregion

    //region GettersPlayer
    Set<Integer> getPlayerHand(String nickname);

    Set<Integer> getPlayerObjective(String nickname);

    PlayerColor getPlayerColor(String nickname);

    //FIXME this interface has to be changed, it can't return a map
    Map<Position, CardContainer> getPositionedCard(String nickname);

    Set<Position> getAvailablePositions(String nickname);

    //endregion

    //region GetterPickableTable

    Optional<Color> getDeckTop(PlayableCardType type);

    List<Integer> getCommonObjectives();

    List<Integer> getExposedCards();
    //endregion

    //region GetterPlateau
    int getPlayerPoints(String nickname)
    throws IllegalPlateauActionException;

    int getPlayerFinishingPosition(String nickname) throws IllegalPlateauActionException;

    List<String> getWinner();

    //endregion

    //region GameInitialization
    void initGame() throws IllegalNumOfPlayersException, GameStatusException;

    void resetAll();

    void addPlayerToTable(String nickname, PlayerColor colour)
    throws PlayerInitException, GameStatusException;

    void removePlayer(@NotNull String nickname) throws GameStatusException;

    int pickStarter() throws EmptyDeckException, GameStatusException;

    int pickObjective() throws EmptyDeckException, GameStatusException;

    void setStarterFor(String nickname, int cardID, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException;

    void setObjectiveFor(String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException;
    //endregion

    //region TurnsActions

    String goNextTurn() throws GameBreakingException, GameStatusException;

    void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException, GameStatusException, NotInHandException;

    int drawFromGoldDeck(String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException;

    int drawFromResourceDeck(String nickname)
    throws GameBreakingException,
           EmptyDeckException,
           IllegalPlayerSpaceActionException, TurnsOrderException, GameStatusException;

    int drawFromDeckOf(PlayableCardType type, String nickname)
    throws GameStatusException, TurnsOrderException, GameBreakingException, EmptyDeckException,
           IllegalPlayerSpaceActionException;

    void drawVisibleOf(PlayableCardType type, String nickname, int cardID)
    throws GameStatusException, TurnsOrderException, GameBreakingException,
           IllegalPlayerSpaceActionException, IllegalPickActionException;

    //TODO add possibility of signaling how many objectives a player has completed
    void countObjectivesPoints() throws IllegalPlateauActionException, GameStatusException;

    //DONE
    void endGame();

    GameStatus getStatus();

    Optional<Color> getDeckTop(PlayableCardType type);
    //endregion

}

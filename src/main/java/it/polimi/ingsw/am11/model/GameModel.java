package it.polimi.ingsw.am11.model;

import it.polimi.ingsw.am11.cards.starter.StarterCard;
import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.players.CardContainer;
import it.polimi.ingsw.am11.players.PlayerColor;
import it.polimi.ingsw.am11.players.Position;
import it.polimi.ingsw.am11.table.GameStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"ClassWithTooManyMethods", "OverlyCoupledClass"})
public interface GameModel {

    Set<String> getPlayers();

    String getCurrentTurnPlayer() throws GameStatusException;

    String getFirstPlayer() throws GameStatusException;

    Set<Integer> getPlayerHand(String nickname) throws GameStatusException, PlayerInitException;

    Set<Integer> getPlayerObjective(String nickname) throws PlayerInitException,
                                                            GameStatusException;

    PlayerColor getPlayerColor(String nickname) throws PlayerInitException;

    //FIXME this interface has to be changed, it can't return a map
    Map<Position, CardContainer> getPositionedCard(String nickname)
    throws PlayerInitException, GameStatusException;

    Set<Position> getAvailablePositions(String nickname)
    throws PlayerInitException, GameStatusException;

    @NotNull Set<Integer> getCommonObjectives() throws GameStatusException;

    Set<Integer> getExposedCards(PlayableCardType type) throws GameStatusException;


    int getPlayerPoints(String nickname)
    throws PlayerInitException, GameStatusException,
           GameBreakingException;

    int getPlayerFinishingPosition(String nickname)
    throws PlayerInitException, GameStatusException,
           GameBreakingException;

    Set<String> getWinner() throws GameStatusException;

    void initGame() throws IllegalNumOfPlayersException, GameStatusException, GameBreakingException;

    void addPlayerToTable(String nickname, PlayerColor colour)
    throws PlayerInitException, GameStatusException;

    void removePlayer(@NotNull String nickname) throws GameStatusException;

    int pickStarterFor(String nickname) throws EmptyDeckException, GameStatusException,
                                               PlayerInitException;

    Set<Integer> pickCandidateObjectives(@NotNull String nickname)
    throws EmptyDeckException, GameStatusException, PlayerInitException;

    void setStarterFor(String nickname, boolean isRetro)
    throws IllegalCardPlacingException, GameStatusException, PlayerInitException;

    void setObjectiveFor(String nickname, int cardID)
    throws IllegalPlayerSpaceActionException, GameStatusException, PlayerInitException;

    String goNextTurn() throws GameBreakingException, GameStatusException;

    void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException, GameStatusException, NotInHandException,
           PlayerInitException;

    int drawFromDeckOf(PlayableCardType type, String nickname)
    throws GameStatusException, TurnsOrderException, GameBreakingException, EmptyDeckException,
           IllegalPlayerSpaceActionException, PlayerInitException, MaxHandSizeException;

    void drawVisibleOf(PlayableCardType type, String nickname, int cardID)
    throws GameStatusException, TurnsOrderException, GameBreakingException,
           IllegalPlayerSpaceActionException, IllegalPickActionException, PlayerInitException;

    void countObjectivesPoints()
    throws IllegalPlateauActionException, GameStatusException, GameBreakingException;

    void endGame();

    GameStatus getStatus();

    Optional<Color> getDeckTop(PlayableCardType type) throws GameStatusException;

    Set<Integer> getCandidateObjectives(@NotNull String nickname)
    throws PlayerInitException, GameStatusException;

    Optional<StarterCard> getStarterCard(@NotNull String nickname)
    throws PlayerInitException;
}

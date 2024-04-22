package it.polimi.ingsw.am11.controller;

import it.polimi.ingsw.am11.cards.utils.enums.Color;
import it.polimi.ingsw.am11.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.exceptions.*;
import it.polimi.ingsw.am11.model.GameLogic;
import it.polimi.ingsw.am11.model.GameModel;
import it.polimi.ingsw.am11.players.utils.CardContainer;
import it.polimi.ingsw.am11.players.utils.PlayerColor;
import it.polimi.ingsw.am11.players.utils.Position;
import it.polimi.ingsw.am11.table.GameStatus;

import java.util.*;

public class GameController {
    private final GameModel model;
    private final List<PlayerColor> colors;

    public GameController() {
        model = new GameLogic();
        colors = Arrays.asList(PlayerColor.values());
        Collections.shuffle(colors);
    }

    public String getFirstPlayer() throws GameStatusException {
        return model.getFirstPlayer();
    }

    public Set<String> getPlayers() {
        return model.getPlayers();
    }

    public String getCurrentTurnPlayer() throws GameStatusException {
        return model.getCurrentTurnPlayer();
    }

    public Set<Integer> getPlayerHand(String nickname) throws GameStatusException,
                                                              PlayerInitException {
        return model.getPlayerHand(nickname);
    }

    public Set<Integer> getPlayerObjective(String nickname) throws PlayerInitException,
                                                                   GameStatusException {
        return model.getPlayerObjective(nickname);
    }

    public PlayerColor getPlayerColor(String nickname) throws PlayerInitException {
        return model.getPlayerColor(nickname);
    }

    public Map<Position, CardContainer> getPositionedCard(String nickname)
    throws PlayerInitException,
           GameStatusException {
        return model.getPositionedCard(nickname);
    }

    public Set<Position> getAvailablePositions(String nickname) throws PlayerInitException,
                                                                       GameStatusException {
        return model.getAvailablePositions(nickname);
    }

    public Set<Integer> getCommonObjectives() throws GameStatusException {
        return model.getCommonObjectives();
    }

    public Set<Integer> getExposedCards(PlayableCardType type) throws GameStatusException {
        return model.getExposedCards(type);
    }

    public int getPlayerPoints(String nickname) throws PlayerInitException,
                                                       GameStatusException,
                                                       GameBreakingException {
        return model.getPlayerPoints(nickname);
    }

    public int getPlayerFinishingPosition(String nickname) throws PlayerInitException,
                                                                  GameStatusException,
                                                                  GameBreakingException {
        return model.getPlayerFinishingPosition(nickname);
    }

    public Set<String> getWinner() throws GameStatusException {
        return model.getWinner();
    }

    public void initGame() throws IllegalNumOfPlayersException,
                                  GameStatusException,
                                  GameBreakingException {
        model.initGame();
    }

    public void addPlayerToTable(String nickname) throws PlayerInitException, GameStatusException {

        Set<String> players = model.getPlayers();
        if (players.contains(nickname)) {
            throw new PlayerInitException("Player already exists");
        }
        if (colors.isEmpty()) {
            throw new PlayerInitException("No more colors available");
        }

        PlayerColor color = colors.removeFirst();
        model.addPlayerToTable(nickname, color);
    }

    public void removePlayer(String nickname) throws GameStatusException {
        model.removePlayer(nickname);
    }

    public void setStarterFor(String nickname, boolean isRetro) throws GameStatusException,
                                                                       PlayerInitException,
                                                                       IllegalCardPlacingException {
        model.setStarterFor(nickname, isRetro);
    }

    public void setObjectiveFor(String nickname, int cardID) throws GameStatusException,
                                                                    PlayerInitException,
                                                                    IllegalPlayerSpaceActionException {
        model.setObjectiveFor(nickname, cardID);
    }

    public void placeCard(String Nickname, int ID, Position position, boolean isRetro)
    throws GameStatusException,
           PlayerInitException,
           IllegalCardPlacingException,
           TurnsOrderException,
           IllegalPlateauActionException,
           NotInHandException {
        model.placeCard(Nickname, ID, position, isRetro);
    }

    public int drawFromDeckOf(PlayableCardType type, String nickname) throws GameStatusException,
                                                                             TurnsOrderException,
                                                                             GameBreakingException,
                                                                             EmptyDeckException,
                                                                             IllegalPlayerSpaceActionException,
                                                                             PlayerInitException,
                                                                             MaxHandSizeException,
                                                                             IllegalPickActionException {
        return model.drawFromDeckOf(type, nickname);
    }

    public void goNextTurn() throws GameBreakingException,
                                    GameStatusException {
        model.goNextTurn();
    }

    public void drawVisibleOf(PlayableCardType type, String nickname, int cardID)
    throws GameStatusException,
           TurnsOrderException,
           GameBreakingException,
           IllegalPlayerSpaceActionException,
           IllegalPickActionException,
           PlayerInitException {
        model.drawVisibleOf(type, nickname, cardID);
    }

    public void forceEnd() {
        model.forceEnd();
    }

    public GameStatus getStatus() {
        return model.getStatus();
    }

    public Optional<Color> getDeckTop(PlayableCardType type) throws GameStatusException {
        return model.getDeckTop(type);
    }

    public Set<Integer> getCandidateObjectives(String nickname) throws PlayerInitException,
                                                                       GameStatusException {
        return model.getCandidateObjectives(nickname);
    }

    public Optional<Integer> getStarterCard(String nickname) throws PlayerInitException,
                                                                    GameStatusException {
        return model.getStarterCard(nickname);
    }
}

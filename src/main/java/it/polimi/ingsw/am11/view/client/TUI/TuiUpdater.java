package it.polimi.ingsw.am11.view.client.TUI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.model.table.GameStatus;
import it.polimi.ingsw.am11.view.client.ClientViewUpdater;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;

import java.util.Map;
import java.util.Set;

public class TuiUpdater implements ClientViewUpdater {
    private final MiniGameModel model;

    public TuiUpdater(MiniGameModel model) {
        this.model = model;
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        model.table().refreshDeckTop(type, color);
        if (model.getCurrentTurn().equals(model.myName())) {
            System.out.println("You picked a card from the " + type.getName() +
                               " deck, the " + type.getName() + " deck top card is now " +
                               color.getColumnName());
        } else {
            System.out.println(
                    model.getCurrentTurn() + "picked a card from the " + type.getName() +
                    " deck, the " + type.getName() + " deck top card is now " +
                    color.getColumnName());
        }
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId,
                            boolean isRetro, boolean removeMode) {
        Position pos = new Position(x, y);
        String frontOrRetro;

        if (isRetro) {
            frontOrRetro = "retro";
        } else {
            frontOrRetro = "front";
        }
        if (! removeMode) {
            model.getCliPlayer(nickname).getField().place(pos, cardId, isRetro);
            if (nickname.equals(model.myName())) {
                model.setiPlaced(true);
                System.out.println("You placed the card " + cardId + " in " + pos + " on his " +
                                   frontOrRetro);
            } else {
                System.out.println(
                        nickname + " placed the card " + cardId + " in " + pos + " on his " +
                        frontOrRetro);
            }

        } else {
            model.getCliPlayer(nickname).getField().remove(pos);
            if (nickname.equals(model.myName())) {
                System.out.println(
                        "The card in position " + pos + " has been removed from your field");
            } else {
                System.out.println(
                        "The card in position " + pos + " has been removed from " + nickname +
                        "'s field");
            }

        }
    }

    @Override
    public void updateShownPlayable(int previousId, int currentId) {
        model.table().pickVisible(previousId);
        model.table().addVisible(currentId);
        if (model.getCurrentTurn().equals(model.myName())) {
            System.out.println("You picked " + previousId + " from the visible " +
                               "cards, it has been replaced by " + currentId);
        } else {
            System.out.println(
                    model.getCurrentTurn() + "picked " + previousId + " from the visible " +
                    "cards, it has been replaced by " + currentId);
        }

    }

    @Override
    public void updateTurnChange(String nickname) {
        model.setCurrentTurn(nickname);
        if (nickname.equals(model.myName()))
            System.out.println("It's now your turn, good luck");
        else System.out.println("It's now " + nickname + " turn");
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        model.getCliPlayer(nickname).addPoints(points);
        if (nickname.equals(model.myName()))
            System.out.println("You gained " + points + " points, now your score is " +
                               model.getCliPlayer(nickname).getPoints());
        else System.out.println(nickname + " gained " + points + " points, now his score is " +
                                model.getCliPlayer(nickname).getPoints());
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        model.table().setStatus(status);
        switch (status) {
            case SETUP -> {
                System.out.println("Waiting for all players to be ready");
            }
            case CHOOSING_STARTERS -> {
                System.out.println("Choose if you want you want to place your starter card on his" +
                                   " front of his back");
            }
            case CHOOSING_OBJECTIVES -> {
                System.out.print("Choose you the personal objective you like the most");

            }
            case ENDED -> {
                System.out.println("The game has ended");
            }
            case ONGOING -> {
                System.out.println("The game has began, fight with honor");
            }
            case ARMAGEDDON -> {
                System.out.println("The final phase of the game has began, the next turn will be " +
                                   "the last one, choose wisely your moves");
            }
            case LAST_TURN -> {
                System.out.println("This is the last turn, play the ace up your sleeve");
            }
            case null, default -> {
                System.out.println("idk what's going on");
            }
        }
    }

    @Override
    public void updateCommonObjective(int cardId, boolean removeMode) {
        if (removeMode) {
            model.table().removeCommonObjective(cardId);
            System.out.println("The card: " + cardId + " is no longer a common objective");
        } else {
            model.table().addCommonObjectives(cardId);
            System.out.println("New common objective added to the table: " + cardId);
        }
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        model.setFinalLeaderboard(finalLeaderboard);
        System.out.println("The final leaderboard is:");
        for (int i = 1; i <= finalLeaderboard.size(); i++) {
            for (String name : finalLeaderboard.keySet()) {
                if (finalLeaderboard.get(name) == i) {
                    System.out.println(i + ". " + name);
                }
            }
        }
    }

    @Override
    public void updateHand(int cardId, boolean removeMode) {
        if (removeMode) {
            model.removeCardFromHand(cardId);
            System.out.println("Removed the card: " + cardId + " from your hand");
        } else {
            model.addCardInHand(cardId);
            model.setiPlaced(false);
            System.out.println("You picked the card: " + cardId);
        }
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        if (removeMode) {
            model.rmPersonalObjective(cardId);
            System.out.println("Removed the objective: " + cardId + " from your personal " +
                               "objectives");
        } else {
            model.addPersonalObjective(cardId);
            System.out.println("The objective " + cardId + " is now one of your personal " +
                               "objectives");
        }
    }

    @Override
    public void receiveStarterCard(int cardId) {
        model.addStarterCard(cardId);
        System.out.println("The card " + cardId + " is now your starter card.");
    }

    @Override
    public void receiveCandidateObjective(Set<Integer> cardId) {
        System.out.println("The objective " + cardId + " is now one of the candidate objectives " +
                           "you can choose from.");
    }
}

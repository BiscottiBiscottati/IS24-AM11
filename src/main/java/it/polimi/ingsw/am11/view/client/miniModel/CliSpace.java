package it.polimi.ingsw.am11.view.client.miniModel;

import java.util.HashSet;
import java.util.Set;

public class CliSpace {

    private final Set<Integer> playerHand;
    private final Set<Integer> playerObjective;
    private Set<Integer> candidateObjectives;
    private Integer starterCard;
    private boolean starterIsRetro;

    public CliSpace() {
        this.playerHand = new HashSet();
        this.playerObjective = new HashSet<>();
    }

    public Set<Integer> getPlayerHand() {
        return playerHand;
    }

    public Set<Integer> getPlayerObjective() {
        return playerObjective;
    }

    public Set<Integer> getCandidateObjectives() {
        return candidateObjectives;
    }

    public Integer getStarterCard() {
        return starterCard;
    }

    public void setStarterCard(Integer starterCard) {
        this.starterCard = starterCard;
    }

    public void addCardInHand(int cardId) {
        playerHand.add(cardId);
    }

    public void removeCardFromHand(int cardId) {
        playerHand.remove(cardId);
    }

    public void addCandidateObjectives(Set<Integer> candidates) {
        candidateObjectives = candidates;
    }
    public void setStarterIsRetro(boolean isRetro) {
        starterIsRetro = isRetro;
    }

    public boolean getStarterIsRetro() {
        return starterIsRetro;
    }

    public void addPersonalObjective(int cardId) {
        playerObjective.add(cardId);
    }

    public void rmPersonalObjective(int cardId) {
        playerObjective.remove(cardId);
    }
}

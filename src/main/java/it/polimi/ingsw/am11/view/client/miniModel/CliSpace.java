package it.polimi.ingsw.am11.view.client.miniModel;

import it.polimi.ingsw.am11.model.utils.memento.PersonalSpaceMemento;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CliSpace {

    private final Set<Integer> playerHand;
    private final Set<Integer> playerObjective;
    private Set<Integer> candidateObjectives;
    private Integer starterCard;
    private boolean starterIsRetro;

    public CliSpace() {
        this.playerHand = new HashSet<>(6);
        this.playerObjective = new HashSet<>(2);
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

    public void addPersonalObjective(int cardId) {
        playerObjective.add(cardId);
    }

    public void rmPersonalObjective(int cardId) {
        playerObjective.remove(cardId);
    }

    public void load(@NotNull PersonalSpaceMemento memento) {
        playerHand.clear();
        playerHand.addAll(memento.hand());
        playerObjective.clear();
        playerObjective.addAll(memento.personalObjs());
        candidateObjectives = memento.candidateObjs();
        starterCard = memento.starterCard();
    }
}

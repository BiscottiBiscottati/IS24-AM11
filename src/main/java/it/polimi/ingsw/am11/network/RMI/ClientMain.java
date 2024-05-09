package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.PlayableCardType;
import it.polimi.ingsw.am11.model.table.GameStatus;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ClientMain implements ConnectorServerInterface {

    static int PORT = 1234;
    static String nick = "Bob";

    public static void main(String[] args) throws RemoteException {
        System.out.println("Hello from Client!");

        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
            // Looking up the registry for the remote object
            Loggable stub = (Loggable) registry.lookup("Loggable");
            // Calling the remote method using the obtained object
            System.out.print("insert nickname: ");
            Scanner scanner = new Scanner(System.in);
            String nick = scanner.nextLine();
            stub.login(nick);
            System.out.println("Remote method invoked");
            new Thread().start();
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Client exception: " + e);
            throw new RuntimeException(e);
        }
    }

    public void updateHand(int cardId, boolean removeMode) {
        System.out.println("updateHand: " + cardId + " " + removeMode);
    }

    @Override
    public void updatePersonalObjective(int cardId, boolean removeMode) {
        System.out.println("updatePersonalObjective: " + cardId + " " + removeMode);
    }

    @Override
    public void sendStarterCard(int cardId) {
        System.out.println("sendStarterCard: " + cardId);
    }

    @Override
    public void sendCandidateObjective(Set<Integer> cardsId) {
        System.out.println("sendCandidateObjective: " + cardsId);
    }

    @Override
    public void updateDeckTop(PlayableCardType type, Color color) {
        System.out.println("updateDeckTop: " + type + " " + color);
    }

    @Override
    public void updateField(String nickname, int x, int y, int cardId, boolean isRetro,
                            boolean removeMode) {
        System.out.println(
                "updateField: " + nickname + " " + x + " " + y + " " + cardId + " " + isRetro +
                " " + removeMode);
    }

    @Override
    public void updateShownPlayable(Integer previousId, Integer currentId) {
        System.out.println("updateShownPlayable: " + previousId + " " + currentId);
    }

    @Override
    public void updateTurnChange(String nickname) {
        System.out.println("updateTurnChange: " + nickname);
    }

    @Override
    public void updatePlayerPoint(String nickname, int points) {
        System.out.println("updatePlayerPoint: " + nickname + " " + points);
    }

    @Override
    public void updateGameStatus(GameStatus status) {
        System.out.println("updateGameStatus: " + status);
    }

    @Override
    public void updateCommonObjective(int cardId, boolean removeMode) {
        System.out.println("updateCommonObjective: " + cardId + " " + removeMode);
    }

    @Override
    public void sendFinalLeaderboard(Map<String, Integer> finalLeaderboard) {
        System.out.println("sendFinalLeaderboard: " + finalLeaderboard);
    }

    @Override
    public void updateCommonObjective(Set<Integer> cardsId, boolean removeMode) {

    }


}

package it.polimi.ingsw.am11.network.RMI;

import it.polimi.ingsw.am11.model.exceptions.GameStatusException;
import it.polimi.ingsw.am11.model.exceptions.NumOfPlayersException;
import it.polimi.ingsw.am11.model.exceptions.PlayerInitException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {

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
        } catch (NumOfPlayersException | PlayerInitException | GameStatusException e) {
            throw new RuntimeException(e);
        }
    }


}

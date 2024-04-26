package it.polimi.ingsw.am11.network.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {

    static int PORT = 1234;

    public static void main(String[] args) throws RemoteException {
        System.out.println("Hello from Client!");

        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
            // Looking up the registry for the remote object
            Loggable stub = (Loggable) registry.lookup("Loggable");
            // Calling the remote method using the obtained object
            boolean Logged = stub.login("Bob");
            System.out.println("Remote method invoked: " + Logged);
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Client exception: " + e);
            throw new RuntimeException(e);
        }
    }
}

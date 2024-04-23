package it.polimi.ingsw.am11.network.RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain implements Loggable {

    static int PORT = 1234;

    public static void main(String[] args) {
        System.out.println("Hello from Server!");

        ServerMain obj = new ServerMain();
        try {
            Loggable stub = (Loggable) UnicastRemoteObject.exportObject(
                    obj, PORT);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("Loggable", stub);
            System.err.println("Server ready");
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean login(String nick) throws RemoteException {
        System.out.println(nick + " is logging...");
        return false;
    }

    @Override
    public void logout(String nick) throws RemoteException {

    }
}

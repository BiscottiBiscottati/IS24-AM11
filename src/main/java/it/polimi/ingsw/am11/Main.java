package it.polimi.ingsw.am11;

public class Main {

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("Usage: java -jar <jarname> <server|client> [tui|gui]");
            return;
        }

        String mode = args[0];
        switch (mode.toLowerCase()) {
            case "server" -> Server.start();
            case "client" -> Client.start();
        }
    }
}

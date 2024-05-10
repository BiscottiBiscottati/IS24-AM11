package it.polimi.ingsw.am11;

public class Main {

    static final int DEFAULT_SOCKET_PORT = 12345;
    static final int DEFAULT_RMI_PORT = 12346;

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("Usage: java -jar <jarname> <server|client> [tui|gui]");
            return;
        }

        String mode = args[0];
        switch (mode.toLowerCase()) {
            case "server":

        }
    }
}

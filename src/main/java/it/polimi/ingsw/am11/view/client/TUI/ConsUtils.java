package it.polimi.ingsw.am11.view.client.TUI;

public abstract class ConsUtils {

    public static void clear() {
        // Check the operating system
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                // Enable ANSI escape codes on Windows
                new ProcessBuilder("cmd", "/c", "echo \033[H\033[2J").inheritIO().start().waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Unix-based systems
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static void card() {
        System.out.println("""
                                   ╔═══╤═══════════╤═══╗
                                   ║ W │           │ W ║
                                   ╟───┘           └───╢                   
                                   ║                   ║
                                   ╟───┐           ┌───╢
                                   ║ W │           │ W ║
                                   ╚═══╧═══════════╧═══╝                                
                                   """);
    }
}

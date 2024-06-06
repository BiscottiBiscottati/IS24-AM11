package it.polimi.ingsw.am11.view.client.TUI.printers;

import org.jetbrains.annotations.Nullable;

public class InfoBarPrinter {
    public static void printInfoBar(@Nullable String line1, @Nullable String line2,
                                    @Nullable String line3) {
        System.out.println("""
                                   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                   """ + line1 + """
                                   """ + line2 + """
                                   """ + line3 + """
                                    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                   \s""");
    }

    public static void printInfoBar(@Nullable String line1) {
        System.out.println("""
                                   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                                   """ + line1 + """
                                                                      
                                    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                   \s""");
    }

}

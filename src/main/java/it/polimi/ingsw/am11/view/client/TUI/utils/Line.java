package it.polimi.ingsw.am11.view.client.TUI.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Line extends PrintComposite {
    public Line(@NotNull List<Part> parts) {
        parts.forEach(this::add);
    }

    @Override
    protected void printThisAfter() {
        System.out.println();
    }
}

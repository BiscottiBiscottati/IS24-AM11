package it.polimi.ingsw.am11.view.client.TUI.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Matrix extends PrintComposite {
    public Matrix(@NotNull List<Line> lines) {
        lines.forEach(this::add);
    }
}

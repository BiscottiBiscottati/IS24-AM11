package it.polimi.ingsw.am11.view.client.TUI.utils;

import java.util.ArrayList;
import java.util.List;

public class PrintComposite {
    private final List<PrintComposite> children = new ArrayList<>(16);

    protected PrintComposite() {
    }

    public void add(PrintComposite letter) {
        children.add(letter);
    }

    public int count() {
        return children.size();
    }

    protected void printThisBefore() {
    }

    protected void printThisAfter() {
    }

    public void print() {
        printThisBefore();
        children.forEach(PrintComposite::print);
        printThisAfter();
    }
}

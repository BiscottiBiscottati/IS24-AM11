package it.polimi.ingsw.am11.view.client.TUI.utils;

public class Part extends PrintComposite {
    private final String content;

    public Part(String content) {
        this.content = content;
    }

    @Override
    protected void printThisBefore() {
        System.out.print(content);
    }
}

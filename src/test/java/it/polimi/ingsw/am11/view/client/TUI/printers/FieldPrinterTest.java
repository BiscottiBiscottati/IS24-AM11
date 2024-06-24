package it.polimi.ingsw.am11.view.client.TUI.printers;

import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.client.miniModel.CliField;
import it.polimi.ingsw.am11.view.client.miniModel.exceptions.SyncIssueException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FieldPrinterTest {


    static CliField field;
    FieldPrinter renderer;

    @BeforeAll
    static void beforeAll() throws SyncIssueException {
        field = new CliField();
        field.place(new Position(0, 0), 97, true);
        field.place(new Position(- 1, 1), 76, true);
        field.place(new Position(0, 2), 20, false);
    }

    @Test
    void render() throws IllegalCardBuildException {
        FieldPrinter.render(field, true);
        CardPrinter.printCardFrontAndBack(100);
        System.out.println(100);
        CardPrinter.printCardFrontAndBack(2);
        System.out.println(2);
        CardPrinter.printCardFrontAndBack(3);
        System.out.println(3);
        CardPrinter.printCardFrontAndBack(5);
        System.out.println(5);
        CardPrinter.printCardFrontAndBack(6);
        System.out.println(6);
    }
}
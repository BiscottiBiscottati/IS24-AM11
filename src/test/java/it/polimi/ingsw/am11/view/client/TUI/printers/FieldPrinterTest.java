package it.polimi.ingsw.am11.view.client.TUI.printers;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.client.miniModel.CliField;
import it.polimi.ingsw.am11.view.client.miniModel.MiniCardContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldPrinterTest {


    FieldPrinter renderer;

    static CliField field;

    @BeforeAll
    static void beforeAll() {
        field = new CliField();
        field.place(new Position(0, 0), 100, true);
        field.place(new Position(- 1, - 1), 2, false);
        field.place(new Position(1, 1), 3, false);
        field.place(new Position(- 1, 1), 5, false);
        field.place(new Position(2, 2), 6, false);
    }

    @Test
    void render() throws IllegalCardBuildException {
        FieldPrinter.render(field, false);
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
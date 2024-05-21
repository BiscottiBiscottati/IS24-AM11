package it.polimi.ingsw.am11.view.client.TUI.printers;

import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.client.miniModel.CliField;
import it.polimi.ingsw.am11.view.client.miniModel.MiniCardContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FieldPrinterTest {

    static Map<Position, MiniCardContainer> testField;

    FieldPrinter renderer;

    @Mock
    CliField field;

    @BeforeAll
    static void beforeAll() {
        MiniCardContainer starter = new MiniCardContainer(100, true);
        Stream.of(Corner.values())
              .filter(corner -> corner != Corner.DOWN_RX)
              .forEach(starter::cover);

        testField = Map.of(
                new Position(0, 0), starter,
                new Position(- 1, - 1), new MiniCardContainer(2, false),
                new Position(1, 1), new MiniCardContainer(3, false),
                new Position(- 1, 1), new MiniCardContainer(5, false)
        );
    }

    @BeforeEach
    void setUp() {
        renderer = new FieldPrinter(field);
    }

    @Test
    void render() throws IllegalCardBuildException {
        when(field.getCardsPositioned()).thenReturn(testField);
        renderer.render();
    }
}
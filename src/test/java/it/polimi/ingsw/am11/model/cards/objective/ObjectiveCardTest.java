package it.polimi.ingsw.am11.model.cards.objective;

import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.GameColor;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ObjectiveCardTest {

    ObjectiveCard colorCollectCard;
    ObjectiveCard symbolCollectCard;
    ObjectiveCard triplet;
    ObjectiveCard lCard;

    @BeforeEach
    void setUp() {
        try {
            colorCollectCard = new ColorCollectCard.Builder(10, 2)
                    .hasColor(GameColor.RED, 3)
                    .build();
            symbolCollectCard = new SymbolCollectCard.Builder(11, 2)
                    .hasSymbol(Symbol.GLASS, 2)
                    .build();
            triplet = new TripletCard.Builder(12, 2)
                    .hasColor(GameColor.GREEN)
                    .build();
            lCard = new LCard.Builder(13, 2)
                    .hasPrimaryColor(GameColor.GREEN)
                    .hasSecondaryColor(GameColor.RED)
                    .build();
        } catch (IllegalCardBuildException e) {
            fail(e);
        }
    }

    @Test
    void hasItemRequirements() {
        assertEquals(3, colorCollectCard.hasItemRequirements(GameColor.RED));
        Stream.concat(Arrays.stream(GameColor.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != GameColor.RED)
              .forEach(item -> assertEquals(0, colorCollectCard.hasItemRequirements(item)));

        assertEquals(2, symbolCollectCard.hasItemRequirements(Symbol.GLASS));
        Stream.concat(Arrays.stream(GameColor.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != Symbol.GLASS)
              .forEach(item -> assertEquals(0, symbolCollectCard.hasItemRequirements(item)));

        assertEquals(3, triplet.hasItemRequirements(GameColor.GREEN));
        Stream.concat(Arrays.stream(GameColor.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != GameColor.GREEN)
              .forEach(item -> assertEquals(0, triplet.hasItemRequirements(item)));

        assertEquals(2, lCard.hasItemRequirements(GameColor.GREEN));
        assertEquals(1, lCard.hasItemRequirements(GameColor.RED));
        Stream.concat(Arrays.stream(GameColor.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != GameColor.GREEN && item != GameColor.RED)
              .forEach(item -> assertEquals(0, lCard.hasItemRequirements(item)));
    }
}
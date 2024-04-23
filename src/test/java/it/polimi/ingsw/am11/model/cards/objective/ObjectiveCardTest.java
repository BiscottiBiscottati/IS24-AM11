package it.polimi.ingsw.am11.model.cards.objective;

import it.polimi.ingsw.am11.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.ColorCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.collecting.SymbolCollectCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.LCard;
import it.polimi.ingsw.am11.model.cards.objective.positioning.TripletCard;
import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Symbol;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectiveCardTest {

    ObjectiveCard colorCollectCard;
    ObjectiveCard symbolCollectCard;
    ObjectiveCard triplet;
    ObjectiveCard lCard;

    @BeforeEach
    void setUp() {
        try {
            colorCollectCard = new ColorCollectCard.Builder(10, 2)
                    .hasColor(Color.RED, 3)
                    .build();
            symbolCollectCard = new SymbolCollectCard.Builder(11, 2)
                    .hasSymbol(Symbol.GLASS, 2)
                    .build();
            triplet = new TripletCard.Builder(12, 2)
                    .hasColor(Color.GREEN)
                    .build();
            lCard = new LCard.Builder(13, 2)
                    .hasPrimaryColor(Color.GREEN)
                    .hasSecondaryColor(Color.RED)
                    .build();
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void hasItemRequirements() {
        assertEquals(3, colorCollectCard.hasItemRequirements(Color.RED));
        Stream.concat(Arrays.stream(Color.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != Color.RED)
              .forEach(item -> assertEquals(0, colorCollectCard.hasItemRequirements(item)));

        assertEquals(2, symbolCollectCard.hasItemRequirements(Symbol.GLASS));
        Stream.concat(Arrays.stream(Color.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != Symbol.GLASS)
              .forEach(item -> assertEquals(0, symbolCollectCard.hasItemRequirements(item)));

        assertEquals(3, triplet.hasItemRequirements(Color.GREEN));
        Stream.concat(Arrays.stream(Color.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != Color.GREEN)
              .forEach(item -> assertEquals(0, triplet.hasItemRequirements(item)));

        assertEquals(2, lCard.hasItemRequirements(Color.GREEN));
        assertEquals(1, lCard.hasItemRequirements(Color.RED));
        Stream.concat(Arrays.stream(Color.values()), Arrays.stream(Symbol.values()))
              .filter(item -> item != Color.GREEN && item != Color.RED)
              .forEach(item -> assertEquals(0, lCard.hasItemRequirements(item)));
    }
}
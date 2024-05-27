package it.polimi.ingsw.am11.view.client.TUI.printers;

import com.google.common.base.Strings;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.helpers.MatrixFiller;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.client.miniModel.CliField;
import it.polimi.ingsw.am11.view.client.miniModel.MiniCardContainer;
import it.polimi.ingsw.am11.view.client.miniModel.MiniGameModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FieldPrinter {
    public static final String HORIZONTAL_SEPARATOR = "─";
    public static final String VERTICAL_SEPARATOR = "│";
    public static final int CARD_WIDTH = 3;


    public static void render(CliField field, boolean withSuggestions) {

        Map<Position, MiniCardContainer> cardsPositioned = field.getCardsPositioned();

        if (cardsPositioned.isEmpty()) return;

        List<List<MiniCardContainer>> matrixOfCards = intoMatrix(cardsPositioned);

        int sizeY = (matrixOfCards.size() << 1) + 1;
        int sizeX = (matrixOfCards.getFirst().size() << 1) + 1;
        List<List<String>> printMatrix = new ArrayList<>(sizeY);
        for (int i = 0; i < sizeY; i++) {
            printMatrix.add(new ArrayList<>(sizeX));
        }

        // fill the matrix with empty spaces
        for (List<String> strings : printMatrix) {
            for (int j = 0; j < sizeX; j++) {
                strings.add(" ".repeat(j % 2 == 0 ? 1 : CARD_WIDTH));
            }
        }

        int rowIndex = 1;
        for (List<MiniCardContainer> row : matrixOfCards) {
            int columnIndex = 0;
            for (MiniCardContainer card : row) {
                if (card != null) {
                    setCard(printMatrix, rowIndex, columnIndex, card);
                }
                columnIndex += 2;
            }
            rowIndex += 2;
        }

        // Print the matrix
        for (List<String> strings : printMatrix) {
            System.out.println(String.join("", strings));
        }

    }

    private static @NotNull List<List<MiniCardContainer>> intoMatrix(
            @NotNull Map<Position, MiniCardContainer> cardsPositioned) {
        int minX = cardsPositioned.keySet().stream()
                                  .mapToInt(Position::x)
                                  .min().orElseThrow();
        int maxX = cardsPositioned.keySet().stream()
                                  .mapToInt(Position::x)
                                  .max().orElseThrow();

        int minY = cardsPositioned.keySet().stream()
                                  .mapToInt(Position::y)
                                  .min().orElseThrow();
        int maxY = cardsPositioned.keySet().stream()
                                  .mapToInt(Position::y)
                                  .max().orElseThrow();

        int rangeX = maxX - minX + 1;
        int rangeY = maxY - minY + 1;

        List<List<MiniCardContainer>> matrixOfCards =
                MatrixFiller.fillMatrixWithNull(rangeY, rangeX, MiniCardContainer.class);

        cardsPositioned.forEach(
                (pos, card) -> matrixOfCards.get(pos.y() - minY).set(pos.x() - minX, card));

        int size = matrixOfCards.size();
        for (int i = 0; i < size / 2; i++) {
            List<MiniCardContainer> temp = matrixOfCards.get(i);
            matrixOfCards.set(i, matrixOfCards.get(size - i - 1));
            matrixOfCards.set(size - i - 1, temp);
        }
        return matrixOfCards;
    }

    private static void setCard(@NotNull List<List<String>> printMatrix, int rowIndex,
                                int columnIndex, @NotNull MiniCardContainer card) {
        setCorner(printMatrix, rowIndex - 1, columnIndex, card, Corner.TOP_LX);

        String separator = HORIZONTAL_SEPARATOR.repeat(CARD_WIDTH);

        printMatrix.get(rowIndex - 1).set(columnIndex + 1, separator);
        setCorner(printMatrix, rowIndex - 1, columnIndex + 2, card, Corner.TOP_RX);

        printMatrix.get(rowIndex).set(columnIndex, VERTICAL_SEPARATOR);
        printMatrix.get(rowIndex).set(columnIndex + 1,
                                      Strings.padStart(String.valueOf(card.getCardId()),
                                                       CARD_WIDTH,
                                                       ' '));
        printMatrix.get(rowIndex).set(columnIndex + 2, VERTICAL_SEPARATOR);

        setCorner(printMatrix, rowIndex + 1, columnIndex, card, Corner.DOWN_LX);
        printMatrix.get(rowIndex + 1).set(columnIndex + 1, separator);
        setCorner(printMatrix, rowIndex + 1, columnIndex + 2, card, Corner.DOWN_RX);
    }

    private static void setCorner(@NotNull List<List<String>> printMatrix, int rowIndex,
                                  int columnIndex, @NotNull MiniCardContainer card, Corner corner) {
        if (! card.isCovered(corner)) {
            printMatrix.get(rowIndex).set(columnIndex, card.getContainerOn(corner).getTUICode());
        }
    }
}

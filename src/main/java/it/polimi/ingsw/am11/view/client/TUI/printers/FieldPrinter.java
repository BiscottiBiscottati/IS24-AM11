package it.polimi.ingsw.am11.view.client.TUI.printers;


import it.polimi.ingsw.am11.model.cards.utils.enums.Color;
import it.polimi.ingsw.am11.model.cards.utils.enums.Corner;
import it.polimi.ingsw.am11.model.cards.utils.helpers.MatrixFiller;
import it.polimi.ingsw.am11.model.exceptions.IllegalCardBuildException;
import it.polimi.ingsw.am11.model.players.utils.Position;
import it.polimi.ingsw.am11.view.client.miniModel.CliField;
import it.polimi.ingsw.am11.view.client.miniModel.MiniCardContainer;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldPrinter {
    public static final String HORIZONTAL_SEPARATOR = "─";
    public static final String VERTICAL_SEPARATOR = "│";
    public static final String VERTICAL_COORD_SEP = "╎";
    public static final int CARD_WIDTH = 3;


    public static void render(CliField field, boolean withSuggestions) {

        //
        //  X───R   X───+    ╔╔══R   ┌┬─┬┐ R┬─┬┐
        //  │ R │   │  3│    ╠╝   ║  ├┘ └┤ ├┤B├┤
        //  +─5─R───R───+            └┴─┴┘ └┴─┴┘
        //      │100│                    ├  100^
        //  X───F───P╌╌╌
        //  │  2│ ╎-14;-19╎
        //  G───R    ╌╌╌

        //  ┌──┬R
        //  ├┐B├┤
        //  └┴─┴R┬─┬R
        //    5 ├RGB┤
        //      └┴─┴┘
        //       100
        //

        int i;
        int j;
        int x;
        int y;

        Map<Position, MiniCardContainer> cardsPositioned = field.getCardsPositioned();

        if (cardsPositioned.isEmpty()) {
            return;
        }

        //Get a matrix with all the positions of the cards
        List<List<MiniCardContainer>> matrixOfCards = intoContainerMatrix(cardsPositioned);
        List<List<Position>> matrixOfPositions = intoPositionsMatrix(cardsPositioned);

        for (List<MiniCardContainer> strings : matrixOfCards) {
            System.out.println(strings);
        }
        System.out.println("jdsnfkasjdf lajsf ");

        for (List<Position> strings : matrixOfPositions) {
            System.out.println(strings);
        }


        int PosSize = matrixOfPositions.size();
        int sizeY = (PosSize << 1) + 1;
        int sizeX = (matrixOfPositions.getFirst().size() << 1) + 1;

        List<List<String>> printMatrix = new ArrayList<>(sizeY + 2);
        for (i = 0; i < sizeY; i++) {
            printMatrix.add(new ArrayList<>(sizeX + 8));
        }

        // fill the printMatrix with empty spaces
        for (List<String> strings : printMatrix) {
            for (j = 0; j < sizeX; j++) {
                strings.add(" ".repeat(j % 2 == 1 ? 1 : CARD_WIDTH));
                //  ░░░░░░░░░░░░░░░░░░░+───X
                //  ░╎-14;-19╎-14;-19╎░│  6│
                //  ░░░░░░░X───R-19X───+───R
                //  ░░░░░░░│  5│░░░│  3│░░-14;-19
                //  ░░░░-14+───R───R───+░░░░
                //  ░░░░░░░░░░░│100│░░░░░░░░
                //  ░░░░░░░X───F───P░░░░░░░░
                //  ░░░░░░░│  2│░░░░░░░░░░░░
                //  ░░░░░░░G───R░░░░░░░░░░░░
                //  +++-+++-+++-+++-+++-+++-+++-+++-
                //   0 1 2 3 4 5 6 7 8 9 A B
            }
        }

        int rowIndex;
        int columnIndex;

        rowIndex = 1;
        for (y = 0; y < PosSize; y++) {
            columnIndex = 2;
            for (x = 0; x < PosSize; x++) {
                // Fill the border
                if (isBorder(matrixOfPositions, x, y) &&
                    matrixOfPositions.get(y).get(x) != null &&
                    withSuggestions) {
                    setCoordinates(matrixOfPositions.get(y).get(x),
                                   rowIndex,
                                   columnIndex,
                                   printMatrix);
                }
                // Fill center with coord
                else if (withSuggestions && matrixOfPositions.get(y).get(x) != null &&
                         matrixOfCards.get(y - 1).get(x - 1) == null) {
                    setCoordinates(matrixOfPositions.get(y).get(x),
                                   rowIndex,
                                   columnIndex,
                                   printMatrix);
                }
                // Fill with cards
                else if (! isBorder(matrixOfPositions, x, y) &&
                         matrixOfCards.get(y - 1).get(x - 1) != null) {
                    setCard(printMatrix, rowIndex, columnIndex - 1,
                            matrixOfCards.get(y - 1).get(x - 1));
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

    private static void setCoordinates(@NotNull Position pos, int rowIndex, int columnIndex,
                                       @NotNull List<List<String>> printMatrix) {

        char[] xVal = String.valueOf(pos.x()).toCharArray();
        char[] yVal = String.valueOf(pos.y()).toCharArray();

        if (xVal.length > 3 || yVal.length > 3) {
            throw new RuntimeException("Value of position of card is out of bound ( > 3)");
        }

        printMatrix.get(rowIndex).set(columnIndex, xVal[xVal.length - 1] +
                                                   ";" +
                                                   yVal[0]);

        if (xVal.length > 1) {
            printMatrix.get(rowIndex).set(columnIndex - 1, String.valueOf(xVal[xVal.length - 2]));
        }
        if (xVal.length > 2) {
            printMatrix.get(rowIndex).set(columnIndex - 2,
                                          " " + VERTICAL_COORD_SEP + xVal[0]);
        }
        if (yVal.length > 1) {
            printMatrix.get(rowIndex).set(columnIndex + 1, String.valueOf(yVal[1]));
        }
        if (yVal.length > 2) {
            printMatrix.get(rowIndex).set(columnIndex + 2,
                                          yVal[2] + VERTICAL_COORD_SEP + " ");
        }
    }

    private static @NotNull List<List<Position>> intoPositionsMatrix(@NotNull Map<Position,
            MiniCardContainer> cardsPositioned) {
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

        int rangeX = maxX - minX + 1 + 2;
        int rangeY = maxY - minY + 1 + 2;

        List<List<Position>> matrixOfPositions =
                MatrixFiller.fillMatrixWithNull(rangeY, rangeX, Position.class);

        cardsPositioned.forEach(
                (pos, card) -> {
                    matrixOfPositions.get(pos.y() - minY + 1).set(pos.x() - minX + 1, pos);

                    matrixOfPositions.get(pos.y() + 1 - minY + 1).set(pos.x() + 1 - minX + 1,
                                                                      new Position(pos.x() + 1,
                                                                                   pos.y() + 1));
                    matrixOfPositions.get(pos.y() + 1 - minY + 1).set(pos.x() - 1 - minX + 1,
                                                                      new Position(pos.x() - 1,
                                                                                   pos.y() + 1));
                    matrixOfPositions.get(pos.y() - 1 - minY + 1).set(pos.x() + 1 - minX + 1,
                                                                      new Position(pos.x() + 1,
                                                                                   pos.y() - 1));
                    matrixOfPositions.get(pos.y() - 1 - minY + 1).set(pos.x() - 1 - minX + 1,
                                                                      new Position(pos.x() - 1,
                                                                                   pos.y() - 1));

                }
        );

        int size = matrixOfPositions.size();

        // Reverse the matrix to have the first row on top
        for (int i = 0; i < size / 2; i++) {
            List<Position> temp = matrixOfPositions.get(i);
            matrixOfPositions.set(i, matrixOfPositions.get(size - i - 1));
            matrixOfPositions.set(size - i - 1, temp);
        }
        return matrixOfPositions;

    }

    private static @NotNull List<List<MiniCardContainer>> intoContainerMatrix(
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

        // Reverse the matrix to have the first row on top
        for (int i = 0; i < size / 2; i++) {
            List<MiniCardContainer> temp = matrixOfCards.get(i);
            matrixOfCards.set(i, matrixOfCards.get(size - i - 1));
            matrixOfCards.set(size - i - 1, temp);
        }
        return matrixOfCards;
    }


    private static void setCard(@NotNull List<List<String>> printMatrix, int rowIndex,
                                int columnIndex, @NotNull MiniCardContainer card) {
        //TOP_LX corner
        setCorner(printMatrix, rowIndex - 1, columnIndex, card, Corner.TOP_LX);
        // top separator
        String separator = HORIZONTAL_SEPARATOR.repeat(CARD_WIDTH);
        printMatrix.get(rowIndex - 1).set(columnIndex + 1, separator);
        //TOP_RX corner
        setCorner(printMatrix, rowIndex - 1, columnIndex + 2, card, Corner.TOP_RX);

        // left  vertical separator
        printMatrix.get(rowIndex).set(columnIndex, VERTICAL_SEPARATOR);
        // center
        String centers;
        try {
            Set<Color> colors =
                    CardPrinter.getFieldCard(card.getCardId()).getCenter(card.isRetro());
            centers = colors.stream().map(Color::getTUICode).reduce("", String::concat);
        } catch (IllegalCardBuildException e) {
            throw new RuntimeException(e);
        }
        printMatrix.get(rowIndex).set(columnIndex + 1,
                                      centerStr(centers, CARD_WIDTH, ' '));
        // right vertical separator
        printMatrix.get(rowIndex).set(columnIndex + 2, VERTICAL_SEPARATOR);

        //DOWN_LX corner
        setCorner(printMatrix, rowIndex + 1, columnIndex, card, Corner.DOWN_LX);
        // down separator
        printMatrix.get(rowIndex + 1).set(columnIndex + 1,
                                          centerStr(String.valueOf(card.getCardId())
                                                  , CARD_WIDTH, HORIZONTAL_SEPARATOR.charAt(0)));
        //DOWN_RX corner
        setCorner(printMatrix, rowIndex + 1, columnIndex + 2, card, Corner.DOWN_RX);
    }

    private static void setCorner(@NotNull List<List<String>> printMatrix, int rowIndex,
                                  int columnIndex, @NotNull MiniCardContainer card, Corner corner) {

        if (! card.isCovered(corner)) {
            printMatrix.get(rowIndex).set(columnIndex, card.getContainerOn(corner).getTUICode());
        }

    }

    private static @NotNull String centerStr(@NotNull String string, int width, char padChar) {
        String strChar = String.valueOf(padChar);
        return strChar.repeat((width - string.length() + 1) / 2) + string +
               strChar.repeat((width - string.length()) / 2);
    }

    private static <T> boolean isBorder(List<List<T>> matrix, int x, int y) {
        return x == 0 || y == 0 || x == matrix.getFirst().size() - 1 ||
               y == matrix.size() - 1;
    }


}

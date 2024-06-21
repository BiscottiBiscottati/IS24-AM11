package it.polimi.ingsw.am11.model.cards.utils.helpers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MatrixFiller {
    private MatrixFiller() {
    }


    // Fill a matrix with null values
    public static <T> @NotNull List<List<T>> fillMatrixWithNull(int rows, int cols, Class<T> type) {
        List<List<T>> matrix = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(cols);
            for (int j = 0; j < cols; j++) {
                row.add(null);
            }
            matrix.add(row);
        }
        return matrix;
    }
}

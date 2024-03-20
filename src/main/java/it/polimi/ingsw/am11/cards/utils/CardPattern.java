package it.polimi.ingsw.am11.cards.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public record CardPattern(Color[][] pattern) {
    @Contract("_ -> new")
    public static @NotNull CardPattern of(List<List<Color>> matrix) {
        Color[][] temp = new Color[3][3];
        IntStream.range(0, 3).forEach(
                x -> IntStream.range(0, 3).forEach(
                        y -> temp[x][y] = matrix.get(x).get(y)
                )
        );
        return new CardPattern(temp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardPattern) {
            return IntStream.range(0, 3)
                            .filter(
                                    value -> !Arrays.equals(this.pattern[value], ((CardPattern) obj).pattern()[value])
                            )
                            .findAny()
                            .isEmpty();
        } else return false;
    }
}

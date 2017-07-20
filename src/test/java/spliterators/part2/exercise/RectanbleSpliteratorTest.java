package spliterators.part2.exercise;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;
import spliterators.part1.exercise.RectangleSpliterator;

import static org.junit.Assert.assertEquals;

public class RectanbleSpliteratorTest {
    private int[][] getRandomArray(int width, int height) {
        final int[][] result = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = ThreadLocalRandom.current().nextInt();
            }
        }

        return result;
    }

    private static class Pair<F, S> {
        private F first;
        private S second;
        Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
        public F getFirst() { return first; }
        public S getSecond() { return second; }
        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("first", first)
                    .append("second", second)
                    .toString();
        }
        @Override
        public boolean equals(Object p) {
            return Pair.class.isInstance(p) &&
                    this.first.equals(((Pair) p).first)
                && this.second.equals(((Pair) p).second);
        }
    }

    @Test
    public void comparePair() {
        int width = 203, height = 104;
        final int[][] randomArray = getRandomArray(width, height);

        Integer expected = 0;
        for (int[] ints : randomArray) {
            for (int i : ints) {
                expected += i + 1;
            }
        }

        final Pair<Integer, String> actual =
                StreamSupport.stream(new RectangleSpliterator(randomArray), true)
                        .map(p -> new Pair<>(p + 1, p.toString()))
                        .reduce((p1, p2) -> new Pair<>(p1.getFirst() + p2.getFirst(), "String"))
                        .orElse(new Pair<>(0, ""));

        assertEquals(expected, actual.getFirst());
    }

    @Test
    public void zeroLength() {
        int width = 0, height = 0;
        final int[][] randomArray = getRandomArray(width, height);

        final Pair<Integer, String> expected = new Pair<>(0, "");

        final Pair<Integer, String> actual =
                StreamSupport.stream(new RectangleSpliterator(randomArray), true)
                .map(p -> new Pair<>(p + 1, p.toString()))
                .reduce((p1, p2) -> new Pair<>(p1.getFirst() + p2.getFirst(), "String"))
                                        .orElse(new Pair<>(0, ""));

        assertEquals(expected, actual);
    }

    @Test
    public void compareInts() {
        int width = 100, height = 100;
        final int[][] randomArray = getRandomArray(width, height);

        Integer expected = 0;
        for (int[] ints : randomArray) {
            for (int i : ints) {
                expected += i;
            }
        }

        final Integer actual =
                StreamSupport.stream(new RectangleSpliterator(randomArray), true)
                        .reduce((p1, p2) -> p1 + p2)
                        .orElse(0);

        assertEquals(expected, actual);
    }
}

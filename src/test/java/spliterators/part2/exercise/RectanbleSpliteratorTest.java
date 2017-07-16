package spliterators.part2.exercise;

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
    }

    @Test
    public void comparePair() {
        int width = 100, height = 100;
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
                                        .orElseThrow(NullPointerException::new);

        assertEquals(expected, actual.getFirst());
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
                        .orElseThrow(NullPointerException::new);

        assertEquals(expected, actual);
    }
}

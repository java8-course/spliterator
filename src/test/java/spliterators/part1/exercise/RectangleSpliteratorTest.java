package spliterators.part1.exercise;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class RectangleSpliteratorTest {

    private static final int LENGTH = 1000;
    private final int[][] randomArray = getRandomArray(LENGTH);
    private final int[][] singleElementArray = getRandomArray(1);

    private static int[][] getRandomArray(int length) {
        final int[][] result = new int[length][length];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                result[i][j] = ThreadLocalRandom.current().nextInt();
            }
        }
        return result;
    }

    @Test
    public void compareArrays() {
        final RectangleSpliterator spliterator = new RectangleSpliterator(randomArray);
        final List<Integer> result1 =
                Arrays.stream(randomArray)
                        .flatMapToInt(Arrays::stream)
                        .boxed()
                        .collect(toList());

        final List<Integer> result2 =
        StreamSupport.stream(spliterator, true)
                .map(p -> p + 1)
                .map(p -> p - 1)
                .collect(toList());

        assertEquals(result1, result2);
    }

    @Test
    public void estimateSize() {
        final RectangleSpliterator spliterator = new RectangleSpliterator(randomArray);
        assertEquals(LENGTH*LENGTH, spliterator.estimateSize());
    }

    @Test
    public void trySplitInHalf() {
        final RectangleSpliterator spliterator = new RectangleSpliterator(randomArray);
        final Spliterator.OfInt split = spliterator.trySplit();

        assertEquals(LENGTH*LENGTH / 2, spliterator.estimateSize());
        assertEquals(LENGTH*LENGTH / 2, split.estimateSize());
        assertNotNull(split);
    }

    @Test
    public void forEachRemaining() {
        final RectangleSpliterator spliterator = new RectangleSpliterator(randomArray);
        final List<Integer> expected = new ArrayList<>();
        final IntConsumer c = expected::add;

        spliterator.forEachRemaining(c);

        assertEquals(0, spliterator.estimateSize());
        assertEquals(expected, Arrays.stream(randomArray)
                .flatMapToInt(Arrays::stream)
                .boxed()
                .collect(toList()));
    }

    @Test
    public void trySplitForSingleElementArray() {
        final RectangleSpliterator emptySpliterator = new RectangleSpliterator(singleElementArray);
        final Spliterator.OfInt split = emptySpliterator.trySplit();

        assertNull(split);
    }

    @Test
    public void tryAdvanceForSingleElementArray() {
        final RectangleSpliterator emptySpliterator = new RectangleSpliterator(singleElementArray);
        final int[][] expected = new int[1][1];
        final IntConsumer c = (x) -> expected[0][0] = x;

        assertTrue(emptySpliterator.tryAdvance(c));
        assertFalse(emptySpliterator.tryAdvance(c));
        assertArrayEquals(expected, singleElementArray);
    }

}

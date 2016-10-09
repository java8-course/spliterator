package spliterators.part1.exercise;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class RectangleSpliteratorTest {

    @Parameters(name = "firstArrayLength = {0}")
    public static Collection<Object> data() {
        return Arrays.asList(new Object[]
                {0, 1, 2, 3, 4, 1000}
        );
    }

    @Parameter
    public int length;
    private int[][] randomArray;
    private RectangleSpliterator spliterator;

    private List<Integer> arrayToList(int[][] array) {
        return arrayToList(array, 0, array.length);
    }

    private List<Integer> arrayToList(int[][] array, int skip, int limit) {
        return Arrays.stream(array)
                .skip(skip)
                .limit(limit)
                .flatMapToInt(Arrays::stream)
                .boxed()
                .collect(toList());
    }

    private List<Integer> tryAdvance(Spliterator.OfInt spliterator) {
        return StreamSupport.stream(spliterator, false)
                .collect(toList());
    }

    @Before
    public void getRandomArray() {
        randomArray = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                randomArray[i][j] = ThreadLocalRandom.current().nextInt();
            }
        }
        spliterator = new RectangleSpliterator(randomArray);
    }

    @Test
    public void compareArrays() {
        final List<Integer> result1 =
                arrayToList(randomArray);

        final List<Integer> result2 =
                StreamSupport.stream(spliterator, true)
                        .map(p -> p + 1)
                        .map(p -> p - 1)
                        .collect(toList());

        assertEquals(result1, result2);
    }

    @Test
    public void estimateSize() {
        assertEquals(length * length, spliterator.estimateSize());
    }

    @Test
    public void forEachRemaining() {
        final List<Integer> result = new ArrayList<>();
        final IntConsumer c = result::add;

        spliterator.forEachRemaining(c);

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList(randomArray));
    }

    @Test
    public void tryAdvance() {
        final List<Integer> result = new ArrayList<>();
        final IntConsumer c = result::add;

        while (spliterator.tryAdvance(c));

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList(randomArray));
    }

    @Test
    public void trySplitInHalf() {
        final Spliterator.OfInt split = spliterator.trySplit();
        final int rightLength = (length % 2 == 0) ? length / 2 : length / 2 + 1;
        final int leftLength = length / 2;

        if (length <= 1) {
            assertNull(split);
        } else {
            assertEquals(leftLength * length, split.estimateSize());
            final List<Integer> leftExpected = arrayToList(randomArray, 0, leftLength);
            final List<Integer> leftResult = tryAdvance(split);
            assertEquals(leftExpected, leftResult);
        }

        assertEquals(rightLength * length, spliterator.estimateSize());

        final List<Integer> rightExpected = arrayToList(randomArray, leftLength, rightLength);
        final List<Integer> rightResult = tryAdvance(spliterator);
        assertEquals(rightExpected, rightResult);

    }

}

package spliterators.part2.exercise;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ZipWithIndexDoubleSpliteratorTest {

    @Parameters(name = "firstArrayLength = {0}")
    public static Collection<Object> data() {
        return Arrays.asList(new Object[]
                {0, 1, 2, 3, 4, 1000}
        );
    }

    @Parameter
    public int length;
    private Double[] randomArray;
    private ZipWithIndexDoubleSpliterator spliterator;

    private List<IndexedDoublePair> arrayToList(Double[] array) {
        return arrayToList(array, 0, array.length);
    }

    private List<IndexedDoublePair> arrayToList(Double[] array, int skip, int limit) {
        return Stream.iterate(
                new IndexedDoublePair(0, array.length == 0 ? 0 : array[0]),
                p -> new IndexedDoublePair(p.getIndex() + 1, array[p.getIndex() + 1]))
                .skip(skip)
                .limit(limit)
                .collect(toList());
    }

    private List<IndexedDoublePair> tryAdvance(Spliterator spliterator) {
        return (List<IndexedDoublePair>) StreamSupport
                .stream(spliterator, false).collect(toList());
    }

    @Before
    public void getRandomArray() {
        randomArray = new Double[length];
        for (int i = 0; i < length; i++) {
            randomArray[i] = ThreadLocalRandom.current().nextDouble();
        }
        spliterator = new ZipWithIndexDoubleSpliterator(
                Arrays.spliterator(
                        ArrayUtils.toPrimitive(randomArray)));
    }

    @Test
    public void comparePaired() {

        final List<IndexedDoublePair> expected =
                arrayToList(randomArray);

        final List<IndexedDoublePair> result =
        StreamSupport.stream(spliterator, true)
                .map(p -> new IndexedDoublePair(p.getIndex() + 1, p.getValue()))
                .map(p -> new IndexedDoublePair(p.getIndex() - 1, p.getValue()))
                .collect(toList());

        assertEquals(expected, result);

    }

    @Test
    public void estimateSize() {
        assertEquals(length, spliterator.estimateSize());
    }

    @Test
    public void forEachRemaining() {
        final List<IndexedDoublePair> result = new ArrayList<>();
        final Consumer<IndexedDoublePair> c = result::add;

        spliterator.forEachRemaining(c);

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList(randomArray));
    }

    @Test
    public void tryAdvance() {
        final List<IndexedDoublePair> result = new ArrayList<>();
        final Consumer<IndexedDoublePair> c = result::add;

        while (spliterator.tryAdvance(c)) ;

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList(randomArray));
    }

    @Test
    public void trySplitInHalf() {
        final Spliterator split = spliterator.trySplit();
        final int rightLength = (length % 2 == 0) ? length / 2 : length / 2 + 1;
        final int leftLength = length / 2;

        if (length <= 1) {
            assertNull(split);
        } else {
            assertEquals(leftLength, split.estimateSize());
            final List<IndexedDoublePair> leftExpected = arrayToList(randomArray, 0, leftLength);
            final List<IndexedDoublePair> leftResult = tryAdvance(split);
            assertEquals(leftExpected, leftResult);
        }

        assertEquals(rightLength, spliterator.estimateSize());

        final List<IndexedDoublePair> rightExpected = arrayToList(randomArray, leftLength, rightLength);
        final List<IndexedDoublePair> rightResult = tryAdvance(spliterator);
        assertEquals(rightExpected, rightResult);

    }

}

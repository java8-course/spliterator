package spliterators.part2.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import spliterators.part2.example.ArrayZipWithIndexExample.IndexedArraySpliterator;

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
public class IndexedArraySpliteratorTest {

    @Parameters(name = "firstArrayLength = {0}")
    public static Collection<Object> data() {
        return Arrays.asList(new Object[]
                {0, 1, 2, 3, 4, 1000}
        );
    }

    @Parameter
    public int length;
    private String[] randomArray;
    private IndexedArraySpliterator spliterator;

    private List<IndexedPair<String>> arrayToList(String[] array) {
        return arrayToList(array, 0, array.length);
    }

    private List<IndexedPair<String>> arrayToList(String[] array, int skip, int limit) {
        return Stream.iterate(
                new IndexedPair<>(0, array.length == 0 ? null : array[0]),
                p -> new IndexedPair<>(p.getIndex() + 1, array[p.getIndex() + 1]))
                .skip(skip)
                .limit(limit)
                .collect(toList());
    }

    private List<IndexedPair<String>> tryAdvance(IndexedArraySpliterator spliterator) {
        return (List<IndexedPair<String>>) StreamSupport
                .stream(spliterator, false).collect(toList());
    }

    @Before
    public void getRandomArray() {
        randomArray = new String[length];
        for (int i = 0; i < length; i++) {
            randomArray[i] = String.valueOf(ThreadLocalRandom.current().nextInt());
        }
        spliterator = new IndexedArraySpliterator<>(randomArray);
    }

    @Test
    public void comparePaired() {
        final List<IndexedPair<String>> expected =
                arrayToList(randomArray);

        final List<IndexedPair<String>> result =
                StreamSupport.stream(new IndexedArraySpliterator<>(randomArray), true)
                        .map(p -> new IndexedPair<>(p.getIndex() + 1, p.getValue()))
                        .map(p -> new IndexedPair<>(p.getIndex() - 1, p.getValue()))
                        .collect(toList());

        assertEquals(expected, result);
    }

    @Test
    public void estimateSize() {
        assertEquals(length, spliterator.estimateSize());
    }

    @Test
    public void forEachRemaining() {
        final List<IndexedPair<String>> result = new ArrayList<>();
        final Consumer<IndexedPair<String>> c = result::add;

        spliterator.forEachRemaining(c);

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList(randomArray));
    }

    @Test
    public void tryAdvance() {
        final List<IndexedPair<String>> result = new ArrayList<>();
        final Consumer<IndexedPair<String>> c = result::add;

        while (spliterator.tryAdvance(c)) ;

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList(randomArray));
    }

    @Test
    public void trySplitInHalf() {
        final IndexedArraySpliterator split = spliterator.trySplit();
        final int rightLength = (length % 2 == 0) ? length / 2 : length / 2 + 1;
        final int leftLength = length / 2;

        if (length <= 1) {
            assertNull(split);
        } else {
            assertEquals(leftLength, split.estimateSize());
            final List<IndexedPair<String>> leftExpected = arrayToList(randomArray, 0, leftLength);
            final List<IndexedPair<String>> leftResult = tryAdvance(split);
            assertEquals(leftExpected, leftResult);
        }

        assertEquals(rightLength, spliterator.estimateSize());

        final List<IndexedPair<String>> rightExpected = arrayToList(randomArray, leftLength, rightLength);
        final List<IndexedPair<String>> rightResult = tryAdvance(spliterator);
        assertEquals(rightExpected, rightResult);

    }

}

package spliterators.part3.exercise;

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
public class ZipWithArraySpliteratorTest {

    @Parameters(name = "firstArrayLength = {0}; secondArrayLength = {1}")
    public static Collection<Object> data() {
        return Arrays.asList(new Object[][]
                {{0, 1}, {1, 0}, {2, 1}, {1, 2}, {2, 2}, {3, 5}, {5, 5}, {11, 19}, {17, 6}}
        );
    }

    @Parameter(0)
    public int firstArrayLength;
    @Parameter(1)
    public int secondArrayLength;
    private Integer[] firstArray;
    private Integer[] secondArray;
    private ZipWithArraySpliterator<Integer, Integer> spliterator;

    private List<Pair<Integer, Integer>> arrayToList() {
        return arrayToList(0, getArrayLength());
    }

    private int getArrayLength() {
        return Math.min(firstArray.length, secondArray.length);
    }

    private List<Pair<Integer, Integer>> arrayToList(int skip, int limit) {
        return Stream
                .iterate(0, i -> i + 1)
                .map(i -> new Pair<>(firstArray[i], secondArray[i]))
                .skip(skip)
                .limit(limit)
                .collect(toList());
    }

    private List<Pair<Integer, Integer>> tryAdvance(Spliterator spliterator) {
        return (List<Pair<Integer, Integer>>) StreamSupport
                .stream(spliterator, false).collect(toList());
    }

    @Before
    public void getRandomArray() {
        firstArray = new Integer[firstArrayLength];
        for (int i = 0; i < firstArrayLength; i++) {
            firstArray[i] = ThreadLocalRandom.current().nextInt();
        }
        secondArray = new Integer[secondArrayLength];
        for (int i = 0; i < secondArrayLength; i++) {
            secondArray[i] = ThreadLocalRandom.current().nextInt();
        }
        spliterator = new ZipWithArraySpliterator<>(
                Arrays.spliterator(firstArray), secondArray);
    }

    @Test
    public void comparePaired() {
        final List<Pair<Integer, Integer>> expected =
                arrayToList();

        final List<Pair<Integer, Integer>> result =
                StreamSupport.stream(spliterator, true)
                        .map(p -> new Pair<>(p.getA() - 1, p.getB()))
                        .map(p -> new Pair<>(p.getA() + 1, p.getB()))
                        .collect(toList());

        assertEquals(expected, result);
    }

    @Test
    public void estimateSize() {
        assertEquals(getArrayLength(), spliterator.estimateSize());
    }

    @Test
    public void forEachRemaining() {
        final List<Pair<Integer, Integer>> result = new ArrayList<>();
        final Consumer<Pair<Integer, Integer>> c = result::add;

        spliterator.forEachRemaining(c);

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList());
    }

    @Test
    public void tryAdvance() {
        final List<Pair<Integer, Integer>> result = new ArrayList<>();
        final Consumer<Pair<Integer, Integer>> c = result::add;

        while (spliterator.tryAdvance(c)) ;

        assertEquals(0, spliterator.estimateSize());
        assertEquals(result, arrayToList());
    }

    @Test
    public void trySplitInHalf() {
        final Spliterator split = spliterator.trySplit();
        final int arrayLength = getArrayLength();
        final int rightLength = (arrayLength % 2 == 0) ? arrayLength / 2 : arrayLength / 2 + 1;
        final int leftLength = arrayLength / 2;

        if (arrayLength <= 1) {
            assertNull(split);
        } else {
            assertEquals(leftLength, split.estimateSize());
            final List<Pair<Integer, Integer>> leftExpected = arrayToList(0, leftLength);
            final List<Pair<Integer, Integer>> leftResult = tryAdvance(split);
            assertEquals(leftExpected, leftResult);
        }

        assertEquals(rightLength, spliterator.estimateSize());
        final List<Pair<Integer, Integer>> rightExpected = arrayToList(leftLength, rightLength);
        final List<Pair<Integer, Integer>> rightResult = tryAdvance(spliterator);
        assertEquals(rightExpected, rightResult);
    }

}

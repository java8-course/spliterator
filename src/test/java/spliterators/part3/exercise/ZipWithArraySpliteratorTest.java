package spliterators.part3.exercise;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ZipWithArraySpliteratorTest {

    private String[] getRandomArray(int length) {
        final String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            result[i] = String.valueOf(ThreadLocalRandom.current().nextInt());
        }
        return result;
    }

    @Test
    public void tryAdvanceTest() {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        String[] array = getRandomArray(5);
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        spliterator.tryAdvance(p -> assertEquals(new Pair<>(1, array[0]), p));

        assertEquals(2, spliterator.estimateSize());
    }

    @Test
    public void trySplitTest() {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        String[] array = {"a", "b", "c", "d", "e"};
        List<Pair<Integer, String>> expected = Arrays.asList(new Pair<>(1, "a"), new Pair<>(2, "b"), new Pair<>(3, "c"));
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        long originSize = spliterator.estimateSize();
        Spliterator<Pair<Integer, String>> spliterator1 = spliterator.trySplit();
        long remainderSize = spliterator.estimateSize();
        long secondSize = spliterator1.estimateSize();

        assertEquals(originSize, remainderSize + secondSize);

        List<Pair<Integer, String>> list1 = StreamSupport.stream(spliterator1, false).collect(Collectors.toList());
        List<Pair<Integer, String>> list2 = StreamSupport.stream(spliterator, false).collect(Collectors.toList());
        list1.addAll(list2);

        assertEquals(expected, list1);
    }

    @Test
    public void emptyStreamTest(){
        String[] array = {"abc"};
        Stream<Integer> empty = Stream.empty();
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(empty.spliterator(), array);
        assertEquals(0, spliterator.estimateSize());
        assertNull(spliterator.trySplit());
    }

    @Test
    public void forEachRemainingTest() {
        Stream<Integer> stream = Stream.of(1, 2);
        String[] array = {"John", "Paul", "Henry", "Bill", "Max"};
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);

        List<Pair<Integer, String>> expected = Arrays.asList(new Pair<>(1, "John"), new Pair<>(2, "Paul"));
        List<Pair<Integer, String>> result = new ArrayList<>();
        spliterator.forEachRemaining(result::add);
        assertEquals(expected, result);
        assertFalse(spliterator.tryAdvance(System.out::print));
    }

    @Test
    public void spliteratorStreamTest() {
        final String[] array = {"4", "5", "6", "9"};
        Stream<Integer> stream = Stream.of(1, 2, 3);

        Stream<Pair<Integer, String>> pairStream = StreamSupport
                .stream(
                        new ZipWithArraySpliterator<>(stream.spliterator(), array),
                        true);

        int sum = pairStream
                .mapToInt(value -> value.getA() + Integer.parseInt(value.getB()))
                .sum();

        assertEquals(21, sum);
    }
}

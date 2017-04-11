package spliterators.part3.exercise;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ZipWithArraySpliteratorTest {
    @Test
    public void tryAdvanceTest() {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        String[] array = generateArray(5);
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        spliterator.tryAdvance(p -> assertEquals(new Pair<>(1, array[0]), p));

        assertEquals(2, spliterator.estimateSize());
    }

    @Test
    public void trySplitTest() {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        String[] array = {"d", "b", "z"};
        List<Pair<Integer, String>> expected = Arrays.asList(new Pair<>(1, "d"), new Pair<>(2, "b"), new Pair<>(3, "z"));
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        long originSize = spliterator.estimateSize();
        Spliterator<Pair<Integer, String>> spliterator1 = spliterator.trySplit();
        long size = spliterator.estimateSize();
        long size1 = spliterator1.estimateSize();
        List<Pair<Integer, String>> list1 = StreamSupport.stream(spliterator1, false).collect(Collectors.toList());
        List<Pair<Integer, String>> list2 = StreamSupport.stream(spliterator, false).collect(Collectors.toList());
        list1.addAll(list2);

        assertEquals(originSize, size + size1);
        assertEquals(expected, list1);
    }

    @Test
    public void emptyStreamTest(){
        String[] array = {"dbz"};
        Stream<Integer> empty = Stream.empty();
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(empty.spliterator(), array);

        assertEquals(0, spliterator.estimateSize());
        assertNull(spliterator.trySplit());
    }

    @Test
    public void forEachRemainingTest() {
        Stream<Integer> stream = Stream.of(1, 2);
        String[] array = {"John", "Ace", "Danny", "Felix"};
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        List<Pair<Integer, String>> expected = Arrays.asList(new Pair<>(1, "John"), new Pair<>(2, "Ace"));
        List<Pair<Integer, String>> result = new ArrayList<>();
        spliterator.forEachRemaining(result::add);

        assertEquals(expected, result);
        assertFalse(spliterator.tryAdvance(System.out::print));
    }

    @Test
    public void spliteratorStreamTest() {
        final String[] array = {"1", "2", "3"};
        Stream<Integer> stream = Stream.of(1, 2, 3);
        Stream<Pair<Integer, String>> pairStream = StreamSupport.stream(
                        new ZipWithArraySpliterator<>(stream.spliterator(), array),
                        true);
        int sum = pairStream
                .mapToInt(value -> value.getA() + Integer.parseInt(value.getB()))
                .sum();

        assertEquals(12, sum);
    }

    private String[] generateArray(int size) {
        Random rand = new Random();
        final String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = String.valueOf(rand.nextInt());
        }
        return result;
    }
}
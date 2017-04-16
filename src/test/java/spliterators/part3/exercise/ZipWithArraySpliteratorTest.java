package spliterators.part3.exercise;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class ZipWithArraySpliteratorTest {

    private String[] getRandomArray(int length) {
        final String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            result[i] = String.valueOf(ThreadLocalRandom.current().nextInt());
        }

        return result;
    }





    @Test
    public void tryAdvanceTest() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        String[] array = getRandomArray(2);
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        spliterator.tryAdvance(p -> assertEquals(new Pair<>(1, array[0]), p));
        assertEquals(2, spliterator.estimateSize());
    }

    @Test
    public void trySplitTest() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        String[] array = {"A", "S", "S", "I", "N"};
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        long expected = spliterator.estimateSize();
        Spliterator<Pair<Integer, String>> spliterator1 = spliterator.trySplit();
        long firstPart = spliterator1.estimateSize();
        long secondPart = spliterator.estimateSize();
        assertEquals(expected, firstPart + secondPart);
        List<Pair<Integer, String>> collect1 = StreamSupport.stream(spliterator1, false).collect(Collectors.toList());
        List<Pair<Integer, String>> collect2 = StreamSupport.stream(spliterator, false).collect(Collectors.toList());
        collect1.addAll(collect2);
        assertEquals(Arrays.asList(new Pair<>(1, "A"), new Pair<>(2, "S"), new Pair<>(3, "S")), collect1);
    }

    @Test
    public void forEachRemainingTest() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        String[] array = {"A", "S", "S", "I", "N"};
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        List<Pair<Integer, String>> expected = Arrays.asList(new Pair<>(1, "A"), new Pair<>(2, "S"), new Pair<>(3, "S"));
        List<Pair<Integer, String>> result = new ArrayList<>();
        spliterator.forEachRemaining(result::add);
        assertEquals(expected, result);
        assertFalse(spliterator.tryAdvance(System.out::println));
    }

    @Test
    public void minEstimateSizeTest() throws Exception {

        Stream<Integer> outStream = Stream.of(1);
        String[] randomArray = getRandomArray(5);
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(outStream.spliterator(), randomArray);

        assertEquals(1, spliterator.estimateSize());
        assertFalse(spliterator.tryAdvance(System.out::println));

        Stream<Integer> outStream1 = Stream.of(1, 2, 3);
        String[] randomArray1 = getRandomArray(1);
        ZipWithArraySpliterator<Integer, String> spliterator1 = new ZipWithArraySpliterator<>(outStream1.spliterator(), randomArray1);

        assertEquals(1, spliterator1.estimateSize());
        assertFalse(spliterator1.tryAdvance(System.out::println));
    }

    @Test
    public void zeroTest() throws Exception {
        Stream<Integer> stream = Stream.of();
        String[] array = {"A", "S", "S", "I", "N"};
        ZipWithArraySpliterator<Integer, String> spliterator = new ZipWithArraySpliterator<>(stream.spliterator(), array);
        assertEquals(0, spliterator.estimateSize());
        Spliterator<Pair<Integer, String>> spliterator1 = spliterator.trySplit();
        assertNull(spliterator1);

    }

}
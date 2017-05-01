package spliterators.part2.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;


public class ZipWithIndexDoubleSpliteratorTest {

    final double[] array = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};

    @Test
    public void testCount() {
        Spliterator.OfDouble spliterator = Arrays.spliterator(array);
        final long expected = 6;

        long actual = StreamSupport
                .stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                .count();

        assertEquals(expected, actual);
    }

    @Test
    public void testToList() {
        Spliterator.OfDouble spliterator = Arrays.spliterator(array);
        final List<Double> expected = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        List<Double> actual = StreamSupport
                .stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                .map(IndexedDoublePair::getValue)
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void testPaired() {
        Spliterator.OfDouble spliterator = Arrays.spliterator(array);
        final List<IndexedDoublePair> expected = Arrays.asList(
                new IndexedDoublePair(0, 1.0),
                new IndexedDoublePair(1, 2.0),
                new IndexedDoublePair(2, 3.0),
                new IndexedDoublePair(3, 4.0),
                new IndexedDoublePair(4, 5.0),
                new IndexedDoublePair(5, 6.0)
        );

        List<IndexedDoublePair> actual = StreamSupport
                .stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void testSkip() {
        Spliterator.OfDouble spliterator = Arrays.spliterator(array);

        double actual = StreamSupport
                .stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                .skip(3)
                .mapToDouble(IndexedDoublePair::getValue)
                .sum();

        assertTrue(15.0 == actual);
    }

    @Test
    public void testLimit() {
        Spliterator.OfDouble spliterator = Arrays.spliterator(array);

        double actual = StreamSupport
                .stream(new ZipWithIndexDoubleSpliterator(spliterator), true)
                .limit(3)
                .mapToDouble(IndexedDoublePair::getValue)
                .sum();

        assertTrue(6.0 == actual);
    }
}